package kr.arawn.springframework.data.sqlmap.repository.config;

import kr.arawn.springframework.data.sqlmap.repository.config.DefaultSqlmapRepositoryConfiguration.SqlmapRepositoryConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.data.repository.config.AbstractRepositoryConfigDefinitionParser;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.w3c.dom.Element;


public class SqlmapRepositoryConfigDefinitionParser
        extends
        AbstractRepositoryConfigDefinitionParser<DefaultSqlmapRepositoryConfiguration, SqlmapRepositoryConfiguration> {
    
    private static final Logger logger = LoggerFactory.getLogger(SqlmapRepositoryConfigDefinitionParser.class); 
    
    private static final String CLASS_NAME_SQLMAPCLIENT = "com.ibatis.sqlmap.client.SqlMapClient";
    private static final String CLASS_NAME_SQLMAPCLIENT_FACTORYBEAN = "org.springframework.orm.ibatis.SqlMapClientFactoryBean";
    private static final String CLASS_NAME_SQLMAPCLIENT_TEMPLATE = "org.springframework.orm.ibatis.SqlMapClientTemplate";

    private static final Class<?> DO_NOTHING_PERSISTENCE_EXCEPTION_TRANSLATOR = 
        DoNothingPersistenceExceptionTranslator.class;
    private static final Class<?> SQLMAPCLIENT_TEMPLATE = 
        SqlMapClientTemplate.class;
    
    private BeanDefinition sqlMapClientTemplateBeanDefinition;
    
    
    @Override
    protected DefaultSqlmapRepositoryConfiguration getGlobalRepositoryConfigInformation(Element element) {
        return new DefaultSqlmapRepositoryConfiguration(element);
    }

    @Override
    protected void postProcessBeanDefinition(
            SqlmapRepositoryConfiguration context,
            BeanDefinitionBuilder builder, BeanDefinitionRegistry registry,
            Object beanSource) {
        
        BeanDefinition beanDefinition = registry.getBeanDefinition(context.getSqlmapExecutorRef());
        
        logger.info("sqlmap-executor-ref type name is {}", beanDefinition.getBeanClassName());
        
        if(CLASS_NAME_SQLMAPCLIENT.equals(beanDefinition.getBeanClassName())) {
            builder.addPropertyValue("sqlmapExecutor", getSqlMapClientTemplateBeanDefinition(beanDefinition));
        }
        else if(CLASS_NAME_SQLMAPCLIENT_FACTORYBEAN.equals(beanDefinition.getBeanClassName())) {
            builder.addPropertyValue("sqlmapExecutor", getSqlMapClientTemplateBeanDefinition(beanDefinition));
        }
        else if(CLASS_NAME_SQLMAPCLIENT_TEMPLATE.equals(beanDefinition.getBeanClassName())) {
            builder.addPropertyValue("sqlmapExecutor", beanDefinition);
        }
        else {
            builder.addPropertyReference("sqlmapExecutor", context.getSqlmapExecutorRef());
        }
    }
    
    protected BeanDefinition getSqlMapClientTemplateBeanDefinition(BeanDefinition sqlMapClientBeanDefinition) {
        if(sqlMapClientTemplateBeanDefinition == null) {
            logger.info("create sqlMapClientTemplate beanDefinition.");
            
            sqlMapClientTemplateBeanDefinition = BeanDefinitionBuilder
                .rootBeanDefinition(SQLMAPCLIENT_TEMPLATE)
                .addPropertyValue("sqlMapClient", sqlMapClientBeanDefinition)
                .getBeanDefinition();
        }
        
        return sqlMapClientTemplateBeanDefinition;
    }
    
    @Override
    protected void registerBeansForRoot(BeanDefinitionRegistry registry, Object source) {
        super.registerBeansForRoot(registry, source);
        
        if (!hasBean(DO_NOTHING_PERSISTENCE_EXCEPTION_TRANSLATOR, registry)) {
            AbstractBeanDefinition definition =
                BeanDefinitionBuilder
                .rootBeanDefinition(DO_NOTHING_PERSISTENCE_EXCEPTION_TRANSLATOR)
                .getBeanDefinition();
            
            logger.info("{} register.", definition.getBeanClassName());
            
            registerWithSourceAndGeneratedBeanName(registry, definition, source);
        }
        
    }

}
