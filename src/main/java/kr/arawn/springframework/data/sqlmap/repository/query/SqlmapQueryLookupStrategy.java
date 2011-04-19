package kr.arawn.springframework.data.sqlmap.repository.query;

import java.lang.reflect.Method;

import kr.arawn.springframework.data.sqlmap.repository.statement.StatementInformation;

import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.support.RepositoryMetadata;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.util.Assert;

public final class SqlmapQueryLookupStrategy {

    private SqlmapQueryLookupStrategy() {
    }

    private abstract static class AbstractQueryLookupStrategy implements QueryLookupStrategy {

        private SqlMapClientTemplate template;
        private StatementInformation statement;

        public AbstractQueryLookupStrategy(SqlMapClientTemplate template, StatementInformation statement) {
            Assert.notNull(template);
            Assert.notNull(statement);
            
            this.template = template;
            this.statement = statement;
        }

        protected SqlMapClientTemplate getTemplate() {
            return template;
        }
        
        protected StatementInformation getStatement() {
            return statement;
        }

    }
    
    private static class CreateQueryLookupStrategy extends AbstractQueryLookupStrategy {

        public CreateQueryLookupStrategy(SqlMapClientTemplate template, StatementInformation statement) {
            super(template, statement);
        }

        public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata) {
            throw new UnsupportedOperationException();
        }        

    }
    
    private static class DeclaredQueryLookupStrategy extends AbstractQueryLookupStrategy {

        public DeclaredQueryLookupStrategy(SqlMapClientTemplate template, StatementInformation statement) {
            super(template, statement);
        }

        public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata) {
            return new DefaultSqlmapRepositoryQuery(getTemplate(), 
                    new DefaultSqlmapQueryMethod(method, metadata, getStatement().getStatementId(method.getName())));
        }

    }    

    private static class CreateIfNotFoundQueryLookupStrategy extends AbstractQueryLookupStrategy {

        private DeclaredQueryLookupStrategy declared;
        
        public CreateIfNotFoundQueryLookupStrategy(SqlMapClientTemplate template, StatementInformation statement) {
            super(template, statement);
            this.declared = new DeclaredQueryLookupStrategy(template, statement); 
        }

        public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata) {
            return declared.resolveQuery(method, metadata);
        }        

    }
    
    public static QueryLookupStrategy create(QueryLookupStrategy.Key key, SqlMapClientTemplate template, 
            StatementInformation statement) {
        
        if (key == null) {
            return new DeclaredQueryLookupStrategy(template, statement);
        }
        
        switch (key) {
        case CREATE:
            return new CreateQueryLookupStrategy(template, statement);            
        case USE_DECLARED_QUERY:
            return new DeclaredQueryLookupStrategy(template, statement);            
        case CREATE_IF_NOT_FOUND:
            return new CreateIfNotFoundQueryLookupStrategy(template, statement);            
        default:
            throw new IllegalArgumentException(String.format("Unsupported query lookup strategy %s!", key));
        }
        
    }

}
