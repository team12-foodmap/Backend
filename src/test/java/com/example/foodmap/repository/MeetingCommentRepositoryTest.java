package com.example.foodmap.repository;

import com.example.foodmap.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MeetingCommentRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    MeetingCommentRepository meetingCommentRepository;


    private User user;
    private Meeting meeting;


    @BeforeEach
    void setup(){
        Location location = new Location("강남구",123.231,12.234);
        user = new User(
                "파이리",
                "asdf1234",
                222L,
                "hanghae99@naver.com",
                UserRoleEnum.USER,
                1L,
                "http://sljlet.com",
                location,
                "지우"
        );
        userRepository.save(user);

        String meetingTitle="악어떡볶기 가실분?";
        LocalDateTime startDate = LocalDateTime.of(2021,12,24,05,00);
        LocalDateTime endDate = LocalDateTime.of(2021,12,28,12,00);
        LocalDateTime meetingDate = LocalDateTime.of(2021,12,31,14,00);
        String restaurant="악어떡볶이";
        int limitPeople=5;
        String location1="강남역";
        Long restaurantId = 1L;
        int nowPeople=1;
        String content= "졸맛집";
        int viewCount =1;


        meeting = new Meeting(user,restaurant,restaurantId,meetingTitle,content,location1,startDate,endDate,  meetingDate,viewCount,  limitPeople, nowPeople);
        //user1 유저 정보
        meetingRepository.save(meeting);


        MeetingComment meetingComment1 = new MeetingComment("모임 참가합니다.", user, meeting, null);
        MeetingComment meetingComment2 = new MeetingComment("모임 취소", user, meeting, null);
        meetingCommentRepository.save(meetingComment1);
        meetingCommentRepository.save(meetingComment2);
    }

    @Test
    void saveAndFindAll() {


        //when
        List<MeetingComment> meetingComments = meetingCommentRepository.findAll();

        //then

        assertThat(meetingComments.get(0).getContent()).isEqualTo("모임 참가합니다.");
        assertThat(meetingComments.get(1).getContent()).isEqualTo("모임 취소");
    }


}