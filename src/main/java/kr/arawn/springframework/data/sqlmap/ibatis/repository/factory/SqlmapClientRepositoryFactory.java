package kr.arawn.springframework.data.sqlmap.ibatis.repository.factory;

import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import kr.arawn.springframework.data.sqlmap.ibatis.repository.DefaultSqlmapClientRepository;
import kr.arawn.springframework.data.sqlmap.repository.Namespace;
import kr.arawn.springframework.data.sqlmap.repository.factory.SqlmapEntityInformation;
import kr.arawn.springframework.data.sqlmap.repository.factory.SqlmapRepositoryFactory;
import kr.arawn.springframework.data.sqlmap.repository.query.SqlmapQueryLookupStrategy;
import kr.arawn.springframework.data.sqlmap.repository.statement.Statement;
import kr.arawn.springframework.data.sqlmap.repository.statement.StatementInformation;
import kr.arawn.springframework.data.sqlmap.repository.statement.StatementInformation.StatementIdType;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.data.repository.support.RepositoryMetadata;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.util.ClassUtils;

public class SqlmapClientRepositoryFactory extends SqlmapRepositoryFactory {

    private SqlMapClientTemplate sqlMapClientTemplate;
    private StatementInformation statement;;

    public SqlmapClientRepositoryFactory(SqlMapClientTemplate sqlMapClientTemplate) {
        this.sqlMapClientTemplate = sqlMapClientTemplate;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected Object getTargetRepository(RepositoryMetadata metadata) {
        generateStatementInformation(metadata.getRepositoryInterface(), metadata.getDomainClass());
        
        return new DefaultSqlmapClientRepository(
                this.sqlMapClientTemplate,
                (SqlmapEntityInformation) getEntityInformation(metadata.getDomainClass()),
                this.statement);
    }
    
    @Override
    protected QueryLookupStrategy getQueryLookupStrategy(Key key) {
        return SqlmapQueryLookupStrategy.create(key, this.sqlMapClientTemplate, this.statement);
    }
    
    protected void generateStatementInformation(Class<?> repositoryInterface, Class<?> domainClass) {
        Map<String, String> map = new HashMap<String, String>();
        
        String rootNameSpace = null;
        Namespace namespaceAnnotation = AnnotationUtils.findAnnotation(repositoryInterface, Namespace.class);
        if (namespaceAnnotation != null)
            rootNameSpace = namespaceAnnotation.value();
        else
            rootNameSpace = Introspector.decapitalize(ClassUtils.getShortName(domainClass));

        for(StatementIdType id : StatementIdType.values()) {
            if(rootNameSpace != null && rootNameSpace.length() > 0)
                map.put(id.name(), rootNameSpace + "." + id.name());
            else
                map.put(id.name(), id.name());
        }

        for (Method method : repositoryInterface.getDeclaredMethods()) {
            Statement statement = AnnotationUtils.findAnnotation(method, Statement.class);
            if (statement != null)
                map.put(method.getName(), statement.id());
            else if (!map.containsKey(method.getName()))
                if(rootNameSpace != null && rootNameSpace.length() > 0)
                    map.put(method.getName(), rootNameSpace + "." + method.getName());
                else
                    map.put(method.getName(), method.getName());
        }
        
        this.statement = new StatementInformation(map);
    }

}
