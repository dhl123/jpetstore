package com.software.jpetstore;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * Created by wq on 2016/8/15.
 */
@Configuration
public class LocaleConfig {
    @Bean(name="localeResolver")
    public LocaleResolver localeResolverBean() {
        return new SessionLocaleResolver();
    }
    @Bean(name="messageSource")
    public ResourceBundleMessageSource resourceBundleMessageSource(){
        ResourceBundleMessageSource source=new ResourceBundleMessageSource();
        source.setBasename("messages");
        return source;
    }
}

