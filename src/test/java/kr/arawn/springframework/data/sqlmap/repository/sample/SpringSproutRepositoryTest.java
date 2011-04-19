package kr.arawn.springframework.data.sqlmap.repository.sample;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TransactionConfiguration
@Transactional
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
    
    @Test
    public void 하나_가져오기() {
        repository.save(firstSpringSprout);
        
        SpringSprout sprout = repository.findOne(firstSpringSprout.getId());
        
        assertThat(sprout, is(notNullValue()));
        assertThat(sprout.getName(), is(firstSpringSprout.getName()));
    }
    
    @Test
    public void 이름으로_검색하기() {
        repository.save(firstSpringSprout);
        
        List<SpringSprout> springSprouts = repository.findByName("동");
        assertThat(springSprouts.size(), is(1));
    }
    
    @Test
    public void 전체_조회() {
        repository.save(firstSpringSprout);
        repository.save(secondSpringSprout);
        repository.save(thirdSpringSprout);
        
        List<SpringSprout> springSprouts = repository.findAll();
        assertThat(springSprouts.size(), is(3));
    }
    
    @Test
    public void 정렬_조회() {
        repository.save(firstSpringSprout);
        repository.save(secondSpringSprout);
        repository.save(thirdSpringSprout);
        
        Sort sort = new Sort(new Sort.Order(Direction.DESC, "nickname"), new Sort.Order(Direction.ASC, "name"));
        
        List<SpringSprout> springSprouts = repository.findAll(sort);
        assertThat(springSprouts.size(), is(3));
    }
    
    @Test
    public void 페이지_조회() {
        for(int idx=1; idx<=30; idx++) {
            repository.save(new SpringSprout("Page", String.valueOf(idx)));
        }
        
        SpringSproutCondition condition = new SpringSproutCondition();
        condition.setPageNumber(1);
        condition.setPageSize(5);
        
        Page<SpringSprout> page = repository.findAll(condition);
        assertThat(page, is(notNullValue()));
    }
    
    @Test
    public void 사용자_정의_조회() {
        for(int idx=1; idx<=30; idx++) {
            repository.save(new SpringSprout("Page", String.valueOf(idx)));
        }
        
        SpringSproutCondition condition = new SpringSproutCondition();
        
        List<SpringSprout> springSprouts = repository.findByCondition(condition);
        assertThat(springSprouts, is(notNullValue()));
    }

}
