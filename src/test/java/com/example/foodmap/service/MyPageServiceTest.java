//
//package com.example.foodmap.service;
//
//import com.example.foodmap.dto.Restaurant.RestaurantReviewResponseDto;
//import com.example.foodmap.dto.Restaurant.RestaurantSaveRequestDto;
//import com.example.foodmap.dto.meeting.MeetingCreatRequestDto;
//import com.example.foodmap.dto.mypage.MyLikeResponseDto;
//import com.example.foodmap.dto.mypage.MyReviewResponseDto;
//import com.example.foodmap.model.*;
//import com.example.foodmap.repository.*;
//import com.example.foodmap.security.UserDetailsImpl;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.repository.query.Param;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.TestPropertySource;
//
//import javax.transaction.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Transactional
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@Rollback
//class MyPageServiceTest {
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
//    MeetingParticipateRepository meetingParticipateRepository;
//    @Autowired
//    RestaurantLikesRepository restaurantLikesRepository;
//    @Autowired
//    MyPageRepository myPageRepository;
//    @Autowired
//    MyPageService myPageService;
//
//    private User user;
//    private User user2;
//    private UserDetailsImpl userDetails1;
//    private UserDetailsImpl userDetails2;
//    private Restaurant restaurant1;
//    private Restaurant restaurant2;
//    private RestaurantSaveRequestDto restaurantSaveRequestDto;
//    private List<Review> review1;
//    private List<Review> review2;
//    private Review review3;
//    private Review review4;
//    private Review review5;
//    private Review review6;
//    private Meeting meeting1;
//    private Meeting meeting2;
//    private List<RestaurantLikes> restaurantLikes1;
//    private List<RestaurantLikes> restaurantLikes2;
//    private RestaurantLikes restaurantLikes3;
//    private RestaurantLikes restaurantLikes4;
//    private RestaurantLikes restaurantLikes5;
//    private MeetingParticipate meetingParticipate;
//    private Pageable pageable;
//
//    //region 사전세팅
//    @BeforeEach
//    void setup() {
//        //유저위치 & 유저 저장
//        Location location1 = new Location("강서구", 12.23, 34.21);
//        user = new User(
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
//        userRepository.save(user);
//        userDetails1 = new UserDetailsImpl(user);
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
//       restaurant1 = new Restaurant(
//               1L,
//               user,
//               "식당1",
//               location1,
//               "포장마차",
//               "튀김있음",
//               "순대만",
//               "밀떡",
//               null,
//               restaurantLikes1,
//               review1,
//               0
//       );
//        restaurantRepository.save(restaurant1);
//
//        restaurant2 = new Restaurant(
//                2L,
//                user,
//                "식당2",
//                location2,
//                "포장마차",
//                "튀김있음",
//                "순대만",
//                "밀떡",
//                null,
//                restaurantLikes2,
//                review2,
//                0
//        );
//        restaurantRepository.save(restaurant2);
//
//
////        //식당 저장
////        RestaurantSaveRequestDto restaurantSaveRequestDto1 = new RestaurantSaveRequestDto(
////                "엽떡",
////                22.33,
////                33.44,
////                "서울특별시 강동구",
////                "분식",
////                "판매",
////                "허파만",
////                "밀떡"
////        );
////        restaurant1 = new Restaurant(restaurantSaveRequestDto1, null, user1, location1);
////        restaurantRepository.save(restaurant1);
////
////        RestaurantSaveRequestDto restaurantSaveRequestDto2 = new RestaurantSaveRequestDto(
////                "안돼",
////                22.33,
////                33.44,
////                "강동구",
////                "분식",
////                "판매안함",
////                "허파만",
////                "밀떡"
////        );
////        restaurant2 = new Restaurant(restaurantSaveRequestDto2, null, user1, location1);
//
//        //식당 찜하기저장
////
////        restaurantLikes3 = new RestaurantLikes(1L,user, restaurant2);
////        restaurantLikesRepository.save(restaurantLikes3);
//
//        //리뷰저장
//        review3 = new Review(
//                1L,
//                "너무너무너무 맛있어요!",
//                3,
//                1,
//                null,
//                user,
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
//                user,
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
//                user,
//                restaurant1
//        );
//        restaurantLikesRepository.save(restaurantLikes3);
//
////        restaurantLikes4 = new RestaurantLikes(
////                49L,
////                user,
////                restaurant2
////        );
////        restaurantLikesRepository.save(restaurantLikes4);
////
////        restaurantLikes5 = new RestaurantLikes(
////                50L,
////                user2,
////                restaurant1
////        );
////        restaurantLikesRepository.save(restaurantLikes5);
//
//        //모임저장
//         meeting1 = new Meeting(
//                 user,
//                "신전떡볶이",
//                restaurant1.getId(),
//                "떡볶이 같이 드실 분",
//                "같이 드실 분 구합니다",
//                "서울 강남역",
//                LocalDateTime.of(2022, 1, 1, 12, 00),
//                LocalDateTime.of(2022, 1, 10, 12, 00),
//                LocalDateTime.of(2022, 1, 11, 12, 00),
//                0,
//                5,
//                1
//        );
//        meetingRepository.save(meeting1);
//
//        meeting2 = new Meeting(
//                user,
//                "신전떡볶이2",
//                restaurant2.getId(),
//                "떡볶이 같이 드실 분",
//                "같이 드실 분 구합니다",
//                "서울 강남역",
//                LocalDateTime.of(2022, 1, 1, 12, 00),
//                LocalDateTime.of(2022, 1, 10, 12, 00),
//                LocalDateTime.of(2022, 1, 11, 12, 00),
//                0,
//                5,
//                1
//        );
//        meetingRepository.save(meeting2);
//
//
//        //모임참가 저장
//        meetingParticipate = new MeetingParticipate(meeting1, userDetails1);
//        meetingParticipateRepository.save(meetingParticipate);
//        meetingParticipate = new MeetingParticipate(meeting2, userDetails1);
//        meetingParticipateRepository.save(meetingParticipate);
//
//    }
//    //endregion
//
//    //region 성공케이스
//    @Nested
//    @DisplayName("성공케이스")
//    class success {
//
//        //region 리뷰 리스트
//        @Test
//        @DisplayName("리뷰 리스트")
//        void 리뷰리스트() {
//            // given
//            List<MyReviewResponseDto> myReviewList = new ArrayList<>();
//
//            List<Review> reviewList = reviewRepository.findAllByUser(user,pageable);
//            for (Review review : reviewList) {
//                MyReviewResponseDto myReviewResponseDto = MyReviewResponseDto.builder()
//                        .reviewId(review.getId())
//                        .userId(review.getUser().getId())
//                        .restaurantId(review.getRestaurant().getId())
//                        .image(review.getImage())
//                        .build();
//                myReviewList.add(myReviewResponseDto);
//
//            }
//            assertThat(myReviewList.size()).isEqualTo(2);
//        }
//        //endregion
//
//        //region 좋아요리스트
//        @Test
//        @DisplayName("좋아요 리스트")
//        void 좋아요리스트() {
//
//        List<MeetingParticipate> meetingList = meetingParticipateRepository.findAllByUser(user,pageable);
//        //then
//        assertThat(meetingList.size()).isEqualTo(1);
//    }
//
//        }
//        //endregion
//
//        //region 내가 작성 한 식당
//        @Test
//        @DisplayName("내가 작성 한 식당")
//        void 작성식당() {
//            // given
//
//            //when
//            List<Restaurant> restaurantList = restaurantRepository.findAllByUser( user,pageable);
//
//            //then
//            assertThat(restaurantList.size()).isEqualTo(2);
//        }
//        //endregion
//
//
//    //region 내가 참가 한 모임
//    @Test
//    @DisplayName("내가 참가 한 모임")
//    void 참가모임() {
//        // given
//
//        //when
//        List<MeetingParticipate> meetingList = meetingParticipateRepository.findAllByUser(user,pageable);
//
//        //then
//        assertThat(meetingList.size()).isEqualTo(2);
//    }
//    //endregion
//
//    //region 내 리뷰 조회
//    @Test
//    @DisplayName("내 리뷰 조회")
//    void 리뷰조회() {
//        // given
//
//        //when
//        Review review = reviewRepository.findAllByUserAndId(user, review3.getId());
//        //then
//        assertEquals(review3.getContent(),review.getContent());
//        assertEquals(review3.getReviewLike(),review.getReviewLike());
//        assertEquals(review3.getImage(),review.getImage());
//        assertEquals(review3.getSpicy(),review.getSpicy());
//        assertEquals(review3.getRestaurantTags(),review.getRestaurantTags());
//        assertEquals(review3.getId(),review.getId());
//        assertEquals(review3.getUser(),review.getUser());
//    }
//
//}
//
