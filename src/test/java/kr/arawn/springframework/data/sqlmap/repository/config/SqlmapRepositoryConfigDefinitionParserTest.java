package kr.arawn.springframework.data.sqlmap.repository.config;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import kr.arawn.springframework.data.sqlmap.repository.sample.SpringSproutRepository;

import org.junit.Test;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class SqlmapRepositoryConfigDefinitionParserTest {

    @Test
    public void SqlmapClient_의존성주입_검증() throws Exception {
        
        XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource(
                "kr/arawn/springframework/data/sqlmap/repository/config/" +
                "testContext-SqlMapClientInjection.xml"));

        SqlMapExecutor_의존성_검사(factory);
    }

    @Test
    public void SqlmapClientTemplate_의존성주입_검증() throws Exception {
        
        XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource(
                "kr/arawn/springframework/data/sqlmap/repository/config/" +
                "testContext-SqlMapClientTemplateInjection.xml"));

        SqlMapExecutor_의존성_검사(factory);
    }
    
    @Test
    public void TestSqlmapClientTemplate_의존성주입_검증() throws Exception {
        
        XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource(
                "kr/arawn/springframework/data/sqlmap/repository/config/" +
                "testContext-TestSqlMapClientTemplateInjection.xml"));

        SqlMapExecutor_의존성_검사(factory);
    }
    
    private void SqlMapExecutor_의존성_검사(XmlBeanFactory factory) {
        BeanDefinition definition = factory.getBeanDefinition("springSproutRepository");
        assertThat(definition, is(notNullValue()));
        
        PropertyValue sqlmapExecutor = definition.getPropertyValues().getPropertyValue("sqlmapExecutor");
        assertThat(sqlmapExecutor, is(notNullValue()));
        
        SpringSproutRepository repository = factory.getBean("springSproutRepository", SpringSproutRepository.class);
        assertThat(repository, is(notNullValue()));
    }
    
    @Test
    public void DoNothingPersistenceExceptionTranslator가_등록되었는가() throws Exception {
        
        XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource(
                "kr/arawn/springframework/data/sqlmap/repository/config/" +
                "testContext-SqlMapClientInjection.xml"));

        DoNothingPersistenceExceptionTranslator translator = factory.getBean(DoNothingPersistenceExceptionTranslator.class);
        assertThat(translator, is(notNullValue()));
        
    }

}
