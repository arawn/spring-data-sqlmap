package kr.arawn.springframework.data.sqlmap.repository.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class SqlmapRepositoryNameSpaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("repositories",
                new SqlmapRepositoryConfigDefinitionParser());
    }

}
