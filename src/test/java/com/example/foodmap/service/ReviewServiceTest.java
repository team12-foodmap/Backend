//
//package com.example.foodmap.service;
//
//import com.example.foodmap.dto.Restaurant.RestaurantSaveRequestDto;
//import com.example.foodmap.dto.review.ReviewRequestDto;
//import com.example.foodmap.dto.review.ReviewUpdateRequestDto;
//import com.example.foodmap.exception.CustomException;
//import com.example.foodmap.exception.ErrorCode;
//import com.example.foodmap.model.*;
//import com.example.foodmap.repository.RestaurantRepository;
//import com.example.foodmap.repository.ReviewRepository;
//import com.example.foodmap.repository.UserRepository;
//import com.example.foodmap.security.UserDetailsImpl;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.transaction.Transactional;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
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
//    private ReviewRequestDto reviewRequestDto1;
//    private ReviewRequestDto reviewRequestDto2;
//    private ReviewUpdateRequestDto reviewUpdateRequestDto1;
//    private Location location1;
//    private Location location2;
//    private User user1;
//    private User user2;
//    private User user3;
//    private UserDetailsImpl userDetails1;
//    private UserDetailsImpl userDetails2;
//    private Restaurant restaurant1;
//    private Restaurant restaurant2;
//    private Restaurant restaurant3;
//    private RestaurantLikes restaurantLikes1;
//    private RestaurantLikes restaurantLikes2;
//    private String content;
//    private int spicy;
//    private String restaurantTags;
//    private MultipartFile image;
//    private Review review1;
//    private Review review2;
//    private Review review3;
//    private Pageable pageable;
//
//    @BeforeEach
//    void setup() {
//        location1 = new Location("?????????", 12.23, 34.21);
//        user1 = new User(
//                "?????????",
//                "a111",
//                6L,
//                "maxm@b123.com",
//                UserRoleEnum.USER,
//                1L,
//                null,
//                location1,
//                "????????????"
//        );
//        userRepository.save(user1);
//        userDetails1 = new UserDetailsImpl(user1);
//
//         location2 = new Location("?????????", 34.31, 34.21);
//        user2 = new User(
//                "?????????",
//                "a111",
//                2L,
//                "a@bs.com",
//                UserRoleEnum.USER,
//                2L,
//                "default.png",
//                location2,
//                "????????? ?????????"
//        );
//        userRepository.save(user2);
//        userDetails2 = new UserDetailsImpl(user2);
//
//        RestaurantSaveRequestDto restaurantSaveRequestDto1 = new RestaurantSaveRequestDto(
//                "??????",
//                22.33,
//                33.44,
//                "??????????????? ?????????",
//                "??????",
//                "??????",
//                "?????????",
//                "??????"
//        );
//        restaurant1 = new Restaurant(restaurantSaveRequestDto1, null, user1);
//        restaurantRepository.save(restaurant1);
//        restaurantLikes1 = new RestaurantLikes(restaurantLikes1.getId(), user1,restaurant1);
//        RestaurantSaveRequestDto restaurantSaveRequestDto2 = new RestaurantSaveRequestDto(
//                "??????",
//                22.33,
//                33.44,
//                "?????????",
//                "??????",
//                "????????????",
//                "?????????",
//                "??????"
//        );
//        restaurant2 = new Restaurant(restaurantSaveRequestDto2, null, user1);
//        restaurantRepository.save(restaurant2);
//        //????????????
//        reviewRequestDto1 = new ReviewRequestDto(
//                "?????? ???????????? ????????????.",
//                "1",
//                "2"
//        );
//        review1 = new Review(reviewRequestDto1, user1, restaurant1, null);
//        reviewRepository.save(review1);
//
//        reviewRequestDto2 = new ReviewRequestDto(
//                "?????? ???????????? ????????????.",
//                "5",
//                "3"
//        );
//        review2 = new Review(reviewRequestDto2, user1, restaurant1, "image1");
//        reviewRepository.save(review2);
//
//        reviewUpdateRequestDto1 = ReviewUpdateRequestDto.builder()
//                .content("?????????????????????????????????????????????")
//                .restaurantTags("1")
//                .spicy("5")
//                .build();
//
//    }
//
//    //region ???????????????
//    @Nested
//    @DisplayName("???????????????")
//    class sucess {
//        @Test
//        @DisplayName("?????? ??????")
//        void reviewCreate() {
//            //given
//            ReviewRequestDto reviewRequestDto = new ReviewRequestDto(
//                    "?????? ???????????? ????????????.",
//                    "1",
//                    "2"
//            );
//            review3 = new Review(reviewRequestDto, user1, restaurant2, null);
//            reviewRepository.save(review3);
//            MockMultipartFile mockMultipartFile = new MockMultipartFile(
//                    "image1", "image1", "application/doc", "image".getBytes()
//            );
//
//            //when
//            reviewService.createReview(restaurant2.getId(), reviewRequestDto, user1, mockMultipartFile);
//            //then
//        }
//
//        @Test
//        @DisplayName("?????? ??????")
//        void reviewUpdate() {
//            //given
//
//            Review review = reviewRepository.findById(review1.getId()).orElseThrow(
//                    ()-> new CustomException(ErrorCode.REVIEW_NOT_FOUND)
//            );
//
//            ReviewUpdateRequestDto reviewUpdateRequestDto = ReviewUpdateRequestDto.builder()
//                    .content("?????????????????????????????????????????????")
//                    .restaurantTags("1")
//                    .spicy("5")
//                    .build();
//
//            //when
//            review.updateReview(reviewUpdateRequestDto.getContent(), "image1", reviewUpdateRequestDto.getSpicy(), reviewUpdateRequestDto.getRestaurantTags());
//            //then
//            assertThat(review.getContent()).isEqualTo("?????????????????????????????????????????????");
//            assertThat(review.getSpicy()).isEqualTo(5);
//            assertThat(review.getRestaurantTags()).isEqualTo(1);
//        }
//
//        @Test
//        @DisplayName("?????? ??????")
//        void reviewDelete() {
//            //given
//            reviewRequestDto1 = new ReviewRequestDto(
//                    "?????? ???????????? ????????????!",
//                    "1",
//                    "2");
//
//            Review review = new Review(reviewRequestDto1, user1, restaurant1,"image1");
//            reviewRepository.save(review);
//            //when
//            reviewService.deleteReview(review.getId(), user1);
//        }
//
//        //region ????????????
//        @Test
//        @DisplayName("?????? ?????? ?????? ??????")
//        void review() {
//            //given
//
//            List<Review> reviewList = reviewRepository.findAllByUserId(user1.getId(),pageable);
//
//            assertThat(reviewList.size()).isEqualTo(2);
//
//        }
//        //endregion
//
//
//        //region ??????????????????
//        @Test
//        @DisplayName("?????? ?????? ?????? ??????")
//        void reviewList() {
//            //given
//
//            List<Review> reviewList = reviewRepository.findAllByRestaurantIdOrderByReviewLikeDesc(restaurant1.getId(),pageable);
//
//            assertThat(reviewList.size()).isEqualTo(2);
//
//        }
//        //endregion
//    }
//
//    //endregion
//
//    //region ???????????????
//    @Nested
//    @DisplayName("???????????????")
//    class fail {
//        @Test
//        @DisplayName("?????? ?????? - ????????? ?????? ???")
//        void ????????????() {
//            //given
//
//            //when
//            CustomException exception = assertThrows(CustomException.class,
//                    ()->reviewService.createReview(0L,reviewRequestDto1,user1,null));
//
//            assertEquals(exception.getErrorCode().getDetail(),"?????? ???????????? ???????????? ????????????.");
//        }
//        @Test
//        @DisplayName("?????? ?????? - ????????? ?????? ???")
//        void ????????????() {
//            //given
//
//            //when
//            CustomException exception = assertThrows(CustomException.class,
//                    ()->reviewService.updateReview(restaurant1.getId(),reviewUpdateRequestDto1,user1,null));
//
//            assertThat(exception.getErrorCode().getDetail()).isEqualTo("?????? ????????? ???????????? ????????????.");
//        }
//
////        @Test
////        @DisplayName("?????? ?????? - ????????? ?????? ???")
////        void ??????????????????() {
////            //given
////            restaurant1 = new Restaurant(12L,user1,"????????????",location1,"1","????????????","?????????","??????","test.jpg",restaurant1.getRestaurantLikes(), restaurant1.getReviews(), 0);
////            //when
////            CustomException exception = assertThrows(CustomException.class,
////                    ()->reviewService.updateReview(restaurant1.getId(),reviewUpdateRequestDto1,user1,null));
////
////            assertThat(exception.getErrorCode().getDetail()).isEqualTo("?????? ???????????? ???????????? ????????????.");
////        }
//
//        @Test
//        @DisplayName("?????? ?????? - ???????????? ?????? ???")
//        void ?????????????????????() {
//            //given
//
//            //when
//            Exception exception = assertThrows(IllegalArgumentException.class,
//                    ()->reviewService.deleteReview(review1.getId(),user2));
//
//            assertThat(exception.getMessage()).isEqualTo("????????? ???????????? ????????? ???????????????.");
//        }
//    }
//    //endregion
//
//
//}
//
