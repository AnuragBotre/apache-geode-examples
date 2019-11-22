package com.trendcore.cache.springboot;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

import java.util.Properties;

@Configuration
@EnableGemfireRepositories
public class ApplicationConfiguration {

    @Bean
    public Properties applicationSettings() {
        Properties applicationSettings = new Properties();

        applicationSettings.setProperty("app.gemfire.default.region.initial-capacity", "51");
        applicationSettings.setProperty("app.gemfire.default.region.load-factor", "0.85");

        return applicationSettings;
    }

    @Bean
    public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();

        propertyPlaceholderConfigurer.setProperties(applicationSettings());

        return propertyPlaceholderConfigurer;
    }

}
