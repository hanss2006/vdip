package com.hanss.vdip.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

/**
 *
 */
@Configuration
public class FreemarkerConfig {
    /**
     *
     * @return FreeMarkerConfigurationFactoryBean
     */
    @Bean
    public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() {
        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
        bean.setTemplateLoaderPath("templates/");
        bean.setDefaultEncoding("UTF-8");
        return bean;
    }

}
