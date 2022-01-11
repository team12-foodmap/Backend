//package com.example.foodmap;
//
//
//import com.example.foodmap.repository.RestaurantRepository;
//import com.example.foodmap.repository.ReviewRepository;
//import com.example.foodmap.repository.UserRepository;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class RestaurantIntegrationTest {
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    private HttpHeaders headers;
//    private ObjectMapper mapper = new ObjectMapper();
//
//    @Autowired
//    RestaurantRepository restaurantRepository;
//
//    @Autowired
//    ReviewRepository reviewRepository;
//
//    @Autowired
//    FileUploaderService fileUploaderService;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @BeforeEach
//    public void setup() {
//        headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//    }
//
//    @BeforeEach
//    public void userSetup() {
//
//    }
//
//    @Test
//    @DisplayName("식당 등록")
//    void save() throws JsonProcessingException {
//        //given
//        //when
//        //then
//    }
//
//
//}
