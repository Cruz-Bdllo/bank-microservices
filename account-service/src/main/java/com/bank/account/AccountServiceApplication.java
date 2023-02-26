package com.bank.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableJpaRepositories("com.bank.account.repository")
@EntityScan("com.bank.account.model")
@ComponentScans({@ComponentScan("com.bank.account.controller")})
public class AccountServiceApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(AccountServiceApplication.class);
    }
}
