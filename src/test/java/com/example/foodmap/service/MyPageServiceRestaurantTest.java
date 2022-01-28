package com.example.foodmap.service;

import com.example.foodmap.config.EmbeddedS3Config;
import com.example.foodmap.dto.Restaurant.RestaurantReviewResponseDto;
import com.example.foodmap.dto.Restaurant.RestaurantSaveRequestDto;
import com.example.foodmap.dto.mypage.MyRestaurantResponseDto;
import com.example.foodmap.model.*;
import com.example.foodmap.repository.*;
import com.example.foodmap.security.UserDetailsImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Rollback
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@Import(EmbeddedS3Config.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MyPageServiceRestaurantTest {

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ReviewLikesRepository reviewLikesRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    MeetingParticipateRepository meetingParticipateRepository;
    @Autowired
    RestaurantLikesRepository restaurantLikesRepository;
    @Autowired
    MyPageRepository myPageRepository;
    @Autowired
    MyPageService myPageService;
    @Autowired
    RedisService redisService;

    private User user1;
    private User user2;
    private UserDetailsImpl userDetails1;
    private UserDetailsImpl userDetails2;
    private Restaurant restaurant1;
    private Restaurant restaurant2;
    private RestaurantSaveRequestDto restaurantSaveRequestDto;
    private List<Review> review1;
    private List<Review> review2;
    private Review review3;
    private Review review4;
    private Review review5;
    private Review review6;
    private ReviewLikes reviewLikes1;
    private ReviewLikes reviewLikes2;
    private Meeting meeting1;
    private Meeting meeting2;
    private List<RestaurantLikes> restaurantLikes1;
    private List<RestaurantLikes> restaurantLikes2;
    private RestaurantLikes restaurantLikes3;
    private RestaurantLikes restaurantLikes4;
    private RestaurantLikes restaurantLikes5;
    private MeetingParticipate meetingParticipate;
    private Pageable pageable;
    int page = 0;
    int size = 5;

    //region 사전세팅
    @BeforeEach
    void setup() {
        //유저위치 & 유저 저장
        Location location1 = new Location("강서구", 12.23, 34.21);
        user1 = new User(
                "백정수",
                "a111",
                100L,
                "a@b.com",
                UserRoleEnum.USER,
                1L,
                "",
                location1,
                "클린워터"
        );
        userRepository.save(user1);
        userDetails1 = new UserDetailsImpl(user1);

        Location location2 = new Location("강북구", 34.31, 34.21);
        user2 = new User(
                "이한울",
                "a111",
                200L,
                "a@bs.com",
                UserRoleEnum.USER,
                2L,
                "",
                location2,
                "별명이 뭔가요"
        );
        userRepository.save(user2);
        userDetails2 = new UserDetailsImpl(user2);

        restaurant1 = new Restaurant(
                1L,
                user1,
                "식당1",
                location1,
                "포장마차",
                "튀김있음",
                "순대만",
                "밀떡",
                "",
                restaurantLikes1,
                review1,
                3
        );
        restaurantRepository.save(restaurant1);

        restaurant2 = new Restaurant(
                2L,
                user2,
                "식당2",
                location2,
                "포장마차",
                "튀김있음",
                "순대만",
                "밀떡",
                "",
                restaurantLikes2,
                review2,
                2
        );
        restaurantRepository.save(restaurant2);



    }
    //endregion


    //region 내가 작성 한 식당

        @Test
        @DisplayName("내가 작성 한 식당")
        void 작성식당() {
            // given
            review3 = new Review(
                    1L,
                    "너무너무너무 맛있어요!",
                    3,
                    1,
                    "",
                    user1,
                    restaurant1,
                    3,
                    new ArrayList<>()
            );

            PageRequest pageable = PageRequest.of(page, size);

            List<MyRestaurantResponseDto> myRestaurantList = new ArrayList<>();

            //when
            List<Restaurant> restaurantList = restaurantRepository.findAllByUser(user1, pageable);
            for (Restaurant restaurant : restaurantList) {

                List<Review> reviews = restaurant.getReviews();
                List<RestaurantReviewResponseDto> restaurantReviewResponseDtos = new ArrayList<>();

                int spicySum = 0;
                int spicyAvg = 0;
                if (reviews != null) {
                    for (Review review : reviews) {
                        spicySum += review.getSpicy();

                        RestaurantReviewResponseDto responseDto = RestaurantReviewResponseDto.builder()
                                .reviewId(review.getId())
                                .build();

                        restaurantReviewResponseDtos.add(responseDto);
                    }
                    spicyAvg = Math.round(spicySum / restaurantReviewResponseDtos.size()); //맵기 평균값
                }


                MyRestaurantResponseDto myRestaurantResponseDto = MyRestaurantResponseDto.builder()
                        .restaurantId(restaurant.getId())
                        .restaurantName(restaurant.getRestaurantName())
                        .location(restaurant.getLocation())
                        .fried(restaurant.getFried())
                        .sundae(restaurant.getSundae())
                        .tteokbokkiType(restaurant.getTteokbokkiType())
                        .spicy(spicyAvg)
                        .image(restaurant.getImage())
                        .build();
                myRestaurantList.add(myRestaurantResponseDto);
            }

            myPageService.showMyRestaurant(user1,page,size);
            //then
            assertThat(restaurantList.size()).isEqualTo(0);
        }

    //endregion



}



