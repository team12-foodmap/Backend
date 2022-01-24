//
//package com.example.foodmap.service;
//
//import com.example.foodmap.config.EmbeddedS3Config;
//import com.example.foodmap.dto.Restaurant.RestaurantSaveRequestDto;
//import com.example.foodmap.dto.review.ReviewRequestDto;
//import com.example.foodmap.model.*;
//import com.example.foodmap.repository.*;
//import com.example.foodmap.security.UserDetailsImpl;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.domain.Pageable;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.transaction.Transactional;
//import java.util.List;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Transactional
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@Rollback
//@ActiveProfiles("test")
//@Import(EmbeddedS3Config.class)
//class ReviewServiceTest {
//
//    @Autowired
//    ReviewRepository reviewRepository;
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    RestaurantRepository restaurantRepository;
//    @Autowired
//    MeetingRepository meetingRepository;
//    @Autowired
//    RestaurantLikesRepository restaurantLikesRepository;
//    @Autowired
//    MyPageRepository myPageRepository;
//    @Autowired
//    MyPageService myPageService;
//    @Autowired
//    StorageService storageService;
//    @Autowired
//    ReviewService reviewService;
//
//    private User user1;
//    private User user2;
//    private UserDetailsImpl userDetails1;
//    private UserDetailsImpl userDetails2;
//    private Restaurant restaurant1;
//    private Restaurant restaurant2;
//    private RestaurantSaveRequestDto restaurantSaveRequestDto;
//    private List<Review> review1;
//    private List<Review> review2;
//    private Review review3;
//    private Review review;
//    private Review review4;
//    private Review review5;
//    private Review review6;
//    private List<RestaurantLikes> restaurantLikes1;
//    private List<RestaurantLikes> restaurantLikes2;
//    private RestaurantLikes restaurantLikes3;
//    private RestaurantLikes restaurantLikes4;
//    private RestaurantLikes restaurantLikes5;
//    private Pageable pageable;
//    private ReviewRequestDto reviewRequestDto;
//    private MultipartFile image;
//    private String imagePath;
//
//    @BeforeEach
//    void setup() {
//        Location location1 = new Location("강서구", 12.23, 34.21);
//        user1 = new User(
//                "백정수",
//                "a111",
//                100L,
//                "a@b.com",
//                UserRoleEnum.USER,
//                1L,
//                null,
//                location1,
//                "클린워터"
//        );
//        userRepository.save(user1);
//        userDetails1 = new UserDetailsImpl(user1);
//
//        Location location2 = new Location("강북구", 34.31, 34.21);
//        user2 = new User(
//                "이한울",
//                "a111",
//                200L,
//                "a@bs.com",
//                UserRoleEnum.USER,
//                2L,
//                null,
//                location2,
//                "별명이 뭔가요"
//        );
//        userRepository.save(user2);
//        userDetails2 = new UserDetailsImpl(user2);
//
//        restaurant1 = new Restaurant(
//                1L,
//                user1,
//                "식당1",
//                location1,
//                "포장마차",
//                "튀김있음",
//                "순대만",
//                "밀떡",
//                null,
//                restaurantLikes1,
//                review1,
//                3
//        );
//        restaurantRepository.save(restaurant1);
//
//        restaurant2 = new Restaurant(
//                2L,
//                user2,
//                "식당2",
//                location2,
//                "포장마차",
//                "튀김있음",
//                "순대만",
//                "밀떡",
//                null,
//                restaurantLikes2,
//                review2,
//                2
//        );
//        restaurantRepository.save(restaurant2);
//
//        //리뷰저장
//        review3 = new Review(
//                1L,
//                "너무너무너무 맛있어요!",
//                3,
//                1,
//                null,
//                user1,
//                restaurant1,
//                3,
//                null
//        );
//        reviewRepository.save(review3);
//        review4 = new Review(
//                2L,
//                "너무너무너무 맛있어요!",
//                3,
//                1,
//                null,
//                user1,
//                restaurant1,
//                3,
//                null
//        );
//        reviewRepository.save(review4);
//        review5 = new Review(
//                3L,
//                "너무너무너무 맛있어요!",
//                3,
//                1,
//                null,
//                user2,
//                restaurant1,
//                3,
//                null
//        );
//        reviewRepository.save(review5);
//
//        //찜하기
//        restaurantLikes3 = new RestaurantLikes(
//                1L,
//                user2,
//                restaurant1
//        );
//        restaurantLikesRepository.save(restaurantLikes3);
//        imagePath = "restaurant/2771ea67-c747-4529-b91f-9dcea5a2ac94_스프링르탄이.png";
//    }
//
//    //region 성공케이스
//    @Nested
//    @DisplayName("성공케이스")
//    class sucess {
//        @Test
//        @DisplayName("리뷰 등록")
//        void reviewCreate() {
//            //given
//            String imagePath = storageService.uploadFile(image, "review");
//            reviewRequestDto = ReviewRequestDto.builder()
//                    .content("정말정말별ㄹ로네요어어엉오오")
//                    .spicy("5")
//                    .restaurantTags("1")
//                    .build();
//
//            String expected = "mock1.png";
//            MockMultipartFile mockMultipartFile = new MockMultipartFile("file", expected,
//                    "image/png", "test data".getBytes());
//
//            review = reviewRequestDto.toEntity(user1, restaurant1,expected );
//
//            review.addRestaurant(restaurant1);
//            reviewRepository.save(review);
//
//
//            //when
//
//            //then
//        }
////
////        @Test
////        @DisplayName("리뷰 수정")
////        void reviewUpdate() {
////            //given
////
////            Review review = reviewRepository.findById(review1.getId()).orElseThrow(
////                    ()-> new CustomException(ErrorCode.REVIEW_NOT_FOUND)
////            );
////
////            ReviewUpdateRequestDto reviewUpdateRequestDto = ReviewUpdateRequestDto.builder()
////                    .content("매우매우맛이없어졌네요왜이래요")
////                    .restaurantTags("1")
////                    .spicy("5")
////                    .build();
////
////            //when
////            review.updateReview(reviewUpdateRequestDto.getContent(), "image1", reviewUpdateRequestDto.getSpicy(), reviewUpdateRequestDto.getRestaurantTags());
////            //then
////            assertThat(review.getContent()).isEqualTo("매우매우맛이없어졌네요왜이래요");
////            assertThat(review.getSpicy()).isEqualTo(5);
////            assertThat(review.getRestaurantTags()).isEqualTo(1);
////        }
////
////        @Test
////        @DisplayName("리뷰 삭제")
////        void reviewDelete() {
////            //given
////            reviewRequestDto1 = new ReviewRequestDto(
////                    "여기 되게되게 맛있어요!",
////                    "1",
////                    "2");
////
////            Review review = new Review(reviewRequestDto1, user1, restaurant1,"image1");
////            reviewRepository.save(review);
////            //when
////            reviewService.deleteReview(review.getId(), user1);
////        }
////
////        //region 리뷰조회
////        @Test
////        @DisplayName("개인 리뷰 전체 조회")
////        void review() {
////            //given
////
////            List<Review> reviewList = reviewRepository.findAllByUserId(user1.getId(),pageable);
////
////            assertThat(reviewList.size()).isEqualTo(2);
////
////        }
////        //endregion
////
////
////        //region 리뷰전체조회
////        @Test
////        @DisplayName("식당 리뷰 전체 조회")
////        void reviewList() {
////            //given
////
////            List<Review> reviewList = reviewRepository.findAllByRestaurantIdOrderByReviewLikeDesc(restaurant1.getId(),pageable);
////
////            assertThat(reviewList.size()).isEqualTo(2);
////
////        }
////        //endregion
////    }
////
////    //endregion
////
////    //region 실패케이스
////    @Nested
////    @DisplayName("실패케이스")
////    class fail {
////        @Test
////        @DisplayName("리뷰 등록 - 식당이 없을 때")
////        void 식당없음() {
////            //given
////
////            //when
////            CustomException exception = assertThrows(CustomException.class,
////                    ()->reviewService.createReview(0L,reviewRequestDto1,user1,null));
////
////            assertEquals(exception.getErrorCode().getDetail(),"해당 음식점이 존재하지 않습니다.");
////        }
////        @Test
////        @DisplayName("리뷰 수정 - 리뷰가 없을 때")
////        void 리뷰없음() {
////            //given
////
////            //when
////            CustomException exception = assertThrows(CustomException.class,
////                    ()->reviewService.updateReview(restaurant1.getId(),reviewUpdateRequestDto1,user1,null));
////
////            assertThat(exception.getErrorCode().getDetail()).isEqualTo("해당 리뷰가 존재하지 않습니다.");
////        }
////
//////        @Test
//////        @DisplayName("리뷰 수정 - 식당이 없을 때")
//////        void 수정식당없음() {
//////            //given
//////            restaurant1 = new Restaurant(12L,user1,"실패식당",location1,"1","튀김있음","순대만","밀떡","test.jpg",restaurant1.getRestaurantLikes(), restaurant1.getReviews(), 0);
//////            //when
//////            CustomException exception = assertThrows(CustomException.class,
//////                    ()->reviewService.updateReview(restaurant1.getId(),reviewUpdateRequestDto1,user1,null));
//////
//////            assertThat(exception.getErrorCode().getDetail()).isEqualTo("해당 음식점이 존재하지 않습니다.");
//////        }
////
////        @Test
////        @DisplayName("리뷰 삭제 - 사용자가 다를 때")
////        void 삭제사용자다름() {
////            //given
////
////            //when
////            Exception exception = assertThrows(IllegalArgumentException.class,
////                    ()->reviewService.deleteReview(review1.getId(),user2));
////
////            assertThat(exception.getMessage()).isEqualTo("댓글의 작성자만 삭제가 가능합니다.");
////        }
////    }
//        //endregion
//    }
//
//
//
//}
//
