//package com.example.foodmap.controller;
//
//import com.example.foodmap.MockSpringSecurityFilter;
//import com.example.foodmap.dto.Restaurant.RestaurantDetailResponseDto;
//import com.example.foodmap.dto.Restaurant.RestaurantResponseDto;
//import com.example.foodmap.dto.Restaurant.RestaurantSaveRequestDto;
//import com.example.foodmap.model.Location;
//import com.example.foodmap.model.Restaurant;
//import com.example.foodmap.model.User;
//import com.example.foodmap.model.UserRoleEnum;
//import com.example.foodmap.security.UserDetailsImpl;
//import com.example.foodmap.security.WebSecurityConfig;
//import com.example.foodmap.service.RestaurantService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.awt.*;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.security.Principal;
//import java.util.Collections;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(
//        controllers = RestaurantController.class,
//        excludeFilters = {
//                @ComponentScan.Filter(
//                        type = FilterType.ASSIGNABLE_TYPE,
//                        classes = WebSecurityConfig.class
//                )
//        }
//)
//@MockBean(JpaMetamodelMappingContext.class)
//class RestaurantControllerTest {
//
//    private MockMvc mvc;
//
//    private Principal mockPrincipal;
//
//    private HttpHeaders headers;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private WebApplicationContext context;
//
//    @MockBean
//    private RestaurantService restaurantService;
//
//    private UserDetailsImpl testUserDetails;
//    private Restaurant restaurant;
//    private RestaurantDetailResponseDto registedRestaurant;
//
//    @BeforeEach
//    public void setup() {
//        mvc = MockMvcBuilders.webAppContextSetup(context)
//                .apply(springSecurity(new MockSpringSecurityFilter()))
//                .build();
//
//        headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        Location userLocation = Location.builder()
//                .address("전북 익산시 부송동 100")
//                .latitude(127.1163593869371)
//                .longitude(127.1163593869371)
//                .build();
//
//        User user1 = User.builder()
//                .id(1L)
//                .username("김주란")
//                .kakaoId(1L)
//                .email("test@naver.com")
//                .level(1L)
//                .password("password")
//                .role(UserRoleEnum.USER)
//                .location(userLocation)
//                .build();
//
//        UserDetailsImpl testUserDetails = new UserDetailsImpl(user1);
//        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", Collections.emptyList());
//
//
//    }
//
//    @Test
//    @Order(1)
//    @DisplayName("식당 등록성공-사진 입력 안했을 경우")
//    public void saveRestaurant() throws Exception{
////        File file1 = new File("C:/Users/ghdck/Desktop");
////        InputStream f1 = new FileInputStream(file1);
////        MockMultipartFile file
////                = new MockMultipartFile(
////                "image",
////                "hello.png",
////                "image/png",
////                f1
////        );
//
//        RestaurantSaveRequestDto saveRequestDto = RestaurantSaveRequestDto.builder()
//                .restaurantName("신전떡볶이")
//                .latitude(37.49054133559972)
//                .longitude(126.94830822613552)
//                .address("서울특별시 관악구 청룡길 38")
//                .restaurantType("가게")
//                .fried(true)
//                .sundae("순대만")
//                .tteokbokkiType("밀떡")
//                .image(null)
//                .build();
//
//        String saveInfo = objectMapper.writeValueAsString(saveRequestDto);
//
//
//        mvc.perform(post("/restaurants")
//                .content("식당 등록 성공")
//                .contentType(MediaType.APPLICATION_JSON)
//                .characterEncoding("utf-8")
//                .principal(mockPrincipal)
//        )
//                .andExpect(status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//
//    }
//
////    @Test
////    @DisplayName("식당 등록성공-사진 입력 ")
////    public void registerImage() throws Exception{
////        File file1 = new File("C:/Users/ghdck/Desktop");
////        InputStream f1 = new FileInputStream(file1);
////        MockMultipartFile file
////                = new MockMultipartFile(
////                "image",
////                "가벼운식탁.png",
////                "image/png",
////                f1
////        );
////
////        RestaurantSaveRequestDto saveRequestDto = RestaurantSaveRequestDto.builder()
////                .restaurantName("신전떡볶이")
////                .latitude(37.49054133559972)
////                .longitude(126.94830822613552)
////                .address("서울특별시 관악구 청룡길 38")
////                .restaurantType("가게")
////                .fried(true)
////                .sundae("순대만")
////                .tteokbokkiType("밀떡")
////                .image(file)
////                .build();
////
////        String saveInfo = objectMapper.writeValueAsString(saveRequestDto);
////
////
////        mvc.perform(post("/restaurants")
////                        .content("식당 등록 성공")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .characterEncoding("utf-8")
////                        .principal(mockPrincipal)
////                )
////                .andExpect(status().isOk())
////                .andDo(MockMvcResultHandlers.print());
////    }
//
//
//}