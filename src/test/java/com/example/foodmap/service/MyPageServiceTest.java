//package com.example.foodmap.service;
//ㅁ
//import com.example.foodmap.dto.Restaurant.RestaurantSaveRequestDto;
//import com.example.foodmap.dto.meeting.MeetingCreatRequestDto;
//import com.example.foodmap.dto.mypage.MyReviewResponseDto;
//import com.example.foodmap.dto.review.ReviewRequestDto;
//import com.example.foodmap.model.*;
//import com.example.foodmap.repository.*;
//import com.example.foodmap.security.UserDetailsImpl;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.transaction.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Transactional
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
//    private User user1;
//    private User user2;
//    private UserDetailsImpl userDetails1;
//    private UserDetailsImpl userDetails2;
//    private Restaurant restaurant1;
//    private Restaurant restaurant2;
//    private Review review1;
//    private Review review2;
//    private Meeting meeting1;
//    private Meeting meeting2;
//    private RestaurantLikes restaurantLikes1;
//    private RestaurantLikes restaurantLikes2;
//    private RestaurantLikes restaurantLikes3;
//    private MeetingParticipate meetingParticipate;
//
//    //region 사전세팅
//    @BeforeEach
//    void setup() {
//        //유저위치 & 유저 저장
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
//        Location location2 = new Location("강북구", 34.31, 34.21);
//        user1 = new User(
//                "이한울",
//                "a111",
//                2L,
//                "a@bs.com",
//                UserRoleEnum.USER,
//                2L,
//                "default.png",
//                location2,
//                "별명이 뭔가요"
//        );
//        userRepository.save(user2);
//        userDetails2 = new UserDetailsImpl(user2);
//
//        //식당 저장
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
//        restaurant2 = new Restaurant(restaurantSaveRequestDto2, "default.png", user2, location2);
//
//        //식당 찜하기저장
//        restaurantLikes1 = new RestaurantLikes(user1, restaurant1);
//        restaurantLikesRepository.save(restaurantLikes1);
//        restaurantLikes2 = new RestaurantLikes(user2, restaurant1);
//        restaurantLikesRepository.save(restaurantLikes1);
//        restaurantLikes3 = new RestaurantLikes(user1, restaurant2);
//        restaurantLikesRepository.save(restaurantLikes3);
//
//        //리뷰저장
//        ReviewRequestDto reviewRequestDto1 = new ReviewRequestDto(
//                "이집 잘하네요.",
//                1,
//                "정말 맛있어요!",
//                null
//        );
//        review1 = new Review(reviewRequestDto1, user1, restaurant1);
//        reviewRepository.save(review1);
//
//        ReviewRequestDto reviewRequestDto2 = new ReviewRequestDto(
//                "이집 별로네요.",
//                100,
//                "정말 매워요!",
//                null
//        );
//        review2 = new Review(reviewRequestDto2, user2, restaurant2);
//        reviewRepository.save(review2);
//
//        //모임저장
//        MeetingCreatRequestDto meetingCreatRequestDto1 = new MeetingCreatRequestDto(
//                "떡볶이 같이 드실 분",
//                LocalDateTime.of(2022, 1, 1, 12, 00),
//                "엽떡",
//                LocalDateTime.of(2022, 1, 10, 12, 00),
//                LocalDateTime.of(2022, 1, 11, 12, 00),
//                location1,
//                5,
//                1,
//                "엽떡 맛있게 드실분 구합니다."
//        );
//        meeting1 = new Meeting(user1, meetingCreatRequestDto1);
//        meetingRepository.save(meeting1);
//
//        MeetingCreatRequestDto meetingCreatRequestDto2 = new MeetingCreatRequestDto(
//                "튀김 같이 드실 분",
//                LocalDateTime.of(2022, 1, 10, 12, 00),
//                "엽튀",
//                LocalDateTime.of(2022, 1, 20, 12, 00),
//                LocalDateTime.of(2022, 1, 31, 12, 00),
//                location1,
//                20,
//                1,
//                "튀김 맛있게 드실분 구합니다."
//        );
//        meeting2 = new Meeting(user2, meetingCreatRequestDto2);
//        meetingRepository.save(meeting2);
//
//        //모임참가 저장
//        meetingParticipate = new MeetingParticipate(meeting1, userDetails2);
//        meetingParticipateRepository.save(meetingParticipate);
//
//    }
//    //endregion
//
//    //region 성공케이스
//    @Nested
//    @DisplayName("성공케이스")
//    class sucess {
//        @Test
//        @DisplayName("리뷰 리스트")
//        void 리뷰리스트() {
//            //given
//            List<MyReviewResponseDto> myReviewList = new ArrayList<>();
//            MyReviewResponseDto myReviewResponseDto = MyReviewResponseDto.builder()
//                    .reviewId(review1.getId())
//                    .userId(restaurant1.getUser().getId())
//                    .restaurantId(review1.getRestaurant().getId())
//                    .content(review1.getContent())
//                    .spicy(review1.getSpicy())
//                    .restaurantTags(review1.getRestaurantTags())
//                    .image(review1.getImage())
//                    .createdAt(review1.getCreatedAt())
//                    .modifiedAt(review1.getModifiedAt())
//                    .build();
//            myReviewList.add(myReviewResponseDto);
//
//        }
//        //when
//
//    }
//}
////endregion
//
//
//}