//
//
//package com.example.foodmap.service;
//
//import com.example.foodmap.dto.meeting.*;
//import com.example.foodmap.exception.CustomException;
//import com.example.foodmap.model.*;
//import com.example.foodmap.repository.MeetingCommentRepository;
//import com.example.foodmap.repository.MeetingRepository;
//import com.example.foodmap.repository.UserRepository;
//import com.example.foodmap.security.UserDetailsImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import javax.transaction.Transactional;
//import java.time.LocalDateTime;
//import java.util.List;
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@Transactional
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class MeetingCommentServiceTest {
//
//    @Autowired
//    MeetingRepository meetingRepository;
//    @Autowired
//    MeetingCommentRepository meetingCommentRepository;
//    @Autowired
//    MeetingService meetingService;
//    @Autowired
//    MeetingCommentService meetingCommentService;
//    @Autowired
//    UserRepository userRepository;
//
//    private Meeting meeting1;
//    private User user1;
//    private User user2;
//    private MeetingCreatRequestDto meetingCreatRequestDto;
//    private UserDetailsImpl userDetails1;
//    private UserDetailsImpl userDetails2;
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
//    void setup() {
//        location = new Location("강남구", 123.231, 12.234);
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
//
//
//        meetingTitle = "악어떡볶기 가실분?";
//        startDate = LocalDateTime.of(2021, 12, 24, 05, 00);
//        endDate = LocalDateTime.of(2021, 12, 25, 12, 00);
//        meetingDate = LocalDateTime.of(2021, 12, 28, 14, 00);
//        restaurant = "악어떡볶이";
//        limitPeople = 3;
//        location1 = "강남역";
//        nowPeople = 1;
//        restaurantId = 1L;
//        content = "졸맛집";
//        viewCount=1;
//        userRepository.save(user1);
//        userRepository.save(user2);
//
//        userDetails1 = new UserDetailsImpl(user1);
//        userDetails2 = new UserDetailsImpl(user2);
//
//        meetingCreatRequestDto = new MeetingCreatRequestDto(
//                meetingTitle, restaurant, restaurantId, endDate, startDate, meetingDate, location1, limitPeople, nowPeople, content
//        );
//
//
//        meeting1 = new Meeting(user1,restaurant,restaurantId,meetingTitle,content,location1,startDate,endDate,  meetingDate,viewCount,  limitPeople, nowPeople);
//        //모집등록
//        meetingRepository.save(meeting1);
//
//
//    }
//
//    @Test
//    @DisplayName("첫 댓글 등록")
//    void 댓글등록() {
//        //given
//        MeetingCommentCreateRequestDto meetingCommentCreateRequestDto =
//                new MeetingCommentCreateRequestDto("모임 참가합니다.", null);
//
//        //when
//        meetingCommentService.createComment(meetingCommentCreateRequestDto, meeting1.getId(), userDetails1);
//
//        //then
//
//
//    }
//
//    @Test
//    @DisplayName("댓글등록 시 부모 댓글 없는 경우 -실패")
//    void 댓글등록0() {
//        //given
//        MeetingCommentCreateRequestDto meetingCommentCreateRequestDto =
//                new MeetingCommentCreateRequestDto("모임 참가합니다.", 10000L);
//
//        //when
//        ;
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            meetingCommentService.createComment(meetingCommentCreateRequestDto, meeting1.getId(), userDetails1);
//        });
//        //then
//        assertThat(exception.getMessage()).isEqualTo(null);
//    }
//
//    @Test
//    @DisplayName("댓글 content null-실패")
//    void 댓글등록1() {
//        //given
//        content = null;
//        MeetingCommentCreateRequestDto meetingCommentCreateRequestDto =
//                new MeetingCommentCreateRequestDto(content, null);
//
//        //when
//        Exception exception = assertThrows(NullPointerException.class, () -> {
//            new MeetingComment(meetingCommentCreateRequestDto.getContent(), user1, meeting1, null);
//        });
//
//        //then
//        assertThat(exception.getMessage()).isEqualTo("댓글내용은 필수 입력 값 입니다.");
//    }
//
//    @Test
//    @DisplayName("게시글 null- 실패")
//    void 댓글등록2() {
//        //given
//        meeting1 = null;
//        MeetingCommentCreateRequestDto meetingCommentCreateRequestDto =
//                new MeetingCommentCreateRequestDto(content, 1L);
//        //when
//        Exception exception = assertThrows(NullPointerException.class, () -> {
//            new MeetingComment(meetingCommentCreateRequestDto.getContent(), user1, meeting1, null);
//        });
//
//        //then
//        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 게시물입니다.");
//    }
//
//
//    @Test
//    @DisplayName("댓글 수정")
//    void 댓글수정() {
//        //given
//        MeetingCommentCreateRequestDto meetingCommentCreateRequestDto =
//                new MeetingCommentCreateRequestDto("모임 참가합니다.", null);
//
//        MeetingComment meetingComment = new MeetingComment(meetingCommentCreateRequestDto.getContent(), user1, meeting1, null);
//        meetingCommentRepository.save(meetingComment);
//
//        MeetingUpdateRequestDto updateCommentDto =
//                new MeetingUpdateRequestDto("바뀌나요?");
//        //when
//        meetingCommentService.updateComment(updateCommentDto, meetingComment.getId(), userDetails1);
//
//        //then
//        assertThat(meetingComment.getContent()).isEqualTo("바뀌나요?");
//    }
//
//    @Test
//    @DisplayName("댓글 수정-권한없음")
//    void 댓글수정1() {
//        //given
//        MeetingCommentCreateRequestDto meetingCommentCreateRequestDto =
//                new MeetingCommentCreateRequestDto("모임 참가합니다.", null);
//
//        MeetingComment meetingComment = new MeetingComment(meetingCommentCreateRequestDto.getContent(), user1, meeting1, null);
//        meetingCommentRepository.save(meetingComment);
//
//        MeetingUpdateRequestDto updateCommentDto =
//                new MeetingUpdateRequestDto("바뀌나요?");
//        //when
//        CustomException exception = assertThrows(CustomException.class, () -> {
//            meetingCommentService.updateComment(updateCommentDto, meetingComment.getId(), userDetails2);
//        });
//
//        assertThat(exception.getErrorCode().getDetail()).isEqualTo("수정할 수 있는 권한이 없습니다.");
//    }
//
//
//
//    @Test
//    @DisplayName("계층댓글 조회")
//    void 계층댓글() {
//        //given
//
//        /*
//         * 1
//         *  2
//         *   4
//         *    6
//         *   5
//         *  3
//         *   7
//         * 8 의 댓글 구조
//         */
//
//
//        MeetingComment comment1 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("1번", null), meeting1.getId(), userDetails1);
//        MeetingComment comment2 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("2번", comment1.getId()), meeting1.getId(), userDetails1);
//        MeetingComment comment3 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("3번", comment1.getId()), meeting1.getId(), userDetails1);
//        MeetingComment comment4 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("4번", comment2.getId()), meeting1.getId(), userDetails1);
//        MeetingComment comment5 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("5번", comment2.getId()), meeting1.getId(), userDetails1);
//        MeetingComment comment6 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("6번", comment3.getId()), meeting1.getId(), userDetails1);
//        MeetingComment comment7 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("7번", comment4.getId()), meeting1.getId(), userDetails1);
//        MeetingComment comment8 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("8번", null), meeting1.getId(), userDetails1);
//
//
//        //when
//        MeetingDetailResponseDto meeting = meetingService.getMeeting(meeting1.getId(), userDetails1);
//        List<MeetingCommentResponseDto> result = meeting.getComment();
//        //then
//        assertThat(meeting.getComment().size()).isEqualTo(2); // 최상위 댓글
//        assertThat(result.get(0).getChildren().size()).isEqualTo(2); // 1의 children
//        assertThat(result.get(0).getChildren().get(0).getChildren().size()).isEqualTo(2); // 2의 children
//        assertThat(result.get(0).getChildren().get(0).getChildren().get(0).getChildren().size()).isEqualTo(1); // 4의 children
//        assertThat(result.get(0).getChildren().get(0).getChildren().get(0)
//                .getChildren().get(0).getChildren().size()).isEqualTo(0); // 6의 children
//        assertThat(result.get(0).getChildren().get(0).getChildren().get(1).getChildren().size()).isEqualTo(0); // 5의 children
//        assertThat(result.get(0).getChildren().get(1).getChildren().size()).isEqualTo(1); // 1의 children
//        assertThat(result.get(0).getChildren().get(1).getChildren().get(0).getChildren().size()).isEqualTo(0); // 7의 children
//        assertThat(result.get(1).getChildren().size()).isEqualTo(0); // 8의 children
//    }
//
//    @Test
//    @DisplayName("계층댓글 수정")
//    void 계층댓글1() {
//        //given
//
//        /*
//         * 1
//         *  2 2번만 수정
//         *   4
//         *  3
//         */
//
//
//        MeetingComment comment1 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("1번", null), meeting1.getId(), userDetails1);
//        MeetingComment comment2 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("2번", comment1.getId()), meeting1.getId(), userDetails2);
//        MeetingComment comment3 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("3번", comment1.getId()), meeting1.getId(), userDetails1);
//        MeetingComment comment4 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("4번", comment2.getId()), meeting1.getId(), userDetails1);
//
//        MeetingUpdateRequestDto meetingUpdateRequestDto = new MeetingUpdateRequestDto("수정된2번");
//
//        //when
//        meetingCommentService.updateComment(meetingUpdateRequestDto, comment2.getId(), userDetails2);
//
//        //then
//        assertThat(comment2.getContent()).isEqualTo("수정된2번");
//        assertThat(comment1.getContent()).isEqualTo("1번");
//        assertThat(comment3.getContent()).isEqualTo("3번");
//        assertThat(comment4.getContent()).isEqualTo("4번");
//
//
//    }
//    @Test
//    @DisplayName("계층댓글 삭제")
//    void 댓글삭제() {
//        //given
//        MeetingCommentCreateRequestDto meetingCommentCreateRequestDto =
//                new MeetingCommentCreateRequestDto("모임 참가합니다.", null);
//        MeetingComment comment1 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("1번", null), meeting1.getId(), userDetails1);
//        MeetingComment meetingComment = new MeetingComment(1L,user1,meeting1,meetingCommentCreateRequestDto.getContent(),comment1,null);
//        meetingCommentRepository.save(meetingComment);
//
//        //when
//        CustomException exception = assertThrows(CustomException.class, () -> {
//            meetingCommentService.deleteComment(meetingComment.getId(), userDetails1);
//        });
//        //then
//        assertThat(exception.getErrorCode().getDetail()).isEqualTo("해당 댓글을 찾을 수 없습니다.");
//
//    }
//
//    @Test
//    @DisplayName("계층댓글 삭제-권한 없음")
//    void 댓글삭제1() {
//        MeetingCommentCreateRequestDto meetingCommentCreateRequestDto =
//                new MeetingCommentCreateRequestDto("모임 참가합니다.", null);
//
//        MeetingComment meetingComment = new MeetingComment(meetingCommentCreateRequestDto.getContent(), user1, meeting1, null);
//        meetingCommentRepository.save(meetingComment);
//
//        //when
//        CustomException exception = assertThrows(CustomException.class, () -> {
//            meetingCommentService.deleteComment(meetingComment.getId(), userDetails2);
//        });
//
//        assertThat(exception.getErrorCode().getDetail()).isEqualTo("삭제할 수 있는 권한이 없습니다.");
//    }
//
//    @Test
//    @DisplayName("부모댓글 삭제시 자식댓글 같이 삭제")
//    void 댓글삭제3() {
//        //given
//        /*
//         * 1
//         *  2 2번삭제시 4번같이 삭제
//         *   4
//         *  3
//         */
//
//        MeetingComment comment1 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("1번", null), meeting1.getId(), userDetails1);
//        MeetingComment comment2 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("2번", comment1.getId()), meeting1.getId(), userDetails2);
//        MeetingComment comment3 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("3번", comment1.getId()), meeting1.getId(), userDetails1);
//        MeetingComment comment4 = meetingCommentService.createComment(new MeetingCommentCreateRequestDto("4번", comment2.getId()), meeting1.getId(), userDetails1);
//
//
//        //when
//        meetingCommentService.deleteComment(comment1.getId(),userDetails1);
//
//        //then
//        MeetingDetailResponseDto meeting = meetingService.getMeeting(meeting1.getId(), userDetails1);
//        List<MeetingCommentResponseDto> result = meeting.getComment();
//
//        assertThat(result.get(0).getChildren().size()).isEqualTo(2); //1의 children
//        assertThat(result.get(0).getChildren().get(0).getChildren().size()).isEqualTo(1); // 2의 children
//        assertThat(result.get(0).getChildren().get(0).getContent()).isEqualTo("2번");
//
//
//    }
//}
//
