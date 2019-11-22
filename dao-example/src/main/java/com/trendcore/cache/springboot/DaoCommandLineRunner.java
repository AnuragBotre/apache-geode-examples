package com.trendcore.cache.springboot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class DaoCommandLineRunner  {

    public static void main(final String[] args) {
        new SpringApplicationBuilder(DaoCommandLineRunner.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
