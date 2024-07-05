package com.cartService.port;

import com.cartService.core.domain.model.Cart;
import com.cartService.core.domain.model.CartItem;
import com.cartService.core.domain.service.interfaces.CartRepository;
import com.cartService.core.domain.service.interfaces.CartItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CartControllerIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.3")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Container
    public static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.9.13-management-alpine")
            .withExposedPorts(5672, 15672);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID userId;
    private CartItem cartItem;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", () -> rabbitMQContainer.getMappedPort(5672));
        registry.add("spring.rabbitmq.username", rabbitMQContainer::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitMQContainer::getAdminPassword);
    }

    @BeforeEach
    void setUp() {
        cartRepository.deleteAll();
        cartItemRepository.deleteAll();

        userId = UUID.randomUUID();

        cartItem = new CartItem();
        cartItem.setProductId(UUID.randomUUID());
        cartItem.setName("Test Product");
        cartItem.setPrice(10.0f);
        cartItem.setGender("Unisex");
        cartItem.setSize("M");
        cartItem.setColour("Blue");
        cartItem.setImageLink("http://example.com/image.jpg");

        cartItemRepository.save(cartItem);
    }

    @Test
    void testGetCart() throws Exception {
        mockMvc.perform(get("/cart/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.cartItems").isEmpty());
    }

    @Test
    void testAddToCart() throws Exception {
        mockMvc.perform(post("/cart/add")
                        .param("userId", userId.toString())
                        .param("quantity", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItems", hasKey(cartItem.toString())))
                .andExpect(jsonPath("$.cartItems['" + cartItem.toString() + "']", is(2)))
                .andExpect(jsonPath("$.totalPrice").value(20.0f))
                .andExpect(jsonPath("$.totalNumberOfItems").value(2));
    }

    @Test
    void testSubtractFromCart() throws Exception {
        // Add item first
        // Quantity 3
        mockMvc.perform(post("/cart/add")
                        .param("userId", userId.toString())
                        .param("quantity", "3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItem)))
                .andExpect(status().isOk());

        // Subtract item
        // Subtract 1 from quantity 3
        mockMvc.perform(post("/cart/subtract")
                        .param("userId", userId.toString())
                        .param("quantity", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItem)))
                .andExpect(status().isOk())
                // Expect item exist with 2 quantity
                .andExpect(jsonPath("$.cartItems", hasKey(cartItem.toString())))
                .andExpect(jsonPath("$.cartItems['" + cartItem.toString() + "']", is(2)))
                .andExpect(jsonPath("$.totalNumberOfItems").value(2))
                .andExpect(jsonPath("$.totalPrice").value(20.0f));
    }

    @Test
    void testRemoveFromCart() throws Exception {
        // Add item first
        // Quantity 2
        mockMvc.perform(post("/cart/add")
                        .param("userId", userId.toString())
                        .param("quantity", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItem)))
                .andExpect(status().isOk());

        // Remove item
        // Expect cart to be empty
        mockMvc.perform(post("/cart/remove")
                        .param("userId", userId.toString())
                        .param("productId", cartItem.getProductId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItems").isEmpty())
                .andExpect(jsonPath("$.totalNumberOfItems").value(0))
                .andExpect(jsonPath("$.totalPrice").value(0.0f));
    }

    @Test
    void testClearCart() throws Exception {
        // Add item first
        // Quantity 2
        mockMvc.perform(post("/cart/add")
                        .param("userId", userId.toString())
                        .param("quantity", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItem)))
                .andExpect(status().isOk());

        // Clear cart
        mockMvc.perform(post("/cart/clear")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItems").isEmpty())
                .andExpect(jsonPath("$.totalNumberOfItems").value(0))
                .andExpect(jsonPath("$.totalPrice").value(0.0f));
    }
}
