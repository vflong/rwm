package com.lifesense.framework.rwm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@EnableAutoConfiguration
@EnableGlobalMethodSecurity
@ComponentScan(basePackageClasses = {rwmApplication.class})
public class rwmApplication {
    public static void main(String[] args) {
        SpringApplication.run(rwmApplication.class, args);
    }
}