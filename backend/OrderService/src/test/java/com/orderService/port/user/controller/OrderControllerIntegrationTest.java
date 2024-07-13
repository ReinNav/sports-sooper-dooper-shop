package com.orderService.port.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderService.core.domain.model.Order;
import com.orderService.core.domain.model.OrderStatus;
import com.orderService.core.domain.service.interfaces.OrderRepository;
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

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser
public class OrderControllerIntegrationTest {

    private static final String POSTGRES_IMAGE = "postgres:16.3-bullseye";
    private static final String POSTGRES_DB = "testdb";
    private static final String POSTGRES_USER = "test";
    private static final String POSTGRES_PASSWORD = "test";

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_IMAGE))
            .withDatabaseName(POSTGRES_DB)
            .withUsername(POSTGRES_USER)
            .withPassword(POSTGRES_PASSWORD)
            .withEnv("POSTGRESQL_DATABASE", POSTGRES_DB)
            .withEnv("POSTGRESQL_USER", POSTGRES_USER)
            .withEnv("POSTGRESQL_PASSWORD", POSTGRES_PASSWORD)
            .withExposedPorts(PostgreSQLContainer.POSTGRESQL_PORT);

    @Container
    public static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.9.13-management-alpine")
            .withExposedPorts(5672, 15672);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

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
        orderRepository.deleteAll();
        rabbitAdmin.declareQueue(new Queue("payment_finished"));
        rabbitAdmin.declareQueue(new Queue("order_confirmed"));
    }

    @Test
    void testCreateOrder() throws Exception {
        Order order = new Order();
        order.setUserId("user1");
        order.setDate("2023-07-13");
        order.setOrderItems(Collections.emptyList());
        order.setTotalAmount(BigDecimal.valueOf(100));
        order.setStatus(OrderStatus.PENDING);

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testGetAllOrders() throws Exception {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setUserId("user1");
        order.setDate("2023-07-13");
        order.setOrderItems(Collections.emptyList());
        order.setTotalAmount(BigDecimal.valueOf(100));
        order.setStatus(OrderStatus.PENDING);
        orderRepository.save(order);

        mockMvc.perform(get("/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user1"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    void testGetOrderById() throws Exception {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setUserId("user1");
        order.setDate("2023-07-13");
        order.setOrderItems(Collections.emptyList());
        order.setTotalAmount(BigDecimal.valueOf(100));
        order.setStatus(OrderStatus.PENDING);
        orderRepository.save(order);

        mockMvc.perform(get("/order/" + order.getOrderId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testCancelOrder() throws Exception {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setUserId("user1");
        order.setDate("2023-07-13");
        order.setOrderItems(Collections.emptyList());
        order.setTotalAmount(BigDecimal.valueOf(100));
        order.setStatus(OrderStatus.PENDING);
        orderRepository.save(order);

        mockMvc.perform(put("/order/" + order.getOrderId() + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
}
