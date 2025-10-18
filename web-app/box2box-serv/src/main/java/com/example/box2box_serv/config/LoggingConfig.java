package com.example.box2box_serv.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {
    private static final Logger log = LoggerFactory.getLogger(LoggingConfig.class);

    @PostConstruct
    public void init(){
        log.info("Logging configuration initialized !");
    }
}
