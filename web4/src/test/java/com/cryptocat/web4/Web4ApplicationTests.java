package com.cryptocat.web4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class Web4ApplicationTests {

    @Autowired
    BlockRepository blockRepository;

    @Autowired
    MockMvc mockMvc;

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @BeforeEach
    public void setup() {
        Iterable<Block> blocks = Arrays.asList(
                new Block("hash1"),
                new Block("hash1"),
                new Block("hash1"),
                new Block("hash1"),
                new Block("hash1"),
                new Block("hash1"),
                new Block("hash1")
        );
        blockRepository.saveAll(blocks);
    }

    @Test
    void contextLoads() {
        List<Block> all = blockRepository.findAll();
        assertThat(all.size()).isEqualTo(7);
    }

    @Test
    void testSave() throws Exception {
        this.mockMvc.perform(get("/blocks"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.*", hasSize(7))
                );
    }

}
