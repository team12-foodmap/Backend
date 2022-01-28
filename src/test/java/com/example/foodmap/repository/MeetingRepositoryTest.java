package com.example.foodmap.repository;

import com.example.foodmap.model.*;
import com.example.foodmap.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MeetingRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    MeetingParticipateRepository meetingParticipateRepository;
    @Autowired
    MeetingCommentRepository meetingCommentRepository;


    private User user1;
    private User user2;
    private Meeting meeting1;
    private Meeting meeting2;


    @BeforeEach
    void setup() {
        Location location = new Location("강남구", 123.231, 12.234);
        user1 = new User(
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
        user2 = new User(
                "오나라",
                "azsgzg234",
                11L,
                "hhae99@naver.com",
                UserRoleEnum.USER,
                1L,
                "http://sljlet.com",
                location,
                "꼬북이"
        );
        userRepository.save(user1);
        userRepository.save(user2);
        UserDetailsImpl userDetails1 = new UserDetailsImpl(user1);
        UserDetailsImpl userDetails2 = new UserDetailsImpl(user2);


        String meetingTitle = "악어떡볶기 가실분?";
        LocalDateTime startDate = LocalDateTime.of(2021, 12, 24, 05, 00);
        LocalDateTime endDate = LocalDateTime.of(2021, 12, 28, 12, 00);
        LocalDateTime meetingDate = LocalDateTime.of(2021, 12, 31, 14, 00);
        String restaurant = "악어떡볶이";
        int limitPeople = 5;
        String location1 = "강남역";
        Long restaurantId = 1L;
        int nowPeople = 1;
        String content = "졸맛집";
        int viewCount = 1;


        meeting1 = new Meeting(
                user1,
                restaurant,
                restaurantId,
                meetingTitle,
                content,
                location1,
                startDate,
                endDate,
                meetingDate,
                viewCount,
                limitPeople,
                nowPeople);
        meeting2 = new Meeting(user2, "강남떡볶이", 2L, "강남가자", "여기맛집", location1, startDate, endDate, meetingDate, viewCount, limitPeople, nowPeople);
        //user1 유저 정보
        meetingRepository.save(meeting1);
        meetingRepository.save(meeting2);

    }


    @Test
    void saveAndFindAll() {

        //when
        List<Meeting> result = meetingRepository.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getContent()).isEqualTo("졸맛집");
        assertThat(result.get(1).getContent()).isEqualTo("여기맛집");
    }

    @Test
    void findById() {

        //when
        Optional<Meeting> result = meetingRepository.findById(meeting1.getId());

        //then

        assertThat(result.get().getContent()).isEqualTo("졸맛집");
        assertThat(result.get().getId()).isEqualTo(meeting1.getId());

    }
}