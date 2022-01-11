// package com.example.foodmap.service;

// import com.example.foodmap.dto.Restaurant.RestaurantSaveRequestDto;
// import com.example.foodmap.dto.review.ReviewAllResponseDto;
// import com.example.foodmap.dto.review.ReviewRequestDto;
// import com.example.foodmap.dto.review.ReviewUpdateRequestDto;
// import com.example.foodmap.model.*;
// import com.example.foodmap.repository.RestaurantRepository;
// import com.example.foodmap.repository.ReviewRepository;
// import com.example.foodmap.repository.UserRepository;
// import com.example.foodmap.security.UserDetailsImpl;
// import com.example.foodmap.service.ReviewService;
// import org.junit.jupiter.api.*;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.mock.web.MockMultipartFile;
// import org.springframework.web.multipart.MultipartFile;

// import javax.transaction.Transactional;
// import java.util.ArrayList;
// import java.util.List;

// import static org.assertj.core.api.Assertions.assertThat;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @Transactional
// @TestInstance(TestInstance.Lifecycle.PER_CLASS)
// class ReviewServiceTest {

//     @Autowired
//     ReviewRepository reviewRepository;
//     @Autowired
//     UserRepository userRepository;
//     @Autowired
//     RestaurantRepository restaurantRepository;
//     @Autowired
//     ReviewService reviewService;

//     private ReviewRequestDto reviewRequestDto;
//     private User user1;
//     private User user2;
//     private UserDetailsImpl userDetails1;
//     private UserDetailsImpl userDetails2;
//     private Restaurant restaurant1;
//     private Restaurant restaurant2;
//     private String content;
//     private int spicy;
//     private String restaurantTags;
//     private MultipartFile image;

//     @BeforeEach
//     void setup() {
//         Location location1 = new Location("강서구", 12.23, 34.21);
//         user1 = new User(
//                 "백정수",
//                 "a111",
//                 6L,
//                 "maxm@b123.com",
//                 UserRoleEnum.USER,
//                 1L,
//                 null,
//                 location1,
//                 "클린워터"
//         );
//         userRepository.save(user1);
//         userDetails1 = new UserDetailsImpl(user1);

// //        Location location2 = new Location("강북구", 34.31, 34.21);
// //        user1 = new User(
// //                "이한울",
// //                "a111",
// //                2L,
// //                "a@bs.com",
// //                UserRoleEnum.USER,
// //                2L,
// //                "default.png",
// //                location2,
// //                "별명이 뭔가요"
// //        );
// //        userRepository.save(user2);
// //        userDetails2 = new UserDetailsImpl(user2);

//         RestaurantSaveRequestDto restaurantSaveRequestDto1 = new RestaurantSaveRequestDto(
//                 "엽떡",
//                 22.33,
//                 33.44,
//                 "서울특별시 강동구",
//                 "분식",
//                 "판매",
//                 "허파만",
//                 "밀떡"
//         );
//         restaurant1 = new Restaurant(restaurantSaveRequestDto1, null, user1, location1);
//         restaurantRepository.save(restaurant1);

//         RestaurantSaveRequestDto restaurantSaveRequestDto2 = new RestaurantSaveRequestDto(
//                 "안돼",
//                 22.33,
//                 33.44,
//                 "강동구",
//                 "분식",
//                 "판매안함",
//                 "허파만",
//                 "밀떡"
//         );
//         restaurant2 = new Restaurant(restaurantSaveRequestDto2, null, user1, location1);


//     }

//     //region 성공케이스
//     @Nested
//     @DisplayName("성공케이스")
//     class sucess {
//         @Test
//         @DisplayName("리뷰 등록")
//         void reviewCreate() {
//             //given
//             reviewRequestDto = new ReviewRequestDto(
//                     "여기진짜진짜맛있어요!",
//                     "1",
//                     "5"
//             );
//             MockMultipartFile mockMultipartFile = new MockMultipartFile(
//                     "image1", "image1", "application/doc", "image".getBytes()
//             );

//             //when
//             reviewService.createReview(restaurant1.getId(), reviewRequestDto, user1,mockMultipartFile);
//             //then
//         }

//         @Test
//         @DisplayName("리뷰 수정")
//         void reviewUpdate() {
//             //given
//             reviewRequestDto = new ReviewRequestDto(
//                     "여기완전완전맛있어요!",
//                     "5",
//                     "3"
//             );


//             Review review = Review.builder()
//                     .restaurant(restaurant1)
//                     .user(user1)
//                     .reviewRequestDto(reviewRequestDto)
//                     .imagePath("image1")
//                     .build();
//             reviewRepository.save(review);

//             ReviewUpdateRequestDto reviewUpdateRequestDto = ReviewUpdateRequestDto.builder()
//                     .content("매우매우맛이없어졌네요왜이래요")
//                     .restaurantTags("1")
//                     .spicy("5")
//                     .build();

//             MockMultipartFile mockMultipartFile = new MockMultipartFile(
//                     "image1", "image1", "application/doc", "image".getBytes()
//             );
//             //when

//             review.updateReview(reviewUpdateRequestDto.getContent(),"image1",reviewUpdateRequestDto.getSpicy(),reviewUpdateRequestDto.getRestaurantTags());
//             //then
//             assertThat(review.getContent()).isEqualTo("매우매우맛이없어졌네요왜이래요");
//             assertThat(review.getSpicy()).isEqualTo(5);
//             assertThat(review.getRestaurantTags()).isEqualTo(1);
//         }

//         @Test
//         @DisplayName("리뷰 삭제")
//         void reviewDelete() {
//             //given
//             reviewRequestDto = new ReviewRequestDto(
//                     "여기 되게되게 맛있어요!",
//                     "1",
//                     "2");

//             Review review = new Review(reviewRequestDto, user1, restaurant1,"image1");
//             reviewRepository.save(review);
//             //when
//             reviewService.deleteReview(review.getId(), user1);
//         }


//         //region 리뷰전체조회
//         @Test
//         @DisplayName("리뷰 전체 조회")
//         void reviewList() {
//             //given
//             List<ReviewAllResponseDto> reviewLists = new ArrayList<>();
//             reviewRequestDto = new ReviewRequestDto(
//                     "여기 되게되게 맛있어요!",
//                     "1",
//                     "2");
//             Review reviews = Review.builder()
//                     .restaurant(restaurant1)
//                     .user(user1)
//                     .reviewRequestDto(reviewRequestDto)
//                     .imagePath("image1")
//                     .build();
//             reviewRepository.save(reviews);
//             Review reviews2 = Review.builder()
//                     .restaurant(restaurant1)
//                     .user(user1)
//                     .reviewRequestDto(reviewRequestDto)
//                     .imagePath("image1")
//                     .build();
//             reviewRepository.save(reviews2);


//             List<Review> reviewList = reviewRepository.findAllByRestaurantIdOrderByReviewLikeDesc(restaurant1.getId());
//             for(Review review : reviewList) {
//                 ReviewAllResponseDto reviewAllResponseDto = ReviewAllResponseDto.builder()
//                         .reviewId(review.getId())
//                         .userId(review.getUser().getId())
//                         .restaurantId(review.getRestaurant().getId())
//                         .content(review.getContent())
//                         .reviewLikes(review.getReviewLike())
//                         .restaurantTags(review.getRestaurantTags())
//                         .image("image1")
//                         .build();
//                 reviewLists.add(reviewAllResponseDto);
//             }
//             assertThat(reviewLists.size()).isEqualTo(2);

//         }
//         //endregion
//     }

//     //endregion


// //region 성공케이스
// //    @Nested
// //    @DisplayName("실패케이스")
// //    class fail {
// //        @Test
// //        @DisplayName("음식점 null")
// //        void 음식점없음() {
// //            //given
// //
// //            reviewRequestDto = new ReviewRequestDto(
// //                    "여기맛있어요!",
// //                    1,
// //                    "진짜 맛있네요!",
// //                    null);
// //
// //            //when
// //            reviewService.createReview(restaurant2.getId(), reviewRequestDto, user1);
// //
// //            //then
// //            assertThat(restaurant2.getId()).isEqualTo("해당 음식점이 존재하지 않습니다.");
// //        }
// //
// //        @Test
// //        @DisplayName("리뷰 수정")
// //        void 리뷰수정() {
// //            //given
// //            reviewRequestDto = new ReviewRequestDto(
// //                    "여기맛있어요!",
// //                    1,
// //                    "진짜 맛있네요!",
// //                    null
// //            );
// //
// //            Review review = new Review(reviewRequestDto, user1, restaurant1);
// //            reviewRepository.save(review);
// //
// //            ReviewUpdateRequestDto reviewUpdateRequestDto = new ReviewUpdateRequestDto(
// //                    "별로네요",
// //                    100,
// //                    "흠..",
// //                    null
// //            );
// //            reviewService.updateReview(review.getId(), reviewUpdateRequestDto, user1);
// //
// //            //then
// //            assertThat(review.getContent()).isEqualTo("별로네요");
// //            assertThat(review.getSpicy()).isEqualTo(100);
// //            assertThat(review.getRestaurantTags()).isEqualTo("흠..");
// //        }
// //    }
// }
