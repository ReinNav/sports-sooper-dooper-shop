package com.productService.port.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productService.core.domain.model.Product;
import com.productService.core.domain.service.interfaces.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser
class ProductControllerIntegrationTest {

    private static final String POSTGRES_IMAGE = "postgres:16.3-bullseye";
    private static final String POSTGRES_DB = "testdb";
    private static final String POSTGRES_USER = "test";
    private static final String POSTGRES_PASSWORD = "test";

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_IMAGE))
            .withDatabaseName(POSTGRES_DB)
            .withUsername(POSTGRES_USER)
            .withPassword(POSTGRES_PASSWORD)
            .withExposedPorts(PostgreSQLContainer.POSTGRESQL_PORT);

    @Container
    public static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.9.13-management-alpine")
            .withExposedPorts(5672, 15672);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbitMQContainer::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitMQContainer::getAdminPassword);
    }

    @BeforeEach
    void setUp() {
        postgresContainer.setWaitStrategy(Wait.defaultWaitStrategy().withStartupTimeout(Duration.of(60, SECONDS)));
        productRepository.deleteAll();
        rabbitAdmin.declareQueue(new Queue("product_events"));
    }

    @Test
    void createProduct() throws Exception {
        Product product = new Product();
        product.setName("Test Product");

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated());
    }

    @Test
    void getProduct() throws Exception {
        Product product = new Product();
        product.setName("Test Product");
        product = productRepository.save(product);

        mockMvc.perform(get("/product/{id}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void updateProduct() throws Exception {
        Product product = new Product();
        product.setName("Test Product");
        product = productRepository.save(product);

        product.setName("Updated Product");

        mockMvc.perform(put("/product/admin/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/product/{id}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"));
    }

    @Test
    void deleteProduct() throws Exception {
        Product product = new Product();
        product.setName("Test Product");
        product = productRepository.save(product);

        mockMvc.perform(delete("/product/admin/{id}", product.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/product/{id}", product.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProducts() throws Exception {
        mockMvc.perform(get("/product/products"))
                .andExpect(status().isOk());
    }
}
