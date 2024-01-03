package com.cryptocat.front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestFrontApplication {

	@Bean
	@ServiceConnection
//	@RestartScope
	PostgreSQLContainer<?> postgresContainer() {
		System.out.println("------------------Init postgres container----------------");
		return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
				.withDatabaseName("test")
				.withUsername("test")
				.withPassword("test");
	}

	public static void main(String[] args) {
		SpringApplication.from(FrontApplication::main).with(TestFrontApplication.class).run(args);
	}

}
