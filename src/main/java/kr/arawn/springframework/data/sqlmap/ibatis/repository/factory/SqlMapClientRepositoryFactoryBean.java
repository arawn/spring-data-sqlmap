package kr.arawn.springframework.data.sqlmap.ibatis.repository.factory;

import java.io.Serializable;

import kr.arawn.springframework.data.sqlmap.repository.SqlmapRepository;

import org.springframework.data.repository.support.RepositoryFactorySupport;
import org.springframework.data.repository.support.TransactionalRepositoryFactoryBeanSupport;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.util.Assert;


public class SqlMapClientRepositoryFactoryBean<T extends SqlmapRepository<S, ID>, S, ID extends Serializable>
        extends TransactionalRepositoryFactoryBeanSupport<T, S, ID> {
    
    private SqlMapClientTemplate sqlmapExecutor;
    
    public void setSqlmapExecutor(SqlMapClientTemplate sqlmapExecutor) {
        this.sqlmapExecutor = sqlmapExecutor;
    }
    
    @Override
    public void afterPropertiesSet() {
        Assert.notNull(sqlmapExecutor, "sqlMapClientTemplate must not be null!");
        super.afterPropertiesSet();
    }
    
    @Override
    protected RepositoryFactorySupport doCreateRepositoryFactory() {
        return new SqlmapClientRepositoryFactory(sqlmapExecutor);
    }

}
