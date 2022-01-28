package com.example.foodmap.service;

import com.example.foodmap.config.EmbeddedS3Config;
import com.example.foodmap.dto.Restaurant.RestaurantSaveRequestDto;
import com.example.foodmap.dto.mypage.MyReviewDetailResponseDto;
import com.example.foodmap.dto.review.ReviewLikesDto;
import com.example.foodmap.model.*;
import com.example.foodmap.repository.*;
import com.example.foodmap.security.UserDetailsImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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
class MyPageServiceReviewLikeTest {

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

        //리뷰저장
        review3 = new Review(
                1L,
                "너무너무너무 맛있어요!",
                3,
                1,
                "",
                user1,
                restaurant2,
                3,
                new ArrayList<>()
        );

    }
    //endregion


        @Test
        @DisplayName("쓴 리뷰 리스트")
        void 나의리뷰리스트() {
            // given
            reviewLikes1 = new ReviewLikes(1L, user1, review3);

            reviewLikes2 = new ReviewLikes(1L, user2, review3);

            List<ReviewLikesDto> reviewLikesDtoList = new ArrayList<>();

            if (review3.getReviewLikes() != null) {
                List<ReviewLikes> reviewLikesList = review3.getReviewLikes();
                for (ReviewLikes reviewLikes : reviewLikesList) {
                    ReviewLikesDto reviewLikesDto = new ReviewLikesDto(reviewLikes.getUser().getId());
                    reviewLikesDtoList.add(reviewLikesDto);
                    MyReviewDetailResponseDto.builder()
                            .restaurantId(review3.getRestaurant().getId())
                            .reviewId(review3.getId())
                            .userId(review3.getUser().getId())
                            .reviewLikes(review3.getReviewLike())
                            .content(review3.getContent())
                            .location(review3.getRestaurant().getLocation())
                            .nickname(review3.getUser().getNickname())
                            .restaurantName(review3.getRestaurant().getRestaurantName())
                            .createdAt(review3.getCreatedAt())
                            .modifiedAt(review3.getModifiedAt())
                            .image(review3.getImage())
                            .reviewLikesDto(reviewLikesDtoList)
                            .build();
                    reviewLikesDtoList.add(reviewLikesDto);
                }
            myPageService.showMyReview(user1,page,size);
            }
            assertThat(reviewLikesDtoList.size()).isEqualTo(0);
        }

}



