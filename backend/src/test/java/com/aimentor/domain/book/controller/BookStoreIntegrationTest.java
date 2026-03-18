package com.aimentor.domain.book.controller;

import com.aimentor.common.security.jwt.JwtTokenProvider;
import com.aimentor.domain.book.book.entity.Book;
import com.aimentor.domain.book.book.entity.BookCategory;
import com.aimentor.domain.book.book.repository.BookRepository;
import com.aimentor.domain.book.order.repository.OrderItemRepository;
import com.aimentor.domain.book.order.repository.OrderRepository;
import com.aimentor.domain.user.entity.Role;
import com.aimentor.domain.user.entity.User;
import com.aimentor.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "jwt.secret-key=test-secret-key-test-secret-key-test-secret-key",
        "jwt.access-token-expiration-seconds=1800",
        "jwt.refresh-token-expiration-seconds=1209600"
})
@AutoConfigureMockMvc
class BookStoreIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    private Long interviewBookId;
    private Long englishBookId;

    @BeforeEach
    void setUp() {
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        bookRepository.deleteAll();

        interviewBookId = bookRepository.save(Book.builder()
                .title("면접 합격 전략")
                .author("홍길동")
                .publisher("에이멘토")
                .price(20000)
                .stock(10)
                .description("면접 대비 전략을 정리한 책")
                .imageUrl("https://example.com/interview.jpg")
                .category(BookCategory.INTERVIEW)
                .build()).getId();

        englishBookId = bookRepository.save(Book.builder()
                .title("실전 영어 회화")
                .author("Jane Doe")
                .publisher("에이멘토")
                .price(18000)
                .stock(5)
                .description("영어 회화 훈련 교재")
                .imageUrl("https://example.com/english.jpg")
                .category(BookCategory.ENGLISH)
                .build()).getId();
    }

    @Test
    void bookstoreFlowShouldWorkForAuthenticatedUser() throws Exception {
        String userAccessToken = signupAndGetAccessToken("book-user@example.com");
        String otherUserAccessToken = signupAndGetAccessToken("book-other@example.com");

        mockMvc.perform(get("/api/books")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(userAccessToken))
                        .param("category", "INTERVIEW")
                        .param("page", "0")
                        .param("size", "12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].title").value("면접 합격 전략"));

        mockMvc.perform(get("/api/books/{id}", interviewBookId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(userAccessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.category").value("INTERVIEW"));

        mockMvc.perform(get("/api/books/search")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(userAccessToken))
                        .param("q", "면접"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].id").value(interviewBookId));

        MvcResult addCartResult = mockMvc.perform(post("/api/cart/items")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(userAccessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "bookId": %d,
                                  "quantity": 2
                                }
                                """.formatted(interviewBookId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items.length()").value(1))
                .andExpect(jsonPath("$.data.totalPrice").value(40000))
                .andReturn();

        Long cartItemId = readFirstItemId(addCartResult);

        mockMvc.perform(patch("/api/cart/items/{id}", cartItemId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(userAccessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "quantity": 3
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalPrice").value(60000))
                .andExpect(jsonPath("$.data.items[0].quantity").value(3));

        MvcResult orderResult = mockMvc.perform(post("/api/orders")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(userAccessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalPrice").value(60000))
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andExpect(jsonPath("$.data.items.length()").value(1))
                .andReturn();

        Long orderId = readId(orderResult);

        mockMvc.perform(get("/api/cart")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(userAccessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items.length()").value(0))
                .andExpect(jsonPath("$.data.totalPrice").value(0));

        mockMvc.perform(get("/api/orders/my")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(userAccessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].id").value(orderId));

        mockMvc.perform(get("/api/orders/{id}", orderId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(userAccessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(orderId));

        mockMvc.perform(get("/api/orders/{id}", orderId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(otherUserAccessToken)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.code").value("ORDER_NOT_FOUND"));

        assertThat(bookRepository.findById(interviewBookId).orElseThrow().getStock()).isEqualTo(7);
    }

    @Test
    void adminBookCreateShouldRequireAdminRole() throws Exception {
        String userAccessToken = signupAndGetAccessToken("normal-user@example.com");
        String adminAccessToken = createAdminAccessToken("admin@example.com");

        mockMvc.perform(post("/api/admin/books")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(userAccessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookCreateRequestJson("관리 실패", "OTHER")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error.code").value("BOOK_ADMIN_FORBIDDEN"));

        mockMvc.perform(post("/api/admin/books")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(adminAccessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookCreateRequestJson("관리자 등록 도서", "SELF_DEVELOPMENT")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("관리자 등록 도서"))
                .andExpect(jsonPath("$.data.category").value("SELF_DEVELOPMENT"));
    }

    @Test
    void cartDeleteShouldRemoveOwnedItem() throws Exception {
        String userAccessToken = signupAndGetAccessToken("cart-delete@example.com");

        MvcResult addCartResult = mockMvc.perform(post("/api/cart/items")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(userAccessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "bookId": %d,
                                  "quantity": 1
                                }
                                """.formatted(englishBookId)))
                .andExpect(status().isOk())
                .andReturn();

        Long cartItemId = readFirstItemId(addCartResult);

        mockMvc.perform(delete("/api/cart/items/{id}", cartItemId)
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(userAccessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        mockMvc.perform(get("/api/cart")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(userAccessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items.length()").value(0));
    }

    private String signupAndGetAccessToken(String email) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Book User",
                                  "email": "%s",
                                  "password": "password1"
                                }
                                """.formatted(email)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        String accessToken = response.path("data").path("accessToken").asText();
        assertThat(accessToken).isNotBlank();
        return accessToken;
    }

    private String createAdminAccessToken(String email) {
        User adminUser = userRepository.save(User.builder()
                .email(email)
                .name("Admin")
                .password("encoded-password")
                .role(Role.ADMIN)
                .build());
        return jwtTokenProvider.createAccessToken(adminUser.getId(), adminUser.getEmail(), adminUser.getRole());
    }

    private String bookCreateRequestJson(String title, String category) {
        return """
                {
                  "title": "%s",
                  "author": "관리자",
                  "publisher": "에이멘토",
                  "price": 22000,
                  "stock": 8,
                  "description": "신규 등록 도서",
                  "imageUrl": "https://example.com/new-book.jpg",
                  "category": "%s"
                }
                """.formatted(title, category);
    }

    private Long readId(MvcResult result) throws Exception {
        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        return response.path("data").path("id").asLong();
    }

    private Long readFirstItemId(MvcResult result) throws Exception {
        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        return response.path("data").path("items").get(0).path("id").asLong();
    }

    private String bearerToken(String accessToken) {
        return "Bearer " + accessToken;
    }
}
