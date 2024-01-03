package com.cryptocat.web4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestWeb4Application {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        System.out.println("------------------Init postgres container----------------");
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                .withDatabaseName("test")
                .withUsername("test")
                .withPassword("test");
    }

    public static void main(String[] args) {
        SpringApplication.from(Web4Application::main).with(TestWeb4Application.class).run(args);
    }

}
