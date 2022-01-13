package com.example.foodmap.service;

import com.example.foodmap.dto.Restaurant.RestaurantSaveRequestDto;
import com.example.foodmap.dto.review.ReviewRequestDto;
import com.example.foodmap.dto.review.ReviewUpdateRequestDto;
import com.example.foodmap.exception.CustomException;
import com.example.foodmap.exception.ErrorCode;
import com.example.foodmap.model.*;
import com.example.foodmap.repository.RestaurantRepository;
import com.example.foodmap.repository.ReviewRepository;
import com.example.foodmap.repository.UserRepository;
import com.example.foodmap.security.UserDetailsImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReviewServiceTest {

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    ReviewService reviewService;

    private ReviewRequestDto reviewRequestDto1;
    private ReviewRequestDto reviewRequestDto2;
    private ReviewUpdateRequestDto reviewUpdateRequestDto1;
    private Location location1;
    private Location location2;
    private User user1;
    private User user2;
    private User user3;
    private UserDetailsImpl userDetails1;
    private UserDetailsImpl userDetails2;
    private Restaurant restaurant1;
    private Restaurant restaurant2;
    private Restaurant restaurant3;
    private RestaurantLikes restaurantLikes1;
    private RestaurantLikes restaurantLikes2;
    private String content;
    private int spicy;
    private String restaurantTags;
    private MultipartFile image;
    private Review review1;
    private Review review2;
    private Review review3;
    private Pageable pageable;

    @BeforeEach
    void setup() {
        location1 = new Location("강서구", 12.23, 34.21);
        user1 = new User(
                "백정수",
                "a111",
                6L,
                "maxm@b123.com",
                UserRoleEnum.USER,
                1L,
                null,
                location1,
                "클린워터"
        );
        userRepository.save(user1);
        userDetails1 = new UserDetailsImpl(user1);

         location2 = new Location("강북구", 34.31, 34.21);
        user2 = new User(
                "이한울",
                "a111",
                2L,
                "a@bs.com",
                UserRoleEnum.USER,
                2L,
                "default.png",
                location2,
                "별명이 뭔가요"
        );
        userRepository.save(user2);
        userDetails2 = new UserDetailsImpl(user2);

        RestaurantSaveRequestDto restaurantSaveRequestDto1 = new RestaurantSaveRequestDto(
                "엽떡",
                22.33,
                33.44,
                "서울특별시 강동구",
                "분식",
                "판매",
                "허파만",
                "밀떡"
        );
        restaurant1 = new Restaurant(restaurantSaveRequestDto1, null, user1);
        restaurantRepository.save(restaurant1);
        restaurantLikes1 = new RestaurantLikes(restaurantLikes1.getId(), user1,restaurant1);
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
        restaurant2 = new Restaurant(restaurantSaveRequestDto2, null, user1);
        restaurantRepository.save(restaurant2);
        //리뷰저장
        reviewRequestDto1 = new ReviewRequestDto(
                "이집 매우매우 잘하네요.",
                "1",
                "2"
        );
        review1 = new Review(reviewRequestDto1, user1, restaurant1, null);
        reviewRepository.save(review1);

        reviewRequestDto2 = new ReviewRequestDto(
                "이집 매우매우 별로네요.",
                "5",
                "3"
        );
        review2 = new Review(reviewRequestDto2, user1, restaurant1, "image1");
        reviewRepository.save(review2);

        reviewUpdateRequestDto1 = ReviewUpdateRequestDto.builder()
                .content("매우매우맛이없어졌네요왜이래요")
                .restaurantTags("1")
                .spicy("5")
                .build();

    }

    //region 성공케이스
    @Nested
    @DisplayName("성공케이스")
    class sucess {
        @Test
        @DisplayName("리뷰 등록")
        void reviewCreate() {
            //given
            ReviewRequestDto reviewRequestDto = new ReviewRequestDto(
                    "이집 매우매우 잘하네요.",
                    "1",
                    "2"
            );
            review3 = new Review(reviewRequestDto, user1, restaurant2, null);
            reviewRepository.save(review3);
            MockMultipartFile mockMultipartFile = new MockMultipartFile(
                    "image1", "image1", "application/doc", "image".getBytes()
            );

            //when
            reviewService.createReview(restaurant2.getId(), reviewRequestDto, user1, mockMultipartFile);
            //then
        }

        @Test
        @DisplayName("리뷰 수정")
        void reviewUpdate() {
            //given

            Review review = reviewRepository.findById(review1.getId()).orElseThrow(
                    ()-> new CustomException(ErrorCode.REVIEW_NOT_FOUND)
            );

            ReviewUpdateRequestDto reviewUpdateRequestDto = ReviewUpdateRequestDto.builder()
                    .content("매우매우맛이없어졌네요왜이래요")
                    .restaurantTags("1")
                    .spicy("5")
                    .build();

            //when
            review.updateReview(reviewUpdateRequestDto.getContent(), "image1", reviewUpdateRequestDto.getSpicy(), reviewUpdateRequestDto.getRestaurantTags());
            //then
            assertThat(review.getContent()).isEqualTo("매우매우맛이없어졌네요왜이래요");
            assertThat(review.getSpicy()).isEqualTo(5);
            assertThat(review.getRestaurantTags()).isEqualTo(1);
        }

        @Test
        @DisplayName("리뷰 삭제")
        void reviewDelete() {
            //given
            reviewRequestDto1 = new ReviewRequestDto(
                    "여기 되게되게 맛있어요!",
                    "1",
                    "2");

            Review review = new Review(reviewRequestDto1, user1, restaurant1,"image1");
            reviewRepository.save(review);
            //when
            reviewService.deleteReview(review.getId(), user1);
        }

        //region 리뷰조회
        @Test
        @DisplayName("개인 리뷰 전체 조회")
        void review() {
            //given

            List<Review> reviewList = reviewRepository.findAllByUserId(user1.getId(),pageable);

            assertThat(reviewList.size()).isEqualTo(2);

        }
        //endregion


        //region 리뷰전체조회
        @Test
        @DisplayName("식당 리뷰 전체 조회")
        void reviewList() {
            //given

            List<Review> reviewList = reviewRepository.findAllByRestaurantIdOrderByReviewLikeDesc(restaurant1.getId(),pageable);

            assertThat(reviewList.size()).isEqualTo(2);

        }
        //endregion
    }

    //endregion

    //region 실패케이스
    @Nested
    @DisplayName("실패케이스")
    class fail {
        @Test
        @DisplayName("리뷰 등록 - 식당이 없을 때")
        void 식당없음() {
            //given

            //when
            CustomException exception = assertThrows(CustomException.class,
                    ()->reviewService.createReview(0L,reviewRequestDto1,user1,null));

            assertEquals(exception.getErrorCode().getDetail(),"해당 음식점이 존재하지 않습니다.");
        }
        @Test
        @DisplayName("리뷰 수정 - 리뷰가 없을 때")
        void 리뷰없음() {
            //given

            //when
            CustomException exception = assertThrows(CustomException.class,
                    ()->reviewService.updateReview(restaurant1.getId(),reviewUpdateRequestDto1,user1,null));

            assertThat(exception.getErrorCode().getDetail()).isEqualTo("해당 리뷰가 존재하지 않습니다.");
        }

//        @Test
//        @DisplayName("리뷰 수정 - 식당이 없을 때")
//        void 수정식당없음() {
//            //given
//            restaurant1 = new Restaurant(12L,user1,"실패식당",location1,"1","튀김있음","순대만","밀떡","test.jpg",restaurant1.getRestaurantLikes(), restaurant1.getReviews(), 0);
//            //when
//            CustomException exception = assertThrows(CustomException.class,
//                    ()->reviewService.updateReview(restaurant1.getId(),reviewUpdateRequestDto1,user1,null));
//
//            assertThat(exception.getErrorCode().getDetail()).isEqualTo("해당 음식점이 존재하지 않습니다.");
//        }

        @Test
        @DisplayName("리뷰 삭제 - 사용자가 다를 때")
        void 삭제사용자다름() {
            //given

            //when
            Exception exception = assertThrows(IllegalArgumentException.class,
                    ()->reviewService.deleteReview(review1.getId(),user2));

            assertThat(exception.getMessage()).isEqualTo("댓글의 작성자만 삭제가 가능합니다.");
        }
    }
    //endregion


}
