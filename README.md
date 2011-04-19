Spring Data SqlMap
==================
[Spring Data](http://www.springsource.org/spring-data) 를 사용해서 쉽게 DAO 를 구현하기 위해 만들었습니다.


Features
--------
* CoC 로 SqlMap statement(insert, update, delete, select 등)를 호출
* SqlMapClientTemplate 을 사용하는 CRUD 메소드 구현
* 사용자 정의 메소드 선언 가능


# Quick Start

### spring-data-sqlmap 설치 :

1. 프로젝트 다운로드
2. maven 을 사용해 jar 생성
3. classpath에 target/spring-data-sqlmap.X.X.X.jar 추가


	git clone https://github.com/arawn/spring-data-sqlmap.git
	mvn jar:jar


### Entity(Domain, DTO 등) 작성 :

테이블의 PK 컬럼의 멤버 변수명은 'id' 로 명명 ( id field 가 없으면 예외 발생 )

	class SpringSprout {
		private Long id;
		private String name;
		private String nickName;

		// Getters and setters
	}


### Repository interface 작성 :

* SqlmapRepository interface 를 상속받아 Generic 에 Entity type 과 ID type(pk colum) 을 선언
* 필요에 따라서 사용자 정의 메소드 선언 ( 해당 메소드는 CoC 에 따라서 메소드 명과 동일한 statement id 를 호출 )
* 필요에 따라서 statement 를 직접 선언


	public interface SpringSproutRepository extends SqlmapRepository<SpringSprout, Long> {

		List<SpringSprout> findByName(String name);
    
		@Statement(id="springSprout.selectSpringSprout")
		List<SpringSprout> findByCondition(SpringSproutCondition condition);
    
	}


### SqlMap 작성 :

* Entity type 과 동일한 namespace 선언
* insert, update, delete, deleteAll, findOne, findAll, findAll_Sort, findAll_Pageable, count statement 작성
* 사용자 정의 statement 작성 ( SpringSproutRepository.findByName = 'findByName', SpringSproutRepository.findByCondition = 'selectSpringSprouts' )


	<?xml version="1.0" encoding="UTF-8"?>
	<!DOCTYPE sqlMap PUBLIC "-//ibatis.apache.org//DTD SQL Map 2.0//EN" "http://ibatis.apache.org/dtd/sql-map-2.dtd">
	<sqlMap namespace="springSprout">

		<typeAlias alias="Pageable" type="org.springframework.data.domain.Pageable"/>

		<typeAlias alias="SpringSprout" type="kr.arawn.springframework.data.sqlmap.repository.sample.SpringSprout" />
		<typeAlias alias="SpringSproutCondition" type="kr.arawn.springframework.data.sqlmap.repository.sample.SpringSproutCondition"/>

		<insert id="insert" parameterClass="SpringSprout">
			INSERT INTO SPRINGSPROUT(NAME, NICKNAME) VALUES(#name#, #nickName#);
			<selectKey resultClass="Long" keyProperty="id">
				CALL IDENTITY()
			</selectKey>
		</insert>
		
		<update id="update" parameterClass="SpringSprout">
			...
		</update>
		
		<delete id="delete" parameterClass="long">
			...
		</delete>
		
		<delete id="deleteAll">
			...
		</delete>
		
		<select id="findOne" parameterClass="long" resultClass="SpringSprout">
			...
		</select>
		
		<select id="findAll" resultClass="SpringSprout">
			...
		</select>    
		
		<select id="findAll_Sort" resultClass="SpringSprout" parameterClass="map">
			SELECT * FROM SPRINGSPROUT T
			<iterate property="orders" open="order by" close="" conjunction=", ">
				$orders[].property$ $orders[].direction$
			</iterate>
		</select>
		
		<select id="findAll_Pageable" resultClass="SpringSprout" parameterClass="Pageable">
			SELECT * FROM SPRINGSPROUT T LIMIT #pageSize# OFFSET #offset#
		</select>
		
		<select id="count">
			...
		</select>
		
		<select id="findByName" parameterClass="string" resultClass="SpringSprout">
			SELECT * FROM SPRINGSPROUT WHERE NAME like #name# + '%'
		</select>
		
		<select id="selectSpringSprout" resultClass="SpringSprout" parameterClass="SpringSproutCondition">
			SELECT * FROM SPRINGSPROUT T
			<isGreaterThan property="pageSize" compareValue="0">
				LIMIT #pageSize# OFFSET #offset#
			</isGreaterThan>        
			<iterate property="orders" open="order by" close="" conjunction=", ">
				$orders[].property$ $orders[].direction$
			</iterate>
		</select>

	</sqlMap>


### Spring applicationContext 설정 :

* sqlmap 네임스페이스 선언
* sqlmap-executor-ref 에 SqlMapClient 주입
* base-package 에 Repository 가 선언된 패키지 지정 ( 자동 검색 )


	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
		xmlns:jdbc="http://www.springframework.org/schema/jdbc"
		xmlns:sqlmap="http://www.springframework.org/schema/data/sqlmap"
		xsi:schemaLocation="
			http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd 
			http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc-3.0.xsd
			http://www.springframework.org/schema/data/sqlmap http://www.springframework.org/schema/data/sqlmap/spring-sqlmap-1.0.xsd">

		<jdbc:embedded-database id="dataSource" type="HSQL" />

		<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
			<property name="dataSource" ref="dataSource" />
		</bean>

		<bean id="sqlMapClient" class="org.springframework.orm.ibatis.SqlMapClientFactoryBean">
			<property name="dataSource" ref="dataSource" />
			<property name="configLocation" value="classpath:META-INF/ibatis/config.xml" />
			<property name="mappingLocations" value="classpath:META-INF/ibatis/Sqlmap*.xml" />
		</bean>

		<sqlmap:repositories sqlmap-executor-ref="sqlMapClient"   
												base-package="kr.arawn.springframework.data.sqlmap.repository.sample" />

	</beans>


### 테스트 케이스 작성 :

	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration
	public class SpringSproutRepositoryTest {
		
		@Autowired SpringSproutRepository repository;
		
		SpringSprout firstSpringSprout, secondSpringSprout, thirdSpringSprout;
		
		@Before
		public void 초기화() {
			firstSpringSprout = new SpringSprout("동욱", "ldw");
			secondSpringSprout = new SpringSprout("기선", "bks");
			thirdSpringSprout = new SpringSprout("연희", "jyh");
		}
		
		@Test
		public void 등록() {
			repository.save(firstSpringSprout);
			repository.save(secondSpringSprout);
			repository.save(thirdSpringSprout);
			
			assertThat(firstSpringSprout.getId(), is(notNullValue()));
			assertThat(secondSpringSprout.getId(), is(notNullValue()));
			assertThat(thirdSpringSprout.getId(), is(notNullValue()));
		}

		// 기타 테스트 메소드...

	}
