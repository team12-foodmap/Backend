///*
//package com.example.foodmap.service;
//
//import com.example.foodmap.dto.Restaurant.RestaurantSaveRequestDto;
//import com.example.foodmap.dto.review.ReviewRequestDto;
//import com.example.foodmap.dto.review.ReviewUpdateRequestDto;
//import com.example.foodmap.model.*;
//import com.example.foodmap.repository.RestaurantRepository;
//import com.example.foodmap.repository.ReviewRepository;
//import com.example.foodmap.repository.UserRepository;
//import com.example.foodmap.security.UserDetailsImpl;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.transaction.Transactional;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Transactional
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class ReviewServiceTest {
//
//    @Autowired
//    ReviewRepository reviewRepository;
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    RestaurantRepository restaurantRepository;
//    @Autowired
//    ReviewService reviewService;
//
//    private ReviewRequestDto reviewRequestDto;
//
//    private User user1;
//    private User user2;
//    private UserDetailsImpl userDetails1;
//    private UserDetailsImpl userDetails2;
//    private Restaurant restaurant1;
//    private Restaurant restaurant2;
//    private String content;
//    private int spicy;
//    private String restaurantTags;
//    private MultipartFile image;
//
//    @BeforeEach
//    void setup() {
//        Location location1 = new Location("강서구", 12.23, 34.21);
//        user1 = new User(
//                "백정수",
//                "a111",
//                1L,
//                "a@b.com",
//                UserRoleEnum.USER,
//                1L,
//                "default.png",
//                location1,
//                "클린워터"
//        );
//        userRepository.save(user1);
//        userDetails1 = new UserDetailsImpl(user1);
//
////        Location location2 = new Location("강북구", 34.31, 34.21);
////        user1 = new User(
////                "이한울",
////                "a111",
////                2L,
////                "a@bs.com",
////                UserRoleEnum.USER,
////                2L,
////                "default.png",
////                location2,
////                "별명이 뭔가요"
////        );
////        userRepository.save(user2);
////        userDetails2 = new UserDetailsImpl(user2);
//
//        RestaurantSaveRequestDto restaurantSaveRequestDto1 = new RestaurantSaveRequestDto(
//                "엽떡",
//                22.33,
//                33.44,
//                "강동구",
//                "분식",
//                true,
//                "허파만",
//                "밀떡",
//                null
//        );
//        restaurant1 = new Restaurant(restaurantSaveRequestDto1, "default.png", user1, location1);
//        restaurantRepository.save(restaurant1);
//
//        RestaurantSaveRequestDto restaurantSaveRequestDto2 = new RestaurantSaveRequestDto(
//                "안돼",
//                22.33,
//                33.44,
//                "강동구",
//                "분식",
//                true,
//                "허파만",
//                "밀떡",
//                null
//        );
//        restaurant2 = new Restaurant(restaurantSaveRequestDto2, "default.png", user1, location1);
//
//
//    }
//
//    //region 성공케이스
//    @Nested
//    @DisplayName("성공케이스")
//    class sucess {
//        @Test
//        @DisplayName("리뷰 등록")
//        void 리뷰등록() {
//            //given
//            reviewRequestDto = new ReviewRequestDto(
//                    "여기맛있어요!",
//                    1,
//                    "진짜 맛있네요!",
//                    null);
//
//            //when
//            reviewService.createReview(restaurant1.getId(), reviewRequestDto, user1);
//            //then
//        }
//
//        @Test
//        @DisplayName("리뷰 수정")
//        void 리뷰수정() {
//            //given
//            reviewRequestDto = new ReviewRequestDto(
//                    "여기맛있어요!",
//                    1,
//                    "진짜 맛있네요!",
//                    null
//            );
//
//            Review review = new Review(reviewRequestDto, user1, restaurant1);
//            reviewRepository.save(review);
//
//            ReviewUpdateRequestDto reviewUpdateRequestDto = new ReviewUpdateRequestDto(
//                    "별로네요",
//                    100,
//                    "흠..",
//                    null
//            );
//            reviewService.updateReview(review.getId(), reviewUpdateRequestDto, user1);
//
//            //then
//            assertThat(review.getContent()).isEqualTo("별로네요");
//            assertThat(review.getSpicy()).isEqualTo(100);
//            assertThat(review.getRestaurantTags()).isEqualTo("흠..");
//        }
//
//        @Test
//        @DisplayName("리뷰 삭제")
//        void 리뷰삭제() {
//            //given
//            reviewRequestDto = new ReviewRequestDto(
//                    "여기맛있어요!",
//                    1,
//                    "진짜 맛있네요!",
//                    null);
//            Review review = new Review(reviewRequestDto, user1, restaurant1);
//            reviewRepository.save(review);
//            //when
//            reviewService.deleteRestaurant(restaurant1.getId(), user1);
//        }
//    }
//
//    //endregion
////region 성공케이스
////    @Nested
////    @DisplayName("실패케이스")
////    class fail {
////        @Test
////        @DisplayName("음식점 null")
////        void 음식점없음() {
////            //given
////
////            reviewRequestDto = new ReviewRequestDto(
////                    "여기맛있어요!",
////                    1,
////                    "진짜 맛있네요!",
////                    null);
////
////            //when
////            reviewService.createReview(restaurant2.getId(), reviewRequestDto, user1);
////
////            //then
////            assertThat(restaurant2.getId()).isEqualTo("해당 음식점이 존재하지 않습니다.");
////        }
////
////        @Test
////        @DisplayName("리뷰 수정")
////        void 리뷰수정() {
////            //given
////            reviewRequestDto = new ReviewRequestDto(
////                    "여기맛있어요!",
////                    1,
////                    "진짜 맛있네요!",
////                    null
////            );
////
////            Review review = new Review(reviewRequestDto, user1, restaurant1);
////            reviewRepository.save(review);
////
////            ReviewUpdateRequestDto reviewUpdateRequestDto = new ReviewUpdateRequestDto(
////                    "별로네요",
////                    100,
////                    "흠..",
////                    null
////            );
////            reviewService.updateReview(review.getId(), reviewUpdateRequestDto, user1);
////
////            //then
////            assertThat(review.getContent()).isEqualTo("별로네요");
////            assertThat(review.getSpicy()).isEqualTo(100);
////            assertThat(review.getRestaurantTags()).isEqualTo("흠..");
////        }
////    }
//}
//
//*/
