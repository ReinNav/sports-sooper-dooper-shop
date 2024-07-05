package com.paymentService.port;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentService.core.domain.model.CompletedOrder;
import com.paymentService.core.domain.model.Payment;
import com.paymentService.core.domain.model.PaymentOrder;
import com.paymentService.core.domain.service.PaypalService;
import com.paymentService.core.domain.service.interfaces.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser
public class PaymentControllerIntegrationTest {

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
    private PaymentRepository paymentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaypalService paypalService;

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
        paymentRepository.deleteAll();

        // Mock the behavior of the PaypalService
        PaymentOrder mockPaymentOrder = new PaymentOrder("success", "paypalId", "redirectUrl");
        Mockito.when(paypalService.createPayment(any(BigDecimal.class))).thenReturn(mockPaymentOrder);

        CompletedOrder mockCompletedOrder = new CompletedOrder("success", "paypalId");
        Mockito.when(paypalService.completePayment(anyString())).thenReturn(mockCompletedOrder);
    }

    @Test
    void createPayment_Success() throws Exception {
        mockMvc.perform(post("/paypal/init")
                        .param("sum", "100.00")
                        .param("email", "test@example.com")
                        .param("orderId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void completePayment_Success() throws Exception {
        Payment payment = new Payment();
        payment.setPaypalTransactionId("paypalId");
        payment.setDate(new Date());
        payment.setAmount(BigDecimal.valueOf(100.00));
        payment.setEmail("test@example.com");
        payment.setStatus("Pending");
        payment.setOrderId(1L);
        paymentRepository.save(payment);

        mockMvc.perform(post("/paypal/capture")
                        .param("token", "paypalId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
