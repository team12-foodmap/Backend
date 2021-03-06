

package com.example.foodmap.service;

import com.example.foodmap.dto.meeting.*;
import com.example.foodmap.exception.CustomException;
import com.example.foodmap.model.*;
import com.example.foodmap.repository.MeetingCommentRepository;
import com.example.foodmap.repository.MeetingRepository;
import com.example.foodmap.repository.UserRepository;
import com.example.foodmap.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@Rollback
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MeetingCommentServiceTest {

    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    MeetingCommentRepository meetingCommentRepository;
    @Autowired
    MeetingService meetingService;
    @Autowired
    MeetingCommentService meetingCommentService;
    @Autowired
    UserRepository userRepository;

    private Meeting meeting1;
    private User user1;
    private User user2;
    private MeetingCreatRequestDto meetingCreatRequestDto;
    private UserDetailsImpl userDetails1;
    private UserDetailsImpl userDetails2;
    private String meetingTitle;
    private LocalDateTime startDate;
    private String restaurant;
    private LocalDateTime endDate;
    private LocalDateTime meetingDate;
    private Location location;
    private String location1;
    private Long restaurantId;
    private int limitPeople;
    private int nowPeople;
    private String content;
    private int viewCount;


    @BeforeEach
    void setup() {
        location = new Location("?????????", 123.231, 12.234);
        user1 = new User(
                "?????????",
                "asdf1234",
                222L,
                "hanghae99@naver.com",
                UserRoleEnum.USER,
                1L,
                "http://sljlet.com",
                location,
                "??????"
        );
        user2 = new User(
                "?????????",
                "asdf1234",
                152L,
                "haSLE2@naver.com",
                UserRoleEnum.USER,
                241L,
                "http://sljlet.com",
                location,
                "??????"
        );


        meetingTitle = "??????????????? ??????????";
        startDate = LocalDateTime.of(2021, 12, 24, 05, 00);
        endDate = LocalDateTime.of(2021, 12, 25, 12, 00);
        meetingDate = LocalDateTime.of(2021, 12, 28, 14, 00);
        restaurant = "???????????????";
        limitPeople = 3;
        location1 = "?????????";
        nowPeople = 1;
        restaurantId = 1L;
        content = "?????????";
        viewCount=1;
        userRepository.save(user1);
        userRepository.save(user2);

        userDetails1 = new UserDetailsImpl(user1);
        userDetails2 = new UserDetailsImpl(user2);

        meetingCreatRequestDto = new MeetingCreatRequestDto(
                meetingTitle, restaurant, restaurantId, endDate, startDate, meetingDate, location1, limitPeople, nowPeople, content
        );


        meeting1 = new Meeting(user1,restaurant,restaurantId,meetingTitle,content,location1,startDate,endDate,  meetingDate,viewCount,  limitPeople, nowPeople);
        //????????????
        meetingRepository.save(meeting1);
        MeetingCommentCreateRequestDto meetingCommentCreateRequestDto =
                new MeetingCommentCreateRequestDto("?????? ???????????????.", null);

        MeetingComment meetingComment1 = new MeetingComment(meetingCommentCreateRequestDto.getContent(), user1, meeting1, null);
        meetingCommentRepository.save(meetingComment1);


    }

    @Test
    @DisplayName("??? ?????? ??????")
    void ????????????() {
        //given
        MeetingCommentCreateRequestDto meetingCommentCreateRequestDto =
                new MeetingCommentCreateRequestDto("?????? ???????????????.", null);

        //when
        meetingCommentService.createComment(meetingCommentCreateRequestDto, meeting1.getId(), userDetails1);

        //then
        List<MeetingComment> result = meetingCommentRepository.findAll();

        assertThat(result.get(0).getContent()).isEqualTo("?????? ???????????????.");


    }

    @Test
    @DisplayName("???????????? ??? ?????? ?????? ?????? ?????? -??????")
    void ????????????0() {
        //given
        MeetingCommentCreateRequestDto meetingCommentCreateRequestDto =
                new MeetingCommentCreateRequestDto("?????? ???????????????.", 10000L);

        //when
        ;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            meetingCommentService.createComment(meetingCommentCreateRequestDto, meeting1.getId(), userDetails1);
        });
        //then
        assertThat(exception.getMessage()).isEqualTo(null);
    }

    @Test
    @DisplayName("?????? content null-??????")
    void ????????????1() {
        //given
        content = null;
        MeetingCommentCreateRequestDto meetingCommentCreateRequestDto =
                new MeetingCommentCreateRequestDto(content, null);

        //when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new MeetingComment(meetingCommentCreateRequestDto.getContent(), user1, meeting1, null);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("??????????????? ?????? ?????? ??? ?????????.");
    }

    @Test
    @DisplayName("????????? null- ??????")
    void ????????????2() {
        //given
        meeting1 = null;
        MeetingCommentCreateRequestDto meetingCommentCreateRequestDto =
                new MeetingCommentCreateRequestDto(content, 1L);
        //when
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new MeetingComment(meetingCommentCreateRequestDto.getContent(), user1, meeting1, null);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("???????????? ?????? ??????????????????.");
    }


    @Test
    @DisplayName("?????? ??????")
    void ????????????() {
        //given
        MeetingCommentCreateRequestDto meetingCommentCreateRequestDto =
                new MeetingCommentCreateRequestDto("?????? ???????????????.", null);

        MeetingComment meetingComment = new MeetingComment(meetingCommentCreateRequestDto.getContent(), user1, meeting1, null);
        meetingCommentRepository.save(meetingComment);

        MeetingUpdateRequestDto updateCommentDto =
                new MeetingUpdateRequestDto("?????????????");
        //when
        meetingCommentService.updateComment(updateCommentDto, meetingComment.getId(), userDetails1);

        //then
        assertThat(meetingComment.getContent()).isEqualTo("?????????????");
    }

    @Test
    @DisplayName("?????? ??????-????????????")
    void ????????????1() {
        //given
        MeetingCommentCreateRequestDto meetingCommentCreateRequestDto =
                new MeetingCommentCreateRequestDto("?????? ???????????????.", null);

        MeetingComment meetingComment = new MeetingComment(meetingCommentCreateRequestDto.getContent(), user1, meeting1, null);
        meetingCommentRepository.save(meetingComment);

        MeetingUpdateRequestDto updateCommentDto =
                new MeetingUpdateRequestDto("?????????????");
        //when
        CustomException exception = assertThrows(CustomException.class, () -> {
            meetingCommentService.updateComment(updateCommentDto, meetingComment.getId(), userDetails2);
        });

        assertThat(exception.getErrorCode().getDetail()).isEqualTo("????????? ??? ?????? ????????? ????????????.");
    }



    @Test
    @DisplayName("???????????? ??????")
    void ????????????() {
        //given

        /*
         * 1
         *  2
         *   4
         *    6
         *   5
         *  3
         *   7
         * 8 ??? ?????? ??????
         */


        MeetingComment comment1 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("1???", null), meeting1.getId(), userDetails1);
        MeetingComment comment2 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("2???", comment1.getId()), meeting1.getId(), userDetails1);
        MeetingComment comment3 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("3???", comment1.getId()), meeting1.getId(), userDetails1);
        MeetingComment comment4 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("4???", comment2.getId()), meeting1.getId(), userDetails1);
        MeetingComment comment5 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("5???", comment2.getId()), meeting1.getId(), userDetails1);
        MeetingComment comment6 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("6???", comment3.getId()), meeting1.getId(), userDetails1);
        MeetingComment comment7 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("7???", comment4.getId()), meeting1.getId(), userDetails1);
        MeetingComment comment8 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("8???", null), meeting1.getId(), userDetails1);


        //when
        MeetingDetailResponseDto meeting = meetingService.getMeeting(meeting1.getId(), userDetails1);
        List<MeetingCommentResponseDto> result = meeting.getComment();
        //then
        assertThat(meeting.getComment().size()).isEqualTo(2); // ????????? ??????
        assertThat(result.get(0).getChildren().size()).isEqualTo(2); // 1??? children
        assertThat(result.get(0).getChildren().get(0).getChildren().size()).isEqualTo(2); // 2??? children
        assertThat(result.get(0).getChildren().get(0).getChildren().get(0).getChildren().size()).isEqualTo(1); // 4??? children
        assertThat(result.get(0).getChildren().get(0).getChildren().get(0)
                .getChildren().get(0).getChildren().size()).isEqualTo(0); // 6??? children
        assertThat(result.get(0).getChildren().get(0).getChildren().get(1).getChildren().size()).isEqualTo(0); // 5??? children
        assertThat(result.get(0).getChildren().get(1).getChildren().size()).isEqualTo(1); // 1??? children
        assertThat(result.get(0).getChildren().get(1).getChildren().get(0).getChildren().size()).isEqualTo(0); // 7??? children
        assertThat(result.get(1).getChildren().size()).isEqualTo(0); // 8??? children
    }

    @Test
    @DisplayName("???????????? ??????")
    void ????????????1() {
        //given

        /*
         * 1
         *  2 2?????? ??????
         *   4
         *  3
         */


        MeetingComment comment1 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("1???", null), meeting1.getId(), userDetails1);
        MeetingComment comment2 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("2???", comment1.getId()), meeting1.getId(), userDetails2);
        MeetingComment comment3 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("3???", comment1.getId()), meeting1.getId(), userDetails1);
        MeetingComment comment4 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("4???", comment2.getId()), meeting1.getId(), userDetails1);

        MeetingUpdateRequestDto meetingUpdateRequestDto = new MeetingUpdateRequestDto("?????????2???");

        //when
        meetingCommentService.updateComment(meetingUpdateRequestDto, comment2.getId(), userDetails2);

        //then
        assertThat(comment2.getContent()).isEqualTo("?????????2???");
        assertThat(comment1.getContent()).isEqualTo("1???");
        assertThat(comment3.getContent()).isEqualTo("3???");
        assertThat(comment4.getContent()).isEqualTo("4???");


    }
    @Test
    @DisplayName("???????????? ??????")
    void ????????????() {
        //given

        MeetingComment meetingcomment2 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("1???", null), meeting1.getId(), userDetails1);
        MeetingComment meetingComment3 = new MeetingComment("?????????????",user1,meeting1,null);
        meetingCommentRepository.save(meetingComment3);

        //when
        Long deleteId = meetingCommentService.deleteComment(meetingComment3.getId(), userDetails1);

        //then
        Optional<MeetingComment> comment = meetingCommentRepository.findById(deleteId);
        List<MeetingComment> result = meetingCommentRepository.findAll();
        assertTrue(comment.isEmpty());
        assertThat(result.get(0).getContent()).isEqualTo("?????? ???????????????.");
        assertThat(result.get(1).getContent()).isEqualTo("1???");

    }

    @Test
    @DisplayName("???????????? ??????-?????? ??????")
    void ????????????1() {
        MeetingCommentCreateRequestDto meetingCommentCreateRequestDto =
                new MeetingCommentCreateRequestDto("?????? ???????????????.", null);

        MeetingComment meetingComment = new MeetingComment(meetingCommentCreateRequestDto.getContent(), user1, meeting1, null);
        meetingCommentRepository.save(meetingComment);

        //when
        CustomException exception = assertThrows(CustomException.class, () -> {
            meetingCommentService.deleteComment(meetingComment.getId(), userDetails2);
        });

        assertThat(exception.getErrorCode().getDetail()).isEqualTo("????????? ??? ?????? ????????? ????????????.");
    }

    @Test
    @DisplayName("???????????? ????????? ???????????? ?????? ??????")
    void ????????????3() {
        //given
        /*
         * 1
         *  2 2???????????? 4????????? ??????
         *   4
         *  3
         */

        MeetingComment comment1 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("1???", null), meeting1.getId(), userDetails1);
        MeetingComment comment2 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("2???", comment1.getId()), meeting1.getId(), userDetails2);
        MeetingComment comment3 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("3???", comment1.getId()), meeting1.getId(), userDetails1);
        MeetingComment comment4 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("4???", comment2.getId()), meeting1.getId(), userDetails1);


        //when
        Long deleteId = meetingCommentService.deleteComment(comment2.getId(), userDetails2);


        //then

        Optional<MeetingComment> comment = meetingCommentRepository.findById(deleteId);

        assertTrue(comment.isEmpty());


    }
}

