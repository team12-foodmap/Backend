

package com.example.foodmap.service;


import com.example.foodmap.dto.meeting.MeetingCreatRequestDto;
import com.example.foodmap.dto.meeting.MeetingDetailResponseDto;
import com.example.foodmap.dto.meeting.MeetingTotalListResponseDto;
import com.example.foodmap.exception.CustomException;
import com.example.foodmap.model.*;
import com.example.foodmap.repository.*;
import com.example.foodmap.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MeetingServiceTest {

    @InjectMocks
    MeetingService meetingService;
    @Mock
    MeetingRepository meetingRepository;
    @Mock
    RestaurantRepository restaurantRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    MeetingParticipateRepository meetingParticipateRepository;
    @Mock
    MeetingCommentRepository meetingCommentRepository;
    @Mock
    RedisTemplate redisTemplate;
    @Mock
    RedisService redisService;




    private Meeting meeting;
    private User user1;
    private User user2;
    private Restaurant restaurant1;
    private MeetingCreatRequestDto meetingCreatRequestDto;
    private UserDetailsImpl userDetails;
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
    void setup(){
       location = new Location("강남구",123.231,12.234);
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


        meetingTitle="악어떡볶기 가실분?";
        startDate = LocalDateTime.of(2021,12,24,05,00);
        endDate = LocalDateTime.of(2021,12,28,12,00);
        meetingDate = LocalDateTime.of(2021,12,31,14,00);
        restaurant="악어떡볶이";
        limitPeople=5;
        location1="강남역";
        restaurantId = 1L;
        nowPeople=1;
        content= "졸맛집";
        viewCount =1;


        meetingCreatRequestDto = new MeetingCreatRequestDto(
                meetingTitle, restaurant, restaurantId, startDate, endDate, meetingDate, location1, limitPeople, nowPeople, content
        );


        meeting = new Meeting(user1,restaurant,restaurantId,meetingTitle,content,location1,startDate,endDate,  meetingDate,viewCount,  limitPeople, nowPeople);
        //user1 유저 정보
        userDetails = new UserDetailsImpl(user1);

        restaurant1 = Restaurant.builder()
                .restaurantName("별미분식")
                .location(location)
                .build();
        restaurantRepository.save(restaurant1);

    }
    @Test
    @DisplayName("모임등록")
    void create(){
        //given


        //when-then
        meetingService.creatMeeting(meetingCreatRequestDto,userDetails);



     }

    @Test
    @DisplayName("모임조회")
    void getMeeting(){
        //given

        when(meetingRepository.findById(meeting.getId()))
                .thenReturn(Optional.of(meeting));



        //when
        MeetingDetailResponseDto meetingDetailResponseDto = meetingService.getMeeting(meeting.getId(),userDetails);

        //then

        assertEquals(user1.getId(),meetingDetailResponseDto.getMeetingInfo().getUserId());
        assertEquals(meeting.getViewCount(),meetingDetailResponseDto.getMeetingInfo().getViewCount());
        assertEquals(meeting.getMeetingTitle(),meetingDetailResponseDto.getMeetingInfo().getMeetingTitle());
        assertEquals(meeting.getStartDate(),meetingDetailResponseDto.getMeetingInfo().getStartDate());
        assertEquals(meeting.getViewCount(),meetingDetailResponseDto.getMeetingInfo().getViewCount());
    }
    @Test
    @DisplayName("모임조회-게시물이 없을 때")
    void getMeeting1(){
        //given



        //when
        CustomException exception = assertThrows(CustomException.class,
                () -> meetingService.getMeeting(meeting.getId(),userDetails));
        //then
        assertThat(exception.getErrorCode().getDetail()).isEqualTo("해당 게시글을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("모임조회-사용자가 null일 때")
    void getMeeting2(){
        //given


        //when
        CustomException exception = assertThrows(CustomException.class,
                () -> meetingService.getMeeting(meeting.getId(),userDetails));
        //then
        assertThat(exception.getErrorCode().getDetail()).isEqualTo("해당 게시글을 찾을 수 없습니다.");
    }


    @Test
    @DisplayName("모임삭제")
    void deleteMeeting(){
        //given


        when(meetingRepository.findById(meeting.getId())).thenReturn(Optional.of(meeting));

        //when
        meetingService.deleteMeeting(meeting.getId(),userDetails);


        //then
        verify(meetingRepository,times(1)).findById(meeting.getId());
        verify(meetingRepository,times(1)).deleteById(meeting.getId());




    }
    @Test
    @DisplayName("모집글_삭제시_삭제권한이_없을_때")
    void deletePost01()  {
        //given
         user2 = new User(
                "꼬부기",
                "asdf1234",
                152L,
                "haSLE2@naver.com",
                UserRoleEnum.USER,
                241L,
                 "http://sljlet.com",
                 location,
                 "지우"
        );
        //user2 유저 정보
        UserDetailsImpl userDetails2 = new UserDetailsImpl(user2);

        when(meetingRepository.findById(meeting.getId())).thenReturn(Optional.of(meeting));

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> meetingService.deleteMeeting(meeting.getId(), userDetails2));
        //then

        assertThat(exception.getErrorCode().getDetail()).isEqualTo("삭제할 수 있는 권한이 없습니다.");
    }
    @Test
    @DisplayName("모임전체 조회리스트")
    void getMeetingLsit() {
        //given
        List<Meeting> meetings = new ArrayList<>();
        meetings.add(meeting);




        Pageable pageable = PageRequest.of(0,2,Sort.by("modifiedAt").descending());
        int start = (int) pageable.getOffset();
        int end = Math.min((start+pageable.getPageSize()),meetings.size());
        Page<Meeting> meetingList =new PageImpl<>(meetings.subList(start,end),pageable,meetings.size());
        when(meetingRepository.findByOrderByEndDateDesc(any())).thenReturn(meetingList);

        //when
        List<MeetingTotalListResponseDto> meetingTotalListResponseDtoList = meetingService.getMeetingList(userDetails,1,2);

        //then
        assertThat(meetingTotalListResponseDtoList.size()).isEqualTo(1);
        assertThat(meeting.getContent()).isEqualTo(meetingTotalListResponseDtoList.get(0).getContent());

    }
//    @Test
//    @DisplayName("음식점 검색")
//    void search() {
//        //given
//
//        List<Restaurant> restaurants =new ArrayList<>();
//        restaurants.add(restaurant1);
//
//        Pageable pageable = PageRequest.of(0,2,Sort.by("modifiedAt").descending());
//        int start = (int) pageable.getOffset();
//        int end = Math.min((start+pageable.getPageSize()),restaurants.size());
//        Page<Restaurant> restaurantList =new PageImpl<>(restaurants.subList(start,end),pageable,restaurants.size());
//        when(restaurantRepository.findAllSearch(anyString(),anyString(),any())
//
//    }


}

