package kr.arawn.springframework.data.sqlmap.repository.query;

import java.util.Arrays;

import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class DefaultSqlmapRepositoryQuery implements RepositoryQuery {
    
    private SqlMapClientTemplate template;
    private DefaultSqlmapQueryMethod queryMethod;
    
    public DefaultSqlmapRepositoryQuery(SqlMapClientTemplate template, DefaultSqlmapQueryMethod queryMethod) {
        this.template = template;
        this.queryMethod = queryMethod;
    }
    
    public Object execute(Object[] parameters) {
        
        if(getQueryMethod().getType() == QueryMethod.Type.MODIFYING) {
            if(isEmpty(parameters))
                return template.update(queryMethod.getStatementId());
            else if(isSingelParameters(parameters))
                return template.update(queryMethod.getStatementId(), parameters[0]);
            else
                throw new IllegalArgumentException("parameter 가 너무 많습니다. [" + Arrays.toString(parameters) + "]");
        }
        else if(getQueryMethod().getType() == QueryMethod.Type.SINGLE_ENTITY) {
            if(isEmpty(parameters))
                return template.queryForObject(queryMethod.getStatementId());
            else if(isSingelParameters(parameters))
                return template.queryForObject(queryMethod.getStatementId(), parameters[0]);
            else
                throw new IllegalArgumentException("parameter 가 너무 많습니다. [" + Arrays.toString(parameters) + "]");
        }
        else if(getQueryMethod().getType() == QueryMethod.Type.COLLECTION) {
            if(isEmpty(parameters))
                return template.queryForList(queryMethod.getStatementId());
            else if(isSingelParameters(parameters))
                return template.queryForList(queryMethod.getStatementId(), parameters[0]);
            else
                throw new IllegalArgumentException("parameter 가 너무 많습니다. [" + Arrays.toString(parameters) + "]");
        }
        
        return null;
    }
    
    protected boolean isEmpty(Object[] parameters) {
        return parameters == null || parameters.length == 0 ? true : false; 
    }
    
    protected boolean isSingelParameters(Object[] parameters) {
        return parameters != null && parameters.length == 1 ? true : false; 
    }
    
    public QueryMethod getQueryMethod() {
        return queryMethod;
    }

}
