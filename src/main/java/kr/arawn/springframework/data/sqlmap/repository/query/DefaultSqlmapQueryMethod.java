package kr.arawn.springframework.data.sqlmap.repository.query;

import java.lang.reflect.Method;

import kr.arawn.springframework.data.sqlmap.repository.statement.Statement;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.support.RepositoryMetadata;

public class DefaultSqlmapQueryMethod extends QueryMethod {
    
    private final String statementId;
    private final boolean modifying;
    
    public DefaultSqlmapQueryMethod(Method method, RepositoryMetadata metadata, String statementId) {
        super(method, metadata);
        
        this.statementId = statementId;
        
        Statement statement = AnnotationUtils.findAnnotation(method, Statement.class);
        if (statement != null)
            this.modifying = statement.modifying();
        else
            this.modifying = false;
    }

    public String getStatementId() {
        return statementId;
    }

    @Override
    protected boolean isModifyingQuery() {
        return modifying;
    }
    
}
