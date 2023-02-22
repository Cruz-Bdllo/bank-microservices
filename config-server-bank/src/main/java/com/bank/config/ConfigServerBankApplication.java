package com.bank.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigServerBankApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ConfigServerBankApplication.class);
    }
}
