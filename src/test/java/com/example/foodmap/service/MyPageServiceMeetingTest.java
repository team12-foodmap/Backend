package com.example.foodmap.service;

import com.example.foodmap.config.EmbeddedS3Config;
import com.example.foodmap.dto.Restaurant.RestaurantSaveRequestDto;
import com.example.foodmap.dto.mypage.MyMeetingResponseDto;
import com.example.foodmap.model.*;
import com.example.foodmap.repository.*;
import com.example.foodmap.security.UserDetailsImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
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
class MyPageServiceMeetingTest {

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
    private Review review6;
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
                "",
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
                "",
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
                "",
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
                "",
                restaurantLikes2,
                review2,
                2
        );
        restaurantRepository.save(restaurant2);


//        //????????????
        meeting1 = new Meeting(
                user1,
                "???????????????",
                restaurant1.getId(),
                "????????? ?????? ?????? ???",
                "??????????????????",
                "????????? ?????????",
                LocalDateTime.of(2022, 1, 1, 12, 00),
                LocalDateTime.of(2022, 1, 10, 12, 00),
                LocalDateTime.of(2022, 1, 11, 12, 00),
                0,
                5,
                1
        );
        meetingRepository.save(meeting1);

        meeting2 = new Meeting(
                user1,
                "???????????????",
                restaurant2.getId(),
                "????????? ?????? ?????? ???",
                "??????????????????",
                "????????? ?????????",
                LocalDateTime.of(2022, 1, 1, 12, 00),
                LocalDateTime.of(2022, 1, 10, 12, 00),
                LocalDateTime.of(2022, 1, 11, 12, 00),
                0,
                5,
                1
        );
        meetingRepository.save(meeting1);
        meetingRepository.save(meeting2);

        //???????????? ??????
        meetingParticipate = new MeetingParticipate(meeting1, userDetails1);
        meetingParticipateRepository.save(meetingParticipate);
        meetingParticipate = new MeetingParticipate(meeting2, userDetails1);
        meetingParticipateRepository.save(meetingParticipate);

        //?????? ?????????
        reviewLikes1 = new ReviewLikes(1L, user1, review3);
        reviewLikes2 = new ReviewLikes(1L, user2, review3);
        reviewLikesRepository.save(reviewLikes1);
        reviewLikesRepository.save(reviewLikes2);

    }
    //endregion

    //region ???????????????


    //endregion


        //region ?????? ?????? ??? ??????
        @Test
        @DisplayName("?????? ?????? ??? ??????")
        void ????????????() {
            // given
            PageRequest pageable = PageRequest.of(page, size);
            List<MyMeetingResponseDto> myMeetingList = new ArrayList<>();
            //when
            List<MeetingParticipate> meetingList = meetingParticipateRepository.findAllByUser(user1, pageable);

            for (MeetingParticipate meetingParticipate : meetingList) {

                MyMeetingResponseDto myMeetingResponseDto = MyMeetingResponseDto.builder()
                        .meetingId(meetingParticipate.getMeeting().getId())
                        .userId(meetingParticipate.getUser().getId())
                        .startDate(meetingParticipate.getMeeting().getStartDate())
                        .endDate(meetingParticipate.getMeeting().getEndDate())
                        .meetingDate(meetingParticipate.getMeeting().getMeetingDate())
                        .meetingTitle(meetingParticipate.getMeeting().getMeetingTitle())
                        .content(meetingParticipate.getMeeting().getContent())
                        .nowPeople(meetingParticipate.getMeeting().getNowPeople())
                        .limitPeople(meetingParticipate.getMeeting().getLimitPeople())
                        .location(meetingParticipate.getMeeting().getLocation())
                        .build();
                myMeetingList.add(myMeetingResponseDto);
            }
            myPageService.showMyMeeting(user1,page,size);
            //then
            assertThat(meetingList.size()).isEqualTo(2);

    }

    //endregion



}



