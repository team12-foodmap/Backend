/*
package com.example.foodmap.service;

import com.example.foodmap.dto.Restaurant.RestaurantSaveRequestDto;
import com.example.foodmap.dto.meeting.MeetingCreatRequestDto;
import com.example.foodmap.dto.mypage.MyReviewResponseDto;
import com.example.foodmap.dto.review.ReviewRequestDto;
import com.example.foodmap.model.*;
import com.example.foodmap.repository.*;
import com.example.foodmap.security.UserDetailsImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MyPageServiceTest {

    @Autowired
    ReviewRepository reviewRepository;
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

    private User user1;
    private User user2;
    private UserDetailsImpl userDetails1;
    private UserDetailsImpl userDetails2;
    private Restaurant restaurant1;
    private Restaurant restaurant2;
    private Review review1;
    private Review review2;
    private Meeting meeting1;
    private Meeting meeting2;
    private RestaurantLikes restaurantLikes1;
    private RestaurantLikes restaurantLikes2;
    private RestaurantLikes restaurantLikes3;
    private MeetingParticipate meetingParticipate;
    private Pageable pageable;

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
                null,
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
                null,
                location2,
                "별명이 뭔가요"
        );
        userRepository.save(user2);
        userDetails2 = new UserDetailsImpl(user2);

        RestaurantSaveRequestDto restaurantSaveRequestDto2 = new RestaurantSaveRequestDto(
                "안돼",
                22.33,
                33.44,
                "강동구",
                "분식",
                "판매안함",
                "허파만",
                "밀떡"
        );
        restaurant1 = new Restaurant(restaurantSaveRequestDto2, null, user1);
        restaurantRepository.save(restaurant1);
        restaurantLikes1 = new RestaurantLikes(1L, user1, restaurant1);
        restaurantLikesRepository.save(restaurantLikes1);
        RestaurantSaveRequestDto restaurantSaveRequestDto1 = new RestaurantSaveRequestDto(
                "안돼",
                22.33,
                33.44,
                "강동구",
                "분식",
                "판매안함",
                "허파만",
                "밀떡"
        );
        restaurant2 = new Restaurant(restaurantSaveRequestDto1, null, user1);
        restaurantRepository.save(restaurant2);
        restaurantLikes2 = new RestaurantLikes(2L, user1, restaurant2);
        restaurantLikesRepository.save(restaurantLikes2);

//        //식당 저장
//        RestaurantSaveRequestDto restaurantSaveRequestDto1 = new RestaurantSaveRequestDto(
//                "엽떡",
//                22.33,
//                33.44,
//                "서울특별시 강동구",
//                "분식",
//                "판매",
//                "허파만",
//                "밀떡"
//        );
//        restaurant1 = new Restaurant(restaurantSaveRequestDto1, null, user1, location1);
//        restaurantRepository.save(restaurant1);
//
//        RestaurantSaveRequestDto restaurantSaveRequestDto2 = new RestaurantSaveRequestDto(
//                "안돼",
//                22.33,
//                33.44,
//                "강동구",
//                "분식",
//                "판매안함",
//                "허파만",
//                "밀떡"
//        );
//        restaurant2 = new Restaurant(restaurantSaveRequestDto2, null, user1, location1);

        //식당 찜하기저장

//        restaurantLikes3 = new RestaurantLikes(restaurantLikes3.getId(),user1, restaurant2);
//        restaurantLikesRepository.save(restaurantLikes3);

        //리뷰저장
        ReviewRequestDto reviewRequestDto1 = new ReviewRequestDto(
                "이집 매우매우 잘하네요.",
                "1",
                "2"
        );
        review1 = new Review(reviewRequestDto1, user1, restaurant1, null);
        reviewRepository.save(review1);

        ReviewRequestDto reviewRequestDto2 = new ReviewRequestDto(
                "이집 매우매우 별로네요.",
                "5",
                "3"
        );
        review2 = new Review(reviewRequestDto2, user1, restaurant2, "image1");
        reviewRepository.save(review2);

        //모임저장
        MeetingCreatRequestDto meetingCreatRequestDto1 = new MeetingCreatRequestDto(
                "떡볶이 같이 드실 분",
                LocalDateTime.of(2022, 1, 1, 12, 00),
                "신전떡볶이",
                restaurant1.getId(),
                LocalDateTime.of(2022, 1, 10, 12, 00),
                LocalDateTime.of(2022, 1, 11, 12, 00),
                "서울특별시 강남역",
                5,
                1,
                "엽떡 맛있게 드실분 구합니다."
        );
        meeting1 = new Meeting(user1, meetingCreatRequestDto1);
        meetingRepository.save(meeting1);

        MeetingCreatRequestDto meetingCreatRequestDto2 = new MeetingCreatRequestDto(
                "튀김 같이 드실 분",
                LocalDateTime.of(2022, 1, 10, 12, 00),
                "엽튀",
                restaurant2.getId(),
                LocalDateTime.of(2022, 1, 20, 12, 00),
                LocalDateTime.of(2022, 1, 31, 12, 00),
                "경상북도 구미시",
                20,
                1,
                "튀김 맛있게 드실분 구합니다."
        );
        meeting2 = new Meeting(user2, meetingCreatRequestDto2);
        meetingRepository.save(meeting2);

        //모임참가 저장
        meetingParticipate = new MeetingParticipate(meeting1, userDetails1);
        meetingParticipateRepository.save(meetingParticipate);
        meetingParticipate = new MeetingParticipate(meeting2, userDetails1);
        meetingParticipateRepository.save(meetingParticipate);

    }
    //endregion

    //region 성공케이스
    @Nested
    @DisplayName("성공케이스")
    class success {

        //region 리뷰 리스트
        @Test
        @DisplayName("리뷰 리스트")
        void 리뷰리스트() {
            // given
            Location location1 = new Location("강서구", 12.23, 34.21);
            user1 = new User(
                    "백정수",
                    "a111",
                    6L,
                    "a@b.com",
                    UserRoleEnum.USER,
                    1L,
                    null,
                    location1,
                    "클린워터"
            );
            userRepository.save(user1);
            userDetails1 = new UserDetailsImpl(user1);

            List<MyReviewResponseDto> myReviewList = new ArrayList<>();

            ReviewRequestDto reviewRequestDto1 = new ReviewRequestDto(
                    "이집 매우매우매우잘하네요.",
                    "1",
                    "2"
            );
            review1 = new Review(reviewRequestDto1, user1, restaurant1, "image1");
            reviewRepository.save(review1);

            ReviewRequestDto reviewRequestDto2 = new ReviewRequestDto(
                    "이집 매우매우매우 별로네요.",
                    "5",
                    "3"
            );
            review2 = new Review(reviewRequestDto2, user1, restaurant1, "image1");
            reviewRepository.save(review2);

            List<Review> reviewList = reviewRepository.findAllByUser(user1,pageable);
            for (Review review : reviewList) {
                MyReviewResponseDto myReviewResponseDto = MyReviewResponseDto.builder()
                        .reviewId(review.getId())
                        .userId(review.getUser().getId())
                        .restaurantId(review.getRestaurant().getId())
                        .image(review.getImage())
                        .build();
                myReviewList.add(myReviewResponseDto);

            }
            assertThat(myReviewList.size()).isEqualTo(2);
        }
        //endregion

        //region 좋아요리스트
        @Test
        @DisplayName("좋아요 리스트")
        void 좋아요리스트() {
            // given

            //when
            List<RestaurantLikes> restaurantLikesList = restaurantLikesRepository.findAllByUser(user1,pageable);

            //then
            assertThat(restaurantLikesList.size()).isEqualTo(2);

        }
        //endregion

        //region 내가 작성 한 식당
        @Test
        @DisplayName("내가 작성 한 식당")
        void 작성식당() {
            // given

            //when
            List<Restaurant> restaurantList = restaurantRepository.findAllByUser(user1,pageable);

            //then
            assertThat(restaurantList.size()).isEqualTo(2);
        }
        //endregion
    }

    //region 내가 참가 한 모임
    @Test
    @DisplayName("내가 참가 한 모임")
    void 참가모임() {
        // given

        //when
        List<MeetingParticipate> meetingList = meetingParticipateRepository.findAllByUser(user1,pageable);

        //then
        assertThat(meetingList.size()).isEqualTo(2);
    }
    //endregion

    //region 내 리뷰 조회
    @Test
    @DisplayName("내 리뷰 조회")
    void 리뷰조회() {
        // given

        //when
        Review review = reviewRepository.findAllByUserAndId(user1, review1.getId());
        //then
        assertEquals(review1.getContent(),review.getContent());
        assertEquals(review1.getReviewLike(),review.getReviewLike());
        assertEquals(review1.getImage(),review.getImage());
        assertEquals(review1.getSpicy(),review.getSpicy());
        assertEquals(review1.getRestaurantTags(),review.getRestaurantTags());
        assertEquals(review1.getId(),review.getId());
        assertEquals(review1.getUser(),review.getUser());
    }

}*/
