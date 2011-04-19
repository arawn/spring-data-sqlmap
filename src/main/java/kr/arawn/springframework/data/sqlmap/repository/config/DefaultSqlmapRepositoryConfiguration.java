package kr.arawn.springframework.data.sqlmap.repository.config;

import kr.arawn.springframework.data.sqlmap.repository.SqlmapRepository;

import org.springframework.data.repository.config.AutomaticRepositoryConfigInformation;
import org.springframework.data.repository.config.ManualRepositoryConfigInformation;
import org.springframework.data.repository.config.RepositoryConfig;
import org.springframework.data.repository.config.SingleRepositoryConfigInformation;
import org.w3c.dom.Element;


public class DefaultSqlmapRepositoryConfiguration
        extends
        RepositoryConfig<DefaultSqlmapRepositoryConfiguration.SqlmapRepositoryConfiguration, DefaultSqlmapRepositoryConfiguration> {

    private static final String FACTORY_CLASS = 
        "kr.arawn.springframework.data.sqlmap.ibatis.repository.factory.SqlMapClientRepositoryFactoryBean";
    private static final String SQLMAP_EXECUTOR_REF = "sqlmap-executor-ref";
    
    protected DefaultSqlmapRepositoryConfiguration(Element repositoriesElement) {
        super(repositoriesElement, FACTORY_CLASS);
    }

    public SqlmapRepositoryConfiguration getAutoconfigRepositoryInformation(String interfaceName) {
        return new AutomaticSqlmapRepositoryConfigInformation(interfaceName, this);
    }

    public Class<?> getRepositoryBaseInterface() {
        return SqlmapRepository.class;
    }
    
    public String getSqlmapExecutorRef() {
        return getSource().getAttribute(SQLMAP_EXECUTOR_REF);
    }

    @Override
    protected SqlmapRepositoryConfiguration createSingleRepositoryConfigInformationFor(Element element) {
        return new ManualSqlmapRepositoryConfigInformation(element, this);
    }

    static interface SqlmapRepositoryConfiguration 
            extends SingleRepositoryConfigInformation<DefaultSqlmapRepositoryConfiguration> {

        String getSqlmapExecutorRef();
    }

    private static class AutomaticSqlmapRepositoryConfigInformation 
            extends AutomaticRepositoryConfigInformation<DefaultSqlmapRepositoryConfiguration>
            implements SqlmapRepositoryConfiguration {

        public AutomaticSqlmapRepositoryConfigInformation(String interfaceName,
                DefaultSqlmapRepositoryConfiguration parent) {
            super(interfaceName, parent);
        }

        public String getSqlmapExecutorRef() {
            return getParent().getSqlmapExecutorRef();
        }

    }

    private static class ManualSqlmapRepositoryConfigInformation
            extends ManualRepositoryConfigInformation<DefaultSqlmapRepositoryConfiguration>
            implements SqlmapRepositoryConfiguration {

        public ManualSqlmapRepositoryConfigInformation(Element element,
                DefaultSqlmapRepositoryConfiguration parent) {
            super(element, parent);
        }

        public String getSqlmapExecutorRef() {
            return getAttribute(SQLMAP_EXECUTOR_REF);
        }
    }

}
