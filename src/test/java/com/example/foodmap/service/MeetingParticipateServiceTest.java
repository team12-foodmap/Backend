//
//
//package com.example.foodmap.service;
//
//import com.example.foodmap.dto.meeting.MeetingCreatRequestDto;
//import com.example.foodmap.dto.meeting.ParticipateResponseDto;
//import com.example.foodmap.model.*;
//import com.example.foodmap.repository.MeetingParticipateRepository;
//import com.example.foodmap.repository.MeetingRepository;
//import com.example.foodmap.repository.UserRepository;
//import com.example.foodmap.security.UserDetailsImpl;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//
//import javax.transaction.Transactional;
//import java.time.LocalDateTime;
//
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@Transactional
//@Rollback
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class MeetingParticipateServiceTest {
//
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    MeetingParticipateRepository meetingParticipateRepository;
//    @Autowired
//    MeetingRepository meetingRepository;
//    @Autowired
//    MeetingService meetingService;
//    @Autowired
//    MeetingParticipateService meetingParticipateService;
//
//
//    private Meeting meeting1;
//    private User user1;
//    private User user2;
//    private User user3;
//    private User user4;
//    private MeetingCreatRequestDto meetingCreatRequestDto;
//    private UserDetailsImpl userDetails1;
//    private UserDetailsImpl userDetails2;
//    private UserDetailsImpl userDetails3;
//    private UserDetailsImpl userDetails4;
//    private String meetingTitle;
//    private LocalDateTime startDate;
//    private String restaurant;
//    private LocalDateTime endDate;
//    private LocalDateTime meetingDate;
//    private Location location;
//    private String location1;
//    private Long restaurantId;
//    private int limitPeople;
//    private int nowPeople;
//    private String content;
//    private int viewCount;
//
//
//    @BeforeEach
//    void setup(){
//        location = new Location("강남구",123.231,12.234);
//        user1 = new User(
//                "파이리",
//                "asdf1234",
//                222L,
//                "hanghae99@naver.com",
//                UserRoleEnum.USER,
//                1L,
//                "http://sljlet.com",
//                location,
//                "지우"
//        );
//        user2 = new User(
//                "꼬부기",
//                "asdf1234",
//                152L,
//                "haSLE2@naver.com",
//                UserRoleEnum.USER,
//                241L,
//                "http://sljlet.com",
//                location,
//                "지우"
//        );
//        user3 = new User(
//                "이상해씨",
//                "asdf1234",
//                1315L,
//                "haSLE2@naver.com",
//                UserRoleEnum.USER,
//                12L,
//                "http://sljlet.com",
//                location,
//                "지우"
//        );
//        user4 = new User(
//                "피카츄",
//                "asdf1234",
//                1225L,
//                "haSLE2@naver.com",
//                UserRoleEnum.USER,
//                112L,
//                "http://sljlet.com",
//                location,
//                "지우"
//        );
//
//
//        meetingTitle="악어떡볶기 가실분?";
//        startDate = LocalDateTime.of(2021,12,24,05,00);
//        endDate = LocalDateTime.of(2021,12,25,12,00);
//        meetingDate = LocalDateTime.of(2021,12,28,14,00);
//        restaurant="악어떡볶이";
//        limitPeople=3;
//        location1="강남역";
//        nowPeople=0;
//        content= "졸맛집";
//        restaurantId = 1L;
//        viewCount=1;
//
//        userRepository.save(user1);
//        userRepository.save(user2);
//        userRepository.save(user3);
//        userRepository.save(user4);
//
//
//        userDetails1 = new UserDetailsImpl(user1);
//        userDetails2 = new UserDetailsImpl(user2);
//        userDetails3 = new UserDetailsImpl(user3);
//        userDetails4 = new UserDetailsImpl(user4);
//
//        meetingCreatRequestDto = new MeetingCreatRequestDto(
//                meetingTitle, restaurant, restaurantId, endDate, startDate, meetingDate, location1, limitPeople, nowPeople, content
//        );
//
//
//        meeting1 = new Meeting(user1,restaurant,restaurantId,meetingTitle,content,location1,startDate,endDate,  meetingDate,viewCount,  limitPeople, nowPeople);
//        //모집등록
//        meetingRepository.save(meeting1);
//        MeetingParticipate meetingParticipate1 = new MeetingParticipate(meeting1, userDetails1);
//        meetingParticipateRepository.save(meetingParticipate1);
//        meeting1.addnowPeople();
//
//
//    }
//    @Test
//    @DisplayName("새로운 유저 모집참가")
//    void 모임참가(){
//        //given
//
//
//        //when
//        ParticipateResponseDto participateResponseDto = meetingParticipateService.participateMeeting(meeting1.getId(),userDetails2);
//
//        //then
//        assertThat(participateResponseDto.getUserId()).isEqualTo(userDetails2.getUser().getId());
//
//    }
//
//    @Test
//    @Transactional
//    @DisplayName("기존유저 참가 취소")
//    void 모임참가1(){
//        //given
//
//
//        //기존 참가유저 총 2명
//        MeetingParticipate meetingParticipate2 = new MeetingParticipate(meeting1, userDetails2);
//        meetingParticipateRepository.save(meetingParticipate2);
//        meeting1.addnowPeople();
//
//        //when
//        meetingParticipateService.participateMeeting(meeting1.getId(),userDetails2);
//
//
//
//        //then
//
//
//        assertThat(meeting1.getNowPeople()).isEqualTo(1);
//
//    }
//
//    @Test
//    @DisplayName("초과인원")
//    void 모임참가2(){
//        //given
//        MeetingParticipate meetingParticipate2 = new MeetingParticipate(meeting1, userDetails2);
//        meetingParticipateRepository.save(meetingParticipate2);
//        meeting1.addnowPeople();
//        MeetingParticipate meetingParticipate3 = new MeetingParticipate(meeting1, userDetails3);
//        meetingParticipateRepository.save(meetingParticipate3);
//        meeting1.addnowPeople();
//        //when
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//                () -> meetingParticipateService.participateMeeting(meeting1.getId(), userDetails4));
//
//        //then
//        assertThat(exception.getMessage()).isEqualTo("참가인원이 초과되었습니다.");
//    }
//
//    @Test
//    @Transactional
//    @DisplayName("limit=now 일때 참가취소")
//    void 모임참가3(){
//        //given
//
//        //limit 3명 now 3명
//        MeetingParticipate meetingParticipate2 = new MeetingParticipate(meeting1, userDetails2);
//        meetingParticipateRepository.save(meetingParticipate2);
//        meeting1.addnowPeople();
//        MeetingParticipate meetingParticipate3 = new MeetingParticipate(meeting1, userDetails3);
//        meetingParticipateRepository.save(meetingParticipate3);
//        meeting1.addnowPeople();
//
//
//
//        ParticipateResponseDto participateResponseDto2 = meetingParticipateService.participateMeeting(meeting1.getId(),userDetails2);
//
//
//        //when
//        assertThat(meeting1.getNowPeople()).isEqualTo(2);
//
//    }
//
//
//}
//
