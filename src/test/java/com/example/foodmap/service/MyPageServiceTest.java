/*
package com.example.foodmap.service;

import com.example.foodmap.dto.Restaurant.RestaurantReviewResponseDto;
import com.example.foodmap.dto.Restaurant.RestaurantSaveRequestDto;
import com.example.foodmap.dto.meeting.MeetingCreatRequestDto;
import com.example.foodmap.dto.mypage.MyLikeResponseDto;
import com.example.foodmap.dto.mypage.MyReviewResponseDto;
import com.example.foodmap.dto.review.ReviewRequestDto;
import com.example.foodmap.model.*;
import com.example.foodmap.repository.*;
import com.example.foodmap.security.UserDetailsImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
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
    private RestaurantSaveRequestDto restaurantSaveRequestDto;
    private List<Review> review1;
    private List<Review> review2;
    private Review review3;
    private Review review4;
    private Review review5;
    private Review review6;
    private Meeting meeting1;
    private Meeting meeting2;
    private List<RestaurantLikes> restaurantLikes1;
    private List<RestaurantLikes> restaurantLikes2;
    private RestaurantLikes restaurantLikes3;
    private RestaurantLikes restaurantLikes4;
    private RestaurantLikes restaurantLikes5;
    private MeetingParticipate meetingParticipate;
    private Pageable pageable;

    //region ????????????
    @BeforeEach
    void setup() {
        //???????????? & ?????? ??????
        Location location1 = new Location("?????????", 12.23, 34.21);
        user1 = new User(
                "?????????",
                "a111",
                100L,
                "a@b.com",
                UserRoleEnum.USER,
                1L,
                null,
                location1,
                "????????????"
        );
        userRepository.save(user1);
        userDetails1 = new UserDetailsImpl(user1);

        Location location2 = new Location("?????????", 34.31, 34.21);
        user2 = new User(
                "?????????",
                "a111",
                200L,
                "a@bs.com",
                UserRoleEnum.USER,
                2L,
                null,
                location2,
                "????????? ?????????"
        );
        userRepository.save(user2);
        userDetails2 = new UserDetailsImpl(user2);

       restaurant1 = new Restaurant(
               1L,
               user1,
               "??????1",
               location1,
               "????????????",
               "????????????",
               "?????????",
               "??????",
               null,
               restaurantLikes1,
               review1,
               3
       );
        restaurantRepository.save(restaurant1);

        restaurant2 = new Restaurant(
                2L,
                user2,
                "??????2",
                location2,
                "????????????",
                "????????????",
                "?????????",
                "??????",
                null,
                restaurantLikes2,
                review2,
                2
        );
        restaurantRepository.save(restaurant2);


//        //?????? ??????
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
//        restaurant1 = new Restaurant(restaurantSaveRequestDto1, null, user1, location1);
//        restaurantRepository.save(restaurant1);
//
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
//        restaurant2 = new Restaurant(restaurantSaveRequestDto2, null, user1, location1);

        //?????? ???????????????

//        restaurantLikes3 = new RestaurantLikes(restaurantLikes3.getId(),user1, restaurant2);
//        restaurantLikesRepository.save(restaurantLikes3);

        //????????????
        review3 = new Review(
                1L,
                "?????????????????? ????????????!",
                3,
                1,
                null,
                user1,
                restaurant1,
                3,
                null
        );
        reviewRepository.save(review3);
        review4 = new Review(
                2L,
                "?????????????????? ????????????!",
                3,
                1,
                null,
                user1,
                restaurant1,
                3,
                null
        );
        reviewRepository.save(review4);
        review5 = new Review(
                3L,
                "?????????????????? ????????????!",
                3,
                1,
                null,
                user2,
                restaurant1,
                3,
                null
        );
        reviewRepository.save(review5);

        //?????????
        restaurantLikes3 = new RestaurantLikes(
                1L,
                user2,
                restaurant1
        );
        restaurantLikesRepository.save(restaurantLikes3);

//        restaurantLikes4 = new RestaurantLikes(
//                2L,
//                user1,
//                restaurant2
//        );
//        restaurantLikesRepository.save(restaurantLikes4);

//        restaurantLikes5 = new RestaurantLikes(
//                3L,
//                user2,
//                restaurant1
//        );
//        restaurantLikesRepository.save(restaurantLikes5);

//        //????????????
//        MeetingCreatRequestDto meetingCreatRequestDto1 = new MeetingCreatRequestDto(
//                "????????? ?????? ?????? ???",
//                LocalDateTime.of(2022, 1, 1, 12, 00),
//                "???????????????",
//                restaurant1.getId(),
//                LocalDateTime.of(2022, 1, 10, 12, 00),
//                LocalDateTime.of(2022, 1, 11, 12, 00),
//                "??????????????? ?????????",
//                5,
//                1,
//                "?????? ????????? ????????? ????????????."
//        );
//        meeting1 = new Meeting(user1, meetingCreatRequestDto1);
//        meetingRepository.save(meeting1);
//
//        MeetingCreatRequestDto meetingCreatRequestDto2 = new MeetingCreatRequestDto(
//                "?????? ?????? ?????? ???",
//                LocalDateTime.of(2022, 1, 10, 12, 00),
//                "??????",
//                restaurant2.getId(),
//                LocalDateTime.of(2022, 1, 20, 12, 00),
//                LocalDateTime.of(2022, 1, 31, 12, 00),
//                "???????????? ?????????",
//                20,
//                1,
//                "?????? ????????? ????????? ????????????."
//        );
//        meeting2 = new Meeting(user2, meetingCreatRequestDto2);
//        meetingRepository.save(meeting2);
//
//        //???????????? ??????
//        meetingParticipate = new MeetingParticipate(meeting1, userDetails1);
//        meetingParticipateRepository.save(meetingParticipate);
//        meetingParticipate = new MeetingParticipate(meeting2, userDetails1);
//        meetingParticipateRepository.save(meetingParticipate);

    }
    //endregion

    //region ???????????????
    @Nested
    @DisplayName("???????????????")
    class success {

        //region ?????? ?????????
        @Test
        @DisplayName("?????? ?????????")
        void ???????????????() {
            // given
            List<MyReviewResponseDto> myReviewList = new ArrayList<>();

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

        //region ??????????????????
        @Test
        @DisplayName("????????? ?????????")
        void ??????????????????() {
            // given
            List<MyLikeResponseDto> myLikeList = new ArrayList<>();
            //when
            List<RestaurantLikes> restaurantLikesList = restaurantLikesRepository.findAllByUser(user2,pageable);
            for(RestaurantLikes restaurantLikes : restaurantLikesList) {
                MyLikeResponseDto myLikeResponseDto = MyLikeResponseDto.builder()
                        .restaurantLikeId(restaurantLikes.getId())
                        .restaurantId(restaurantLikes.getRestaurant().getId())
                        .restaurantName(restaurantLikes.getRestaurant().getRestaurantName())
                        .location(restaurantLikes.getRestaurant().getLocation())
                        .fried(restaurantLikes.getRestaurant().getFried())
                        .sundae(restaurantLikes.getRestaurant().getSundae())
                        .tteokbokkiType(restaurantLikes.getRestaurant().getTteokbokkiType())
                        .spicy(restaurantLikes.getRestaurant().getReviews().get(1).getSpicy())
                        .restaurantLikesCount(restaurantLikes.getRestaurant().getRestaurantLikesCount())
                        .image(restaurantLikes.getRestaurant().getImage())
                        .build();
                myLikeList.add(myLikeResponseDto);
            }
            //then
            assertThat(myLikeList.size()).isEqualTo(1);

        }
        //endregion

        //region ?????? ?????? ??? ??????
        @Test
        @DisplayName("?????? ?????? ??? ??????")
        void ????????????() {
            // given

            //when
            List<Restaurant> restaurantList = restaurantRepository.findAllByUser(user1,pageable);

            //then
            assertThat(restaurantList.size()).isEqualTo(2);
        }
        //endregion
    }

    //region ?????? ?????? ??? ??????
    @Test
    @DisplayName("?????? ?????? ??? ??????")
    void ????????????() {
        // given

        //when
        List<MeetingParticipate> meetingList = meetingParticipateRepository.findAllByUser(user1,pageable);

        //then
        assertThat(meetingList.size()).isEqualTo(2);
    }
    //endregion

    //region ??? ?????? ??????
//    @Test
//    @DisplayName("??? ?????? ??????")
//    void ????????????() {
//        // given
//
//        //when
//        Review review = reviewRepository.findAllByUserAndId(user1, review1.getId());
//        //then
//        assertEquals(review1.getContent(),review.getContent());
//        assertEquals(review1.getReviewLike(),review.getReviewLike());
//        assertEquals(review1.getImage(),review.getImage());
//        assertEquals(review1.getSpicy(),review.getSpicy());
//        assertEquals(review1.getRestaurantTags(),review.getRestaurantTags());
//        assertEquals(review1.getId(),review.getId());
//        assertEquals(review1.getUser(),review.getUser());
//    }

}
*/
