package kr.arawn.springframework.data.sqlmap.repository.sample;

import java.util.List;

import kr.arawn.springframework.data.sqlmap.repository.SqlmapRepository;
import kr.arawn.springframework.data.sqlmap.repository.statement.Statement;

public interface SpringSproutRepository extends SqlmapRepository<SpringSprout, Long> {

    List<SpringSprout> findByName(String name);
    
    @Statement(id="springSprout.selectSpringSprout")
    List<SpringSprout> findByCondition(SpringSproutCondition condition);
    
}
