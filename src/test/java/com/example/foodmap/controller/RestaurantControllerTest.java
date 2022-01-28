package com.example.foodmap.controller;

import com.example.foodmap.MockSpringSecurityFilter;
import com.example.foodmap.dto.Restaurant.RestaurantDetailResponseDto;
import com.example.foodmap.dto.Restaurant.RestaurantResponseDto;
import com.example.foodmap.dto.Restaurant.RestaurantSaveRequestDto;
import com.example.foodmap.model.*;
import com.example.foodmap.security.UserDetailsImpl;
import com.example.foodmap.security.WebSecurityConfig;
import com.example.foodmap.service.RedisService;
import com.example.foodmap.service.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = RestaurantController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
@MockBean(JpaMetamodelMappingContext.class)
class RestaurantControllerTest {

    private MockMvc mvc;

    private Principal mockPrincipal;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private RestaurantService restaurantService;
    @MockBean
    private RedisService redisService;

    UserDetailsImpl testUserDetails;
    Restaurant restaurant1;
    RestaurantDetailResponseDto registedRestaurant;
    RestaurantSaveRequestDto saveRequestDto1;
    List<RestaurantResponseDto> restaurantResponseDtoList;
    RestaurantResponseDto restaurantResponseDto;
    Review review1;
    RestaurantDetailResponseDto restaurantDetailResponseDto;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();

        mockUserSetup();
        saveRequestDto1 = saveSetup();
        Location location = new Location(saveRequestDto1.getAddress(), saveRequestDto1.getLatitude(), saveRequestDto1.getLongitude());


        restaurant1 = Restaurant.builder()
                .id(1L)
                .restaurantName(saveRequestDto1.getRestaurantName())
                .location(location)
                .restaurantType(saveRequestDto1.getRestaurantType())
                .fried(saveRequestDto1.getFried())
                .sundae(saveRequestDto1.getSundae())
                .tteokbokkiType(saveRequestDto1.getTteokbokkiType())
                .image("")
                .build();

        review1 = new Review(10L, "진짜 맛있어요.", 1, 1, "image.png", testUserDetails.getUser(), restaurant1, 0, new ArrayList<>());

        restaurantResponseDtoList = new ArrayList<>();
        restaurantResponseDto = RestaurantResponseDto.builder()
                .restaurantId(1L)
                .restaurantName(restaurant1.getRestaurantName())
                .location(restaurant1.getLocation())
                .fried(restaurant1.getFried())
                .sundae(restaurant1.getSundae())
                .tteokbokkiType(restaurant1.getTteokbokkiType())
                .spicy(review1.getSpicy())
                .reviewCount(0)
                .restaurantLikesCount(0)
                .image("")
                .distance(112.2324)
                .build();

        restaurantResponseDtoList.add(restaurantResponseDto);
    }

    private void mockUserSetup() {
        // Mock 테스트 유져 생성
        User testUser = User.builder()
                .nickname("라이언은귀여워")
                .username("김주란")
                .kakaoId(102L)
                .email("test@naver.com")
                .level(1L)
                .encodedPassword("password")
                .role(UserRoleEnum.USER)
                .profileImage("")
                .build();

        testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    private RestaurantSaveRequestDto saveSetup() {

        String restaurantName = "신전떡볶이";
        double latitude = 126.94830822613552;
        double longitude = 37.49054133559972;
        String address = "서울특별시 관악구 청룡길 38";
        String restaurantType = "포장마차";
        String fried = "true";
        String sundae = "순대만";
        String tteokbokkiType = "밀떡";

        RestaurantSaveRequestDto saveRequestDto = new RestaurantSaveRequestDto(restaurantName, latitude, longitude, address, restaurantType, fried, sundae, tteokbokkiType);
        return saveRequestDto;
    }

    @Test
    @DisplayName("식당 등록 성공")
    public void saveRestaurant() throws Exception{


        RestaurantSaveRequestDto saveRequestDto = saveSetup();

        mvc.perform(post("/restaurants")

                        .principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("restaurantName", saveRequestDto.getRestaurantName())

                )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(handler().handlerType(RestaurantController.class))
                .andExpect(handler().methodName("saveRestaurant"));
    }

    @Test
    @DisplayName("내 근처 식당 리스트 조회")
    void getRestaurants() throws Exception {

        User user1 = testUserDetails.getUser();
        double latitude = user1.getLocation().getLatitude();
        double longitude = user1.getLocation().getLongitude();
        int page = 0;
        int size = 10;

        when(restaurantService.getRestaurants(latitude, longitude, page, size)).thenReturn(restaurantResponseDtoList);

        mvc.perform(get("/restaurants")
                .param("page","0")
                .param("size", "10")
                .principal(mockPrincipal))

                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(handler().handlerType(RestaurantController.class))
                .andExpect(handler().methodName("getRestaurants"))
                .andExpect(jsonPath("$.[0].restaurantId").value(restaurantResponseDto.getRestaurantId()))
                .andExpect(jsonPath("$.[0].restaurantName").value(restaurantResponseDto.getRestaurantName()))
                .andExpect(jsonPath("$.[0].location.address").value(restaurantResponseDto.getLocation().getAddress()))
                .andExpect(jsonPath("$.[0].fried").value(restaurantResponseDto.getFried()))
                .andExpect(jsonPath("$.[0].sundae").value(restaurantResponseDto.getSundae()))
                .andExpect(jsonPath("$.[0].tteokbokkiType").value(restaurantResponseDto.getTteokbokkiType()))
                .andExpect(jsonPath("$.[0].spicy").value(restaurantResponseDto.getSpicy()))
                .andExpect(jsonPath("$.[0].reviewCount").value(restaurantResponseDto.getReviewCount()))
                .andExpect(jsonPath("$.[0].restaurantLikesCount").value(restaurantResponseDto.getRestaurantLikesCount()))
                .andExpect(jsonPath("$.[0].image").value(restaurantResponseDto.getImage()))
                .andExpect(jsonPath("$.[0].distance").value(restaurantResponseDto.getDistance()));

        verify(restaurantService, times(1)).getRestaurants(latitude, longitude, page, size);

    }

    @Test
    @DisplayName("식당 상세페이지 조회")
    void getDetailRestaurantInfo() throws Exception {

        restaurantDetailResponseDto = RestaurantDetailResponseDto.builder()
                .restaurantId(restaurant1.getId())
                .restaurantName(restaurant1.getRestaurantName())
                .restaurantType(restaurant1.getRestaurantType())
                .location(restaurant1.getLocation())
                .distance(112.2324)
                .fried(restaurant1.getFried())
                .sundae(restaurant1.getSundae())
                .tteokbokkiType(restaurant1.getTteokbokkiType())
                .image(restaurant1.getImage())
                .spicy(review1.getSpicy())
                .restaurantTagSum(1)
                .build();


        when(restaurantService.getRestaurantDetail(restaurant1.getId(), testUserDetails.getUser())).thenReturn(restaurantDetailResponseDto);


        mvc.perform(get("/restaurants/{restaurantId}",restaurant1.getId())
                        .principal(mockPrincipal))

                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(handler().handlerType(RestaurantController.class))
                .andExpect(handler().methodName("getInfo"))
                .andExpect(jsonPath("$.restaurantId").value(restaurantDetailResponseDto.getRestaurantId()))
                .andExpect(jsonPath("$.restaurantName").value(restaurantDetailResponseDto.getRestaurantName()))
                .andExpect(jsonPath("$.location.address").value(restaurantDetailResponseDto.getLocation().getAddress()))
                .andExpect(jsonPath("$.distance").value(restaurantDetailResponseDto.getDistance()))
                .andExpect(jsonPath("$.fried").value(restaurantDetailResponseDto.getFried()))
                .andExpect(jsonPath("$.sundae").value(restaurantDetailResponseDto.getSundae()))
                .andExpect(jsonPath("$.tteokbokkiType").value(restaurantDetailResponseDto.getTteokbokkiType()))
                .andExpect(jsonPath("$.image").value(restaurantDetailResponseDto.getImage()))
                .andExpect(jsonPath("$.spicy").value(restaurantDetailResponseDto.getSpicy()))
                .andExpect(jsonPath("$.restaurantTagSum").value(restaurantDetailResponseDto.getRestaurantTagSum()))
                .andExpect(jsonPath("$.restaurantType").value(restaurantDetailResponseDto.getRestaurantType()));

        verify(restaurantService, times(1)).getRestaurantDetail(restaurant1.getId(), testUserDetails.getUser());

    }

    @Test
    @DisplayName("로그인하지 않은 사용자(둘러보기)")
    void get_Restaurants_not_user() throws Exception {

        double lat = 126.97260868381068;
        double lon = 37.559187621837744;
        int page = 0;
        int size = 10;

        when(restaurantService.getRestaurants(lat, lon, page, size)).thenReturn(restaurantResponseDtoList);

        mvc.perform(get("/home")
                        .param("page","0")
                        .param("size", "10"))

                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(handler().methodName("getRestaurantsInHome"))
                .andExpect(jsonPath("$.[0].restaurantId").value(restaurantResponseDto.getRestaurantId()))
                .andExpect(jsonPath("$.[0].restaurantName").value(restaurantResponseDto.getRestaurantName()))
                .andExpect(jsonPath("$.[0].location.address").value(restaurantResponseDto.getLocation().getAddress()))
                .andExpect(jsonPath("$.[0].fried").value(restaurantResponseDto.getFried()))
                .andExpect(jsonPath("$.[0].sundae").value(restaurantResponseDto.getSundae()))
                .andExpect(jsonPath("$.[0].tteokbokkiType").value(restaurantResponseDto.getTteokbokkiType()))
                .andExpect(jsonPath("$.[0].spicy").value(restaurantResponseDto.getSpicy()))
                .andExpect(jsonPath("$.[0].reviewCount").value(restaurantResponseDto.getReviewCount()))
                .andExpect(jsonPath("$.[0].restaurantLikesCount").value(restaurantResponseDto.getRestaurantLikesCount()))
                .andExpect(jsonPath("$.[0].image").value(restaurantResponseDto.getImage()))
                .andExpect(jsonPath("$.[0].distance").value(restaurantResponseDto.getDistance()));

        verify(restaurantService, times(1)).getRestaurants(lat, lon, page, size);

    }


    @Test
    @DisplayName("좌표 움직일 때 주변 식당조회")
    void getRestaurantsInHome() throws Exception {

        double lat = 126.97260868381068;
        double lon = 37.559187621837744;
        int page = 0;
        int size = 10;

        when(restaurantService.getRestaurants(lat, lon, page, size)).thenReturn(restaurantResponseDtoList);

        mvc.perform(get("/home")
                        .param("lat", "126.97260868381068")
                        .param("lon", "37.559187621837744")
                        .param("page","0")
                        .param("size", "10"))

                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(handler().methodName("getRestaurantsInHome"))
                .andExpect(jsonPath("$.[0].restaurantId").value(restaurantResponseDto.getRestaurantId()))
                .andExpect(jsonPath("$.[0].restaurantName").value(restaurantResponseDto.getRestaurantName()))
                .andExpect(jsonPath("$.[0].location.address").value(restaurantResponseDto.getLocation().getAddress()))
                .andExpect(jsonPath("$.[0].fried").value(restaurantResponseDto.getFried()))
                .andExpect(jsonPath("$.[0].sundae").value(restaurantResponseDto.getSundae()))
                .andExpect(jsonPath("$.[0].tteokbokkiType").value(restaurantResponseDto.getTteokbokkiType()))
                .andExpect(jsonPath("$.[0].spicy").value(restaurantResponseDto.getSpicy()))
                .andExpect(jsonPath("$.[0].reviewCount").value(restaurantResponseDto.getReviewCount()))
                .andExpect(jsonPath("$.[0].restaurantLikesCount").value(restaurantResponseDto.getRestaurantLikesCount()))
                .andExpect(jsonPath("$.[0].image").value(restaurantResponseDto.getImage()))
                .andExpect(jsonPath("$.[0].distance").value(restaurantResponseDto.getDistance()));

        verify(restaurantService, times(1)).getRestaurants(lat, lon, page, size);

    }
}