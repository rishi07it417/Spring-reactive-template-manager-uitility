package com.example.reactive.template.config;


import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(
        basePackages = "com.example.reactive.repo",
        entityOperationsRef = "templateR2dbcEntityOperations"
)
public class AppConfig {

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.username}")
    private String dbUsername;

    @Value("${db.password}")
    private String dbPassword;


    @Bean
    @ConfigurationProperties("db.user")
    public ConnectionFactory userConnectionFactory() {
        return ConnectionFactories.get(
               ConnectionFactoryOptions.builder()
                       .from(ConnectionFactoryOptions.parse(dbUrl))
                        .option(ConnectionFactoryOptions.USER, dbUsername)
                        .option(ConnectionFactoryOptions.PASSWORD, dbPassword)
                       .build()
                );
    }


    @Bean
    public R2dbcEntityOperations userR2dbcEntityOperations(
            ConnectionFactory connectionFactory) {

        return new R2dbcEntityTemplate(connectionFactory);
    }
}