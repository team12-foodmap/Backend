//package com.example.foodmap.service;
//
//import com.example.foodmap.config.EmbeddedS3Config;
//import com.example.foodmap.dto.Restaurant.RestaurantDetailResponseDto;
//import com.example.foodmap.dto.Restaurant.RestaurantResponseDto;
//import com.example.foodmap.dto.Restaurant.RestaurantSaveRequestDto;
//import com.example.foodmap.dto.review.ReviewRequestDto;
//import com.example.foodmap.exception.CustomException;
//import com.example.foodmap.exception.ErrorCode;
//import com.example.foodmap.model.*;
//import com.example.foodmap.repository.RestaurantLikesRepository;
//import com.example.foodmap.repository.RestaurantRepository;
//import com.example.foodmap.repository.ReviewRepository;
//import com.example.foodmap.repository.UserRepository;
//import com.example.foodmap.security.UserDetailsImpl;
//import io.findify.s3mock.S3Mock;
//import jdk.swing.interop.SwingInterOpUtils;
//import org.aspectj.lang.annotation.After;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@Transactional
//@Rollback(value = true)
//@ActiveProfiles("test")
//@Import(EmbeddedS3Config.class)
//class RestaurantServiceTest {
//
//    @Autowired
//    RestaurantService restaurantService;
//
//    @Autowired
//    RestaurantLikesService restaurantLikesService;
//
//    @Autowired
//    RestaurantRepository restaurantRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    RestaurantLikesRepository restaurantLikesRepository;
//
//    @Autowired
//    ReviewRepository reviewRepository;
//
//    @Autowired
//    S3Mock s3Mock;
//
//    @Autowired
//    StorageService storageService;
//
//
//    private User user1;
//    private Location userLocation;
//    private UserDetailsImpl userDetails01;
//    private RestaurantSaveRequestDto restaurantSaveRequestDto1;
//    private RestaurantSaveRequestDto restaurantSaveRequestDto2;
//    private RestaurantSaveRequestDto restaurantSaveRequestDto3;
//    private Restaurant restaurant1;
//    private RestaurantLikes restaurantLikes1;
//    private ReviewRequestDto reviewRequestDto;
//    private Review review1;
//    private String imagePath;
//    public static final String CLOUD_FRONT_DOMAIN_NAME = "https://djefjounyx85c.cloudfront.net";
//
//    @BeforeEach
//    void setup() {
//
//        user1 = User.builder()
//                .nickname("라이언은귀여워")
//                .username("김주란")
//                .kakaoId(102L)
//                .email("test@naver.com")
//                .level(1L)
//                .encodedPassword("password")
//                .role(UserRoleEnum.USER)
//                .profileImage("default.png")
//                .build();
//
//        userDetails01 = new UserDetailsImpl(user1);
//        userRepository.save(user1);
//
//        restaurantSaveRequestDto1 = RestaurantSaveRequestDto.builder()
//                .restaurantName("써니즉석떡볶이")
//              .latitude(126.97413427217985)
//                .longitude( 37.56047008208804)
//                .address("서울특별시 관악구 청룡길 38")
//                .restaurantType("가게")
//                .fried("튀김있음")
//                .sundae("순대만")
//                .tteokbokkiType("밀떡")
//                .build();
//
//
//        restaurantSaveRequestDto2 = RestaurantSaveRequestDto.builder()
//                .restaurantName("간판없는 떡볶이")
//                .latitude(126.97688085411811)
//                .longitude(37.55414219765539)
//                .address("봉래동1가 104-1번지 2층 중구 서울특별시 KR")
//                .restaurantType("가게")
//                .fried("튀김있음")
//                .sundae("순대만")
//                .tteokbokkiType("밀떡")
//                .build();
//
//
//        restaurantSaveRequestDto3 = RestaurantSaveRequestDto.builder()
//                .restaurantName("까만콩떡볶이")
//                .latitude(126.97773916097381)
//                .longitude(37.56428016188523)
//                .address("후암동 56-22번지 지상1층 용산구 서울특별시 KR")
//                .restaurantType("가게")
//                .fried("튀김있음")
//                .sundae("순대만")
//                .tteokbokkiType("밀떡")
//                .build();
//
//        imagePath = "restaurant/2771ea67-c747-4529-b91f-9dcea5a2ac94_스프링르탄이.png";
//    }
//
//
//    @Nested
//    @DisplayName("식당 등록")
//    class RegisterRestaurant{
//        @Test
//        @DisplayName("식당등록_성공")
//        void saveRestaurant() throws IOException {
//            //given
//            userRepository.save(user1);
//
//            String expected = "mock1.png";
//            MockMultipartFile mockMultipartFile = new MockMultipartFile("file", expected,
//                    "image/png", "test data".getBytes());
//
//
//            //when
//            Long id1 = restaurantService.saveRestaurant(restaurantSaveRequestDto1, user1, mockMultipartFile);
//            Optional<Restaurant> restaurant = restaurantRepository.findById(id1);
//            //then
//            assertThat(id1).isEqualTo(restaurant.get().getId());
//            assertThat(restaurant.get().getRestaurantName()).isEqualTo(restaurantSaveRequestDto1.getRestaurantName());
//            assertThat(restaurant.get().getRestaurantType()).isEqualTo(restaurantSaveRequestDto1.getRestaurantType());
//            assertThat(restaurant.get().getFried()).isEqualTo(restaurantSaveRequestDto1.getFried());
//            assertThat(restaurant.get().getSundae()).isEqualTo(restaurantSaveRequestDto1.getSundae());
//            assertThat(restaurant.get().getTteokbokkiType()).isEqualTo(restaurantSaveRequestDto1.getTteokbokkiType());
//            assertThat(restaurant.get().getLocation().getAddress()).isEqualTo(restaurantSaveRequestDto1.getAddress());
//
//
//        }
//
//        @Test
//        @DisplayName("식당등록_실패_로그인하지 않은 사용자")
//        void saveFail1() {
//            //given
//
//            String expected = "mock1.png";
//            MockMultipartFile mockMultipartFile = new MockMultipartFile("file", expected,
//                    "image/png", "test data".getBytes());
//
//            Location location = new Location("서울특별시 용산구", 126.97234632175427, 37.55612530289547);
//
//            User user = new User(111L, "라이언", "password", "test@naver.com", 1L, UserRoleEnum.USER, 100L, "test.png",expected, location);
//
//            //when
//            CustomException exception = Assertions.assertThrows(CustomException.class, () -> {
//                restaurantService.saveRestaurant(restaurantSaveRequestDto1, user, mockMultipartFile);
//            });
//
//            //then
//            assertThat(exception.getErrorCode().getDetail()).isEqualTo(ErrorCode.USER_NOT_FOUND.getDetail());
//
//        }
//
//    }
//
//
//    @Nested
//    @DisplayName("식당 상세 조회")
//    class getDetailInfo{
//        @Test
//        @DisplayName("식당 상세 조회_성공")
//        void getDetailInfo() {
//            //given
//
//            restaurant1 = new Restaurant(restaurantSaveRequestDto1, imagePath, user1);
//
//            Restaurant save1 = restaurantRepository.save(restaurant1);
//
//            // 댓글
//            reviewRequestDto= ReviewRequestDto.builder()
//                    .content("진짜 맛집이에요.")
//                    .spicy("1")
//                    .restaurantTags("2")
//                    .build();
//
//            List<Review> reviews = new ArrayList<>();
//            review1 = new Review(reviewRequestDto, user1, restaurant1, imagePath);
//            reviewRepository.save(review1);
//
//            reviews.add(review1);
//
//            //when
//            RestaurantDetailResponseDto detailInfo = restaurantService.getRestaurantDetail(save1.getId(), user1);
//            //then
//            assertThat(detailInfo.getRestaurantId()).isEqualTo(restaurant1.getId());
//            assertThat(detailInfo.getRestaurantName()).isEqualTo(restaurant1.getRestaurantName());
//            assertThat(detailInfo.getLocation()).isEqualTo(restaurant1.getLocation());
//            assertThat(detailInfo.getRestaurantType()).isEqualTo(restaurant1.getRestaurantType());
//            assertThat(detailInfo.getFried()).isEqualTo(restaurant1.getFried());
//            assertThat(detailInfo.getSundae()).isEqualTo(restaurant1.getSundae());
//            assertThat(detailInfo.getTteokbokkiType()).isEqualTo(restaurant1.getTteokbokkiType());
//            assertThat(detailInfo.getSpicy()).isEqualTo(1);
//            assertThat(detailInfo.getRestaurantTagSum()).isEqualTo(reviews.size());
//            assertThat(detailInfo.getRestaurantTags().size()).isEqualTo(4);
//            assertThat(detailInfo.getRestaurantReviews().size()).isEqualTo(reviews.size());
//
//        }
//
//    }
//
//    @Nested
//    @DisplayName("내 주변 식당 조회")
//    class getList {
//        @Test
//        @DisplayName("식당 조회_성공")
//        void getList() {
//
//            //given
//            Restaurant r1 = new Restaurant(restaurantSaveRequestDto1, imagePath, user1);
//            Restaurant r2 = new Restaurant(restaurantSaveRequestDto2, imagePath, user1);
//            Restaurant r3 = new Restaurant(restaurantSaveRequestDto3, imagePath, user1);
//
//            Restaurant save1 = restaurantRepository.save(r1);
//            Restaurant save2 = restaurantRepository.save(r2);
//            Restaurant save3 = restaurantRepository.save(r3);
//
//            // 댓글
//            reviewRequestDto= ReviewRequestDto.builder()
//                    .content("진짜 맛집이에요.")
//                    .spicy("1")
//                    .restaurantTags("2")
//                    .build();
//
//            List<Review> reviews = new ArrayList<>();
//            Review v1 = new Review(reviewRequestDto, user1, save1, imagePath);
//            Review v2 = new Review(reviewRequestDto, user1, save2, imagePath);
//            Review v3 = new Review(reviewRequestDto, user1, save3, imagePath);
//
//            reviewRepository.save(v1);
//            reviewRepository.save(v2);
//            reviewRepository.save(v3);
//
//            //when
//
//            List<RestaurantResponseDto> restaurants =
//                    restaurantService.getRestaurants(user1.getLocation().getLatitude(), user1.getLocation().getLongitude(), 0, 3);
//            //then
//
//            assertThat(restaurants.size()).isEqualTo(3);
//            assertThat(restaurants.get(2).getDistance()).isGreaterThan(restaurants.get(1).getDistance());
//            assertThat(restaurants.get(1).getDistance()).isGreaterThan(restaurants.get(0).getDistance());
//
//        }
//
//    }
//
//    @After("")
//    public void shutdownMockS3(){
//        s3Mock.stop();
//    }
//
//}