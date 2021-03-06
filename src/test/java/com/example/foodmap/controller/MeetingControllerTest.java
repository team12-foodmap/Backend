


package com.example.foodmap.controller;

import com.example.foodmap.MockSpringSecurityFilter;
import com.example.foodmap.dto.meeting.*;
import com.example.foodmap.model.Location;
import com.example.foodmap.model.Meeting;
import com.example.foodmap.model.User;
import com.example.foodmap.model.UserRoleEnum;
import com.example.foodmap.security.UserDetailsImpl;
import com.example.foodmap.security.WebSecurityConfig;
import com.example.foodmap.service.MeetingService;
import com.example.foodmap.service.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = MeetingController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
@MockBean(JpaMetamodelMappingContext.class)
class MeetingControllerTest {

    private MockMvc mvc;
    private Principal mockPrincipal;
    private Meeting meeting;
    private User testUser;

    private MeetingCreatRequestDto meetingCreatRequestDto;
    private UserDetailsImpl testUserDetails;
    private String meetingTitle;
    private LocalDateTime startDate;
    private String restaurant;
    private LocalDateTime endDate;
    private LocalDateTime meetingDate;
    private String location1;
    private int limitPeople;
    private int nowPeople;
    private String content;
    private Long restaurantId;
    private int viewCount;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    RedisService redisService;

    @MockBean
    MeetingService meetingService;


    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
        Location location = new Location("?????????",123.231,12.234);
        testUser = new User(
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
        testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());

        meetingTitle = "??????????????? ??????????";
        startDate = LocalDateTime.of(2021, 12, 24, 05, 00);
        endDate = LocalDateTime.of(2021, 12, 25, 12, 00);
        meetingDate = LocalDateTime.of(2021, 12, 28, 14, 00);
        restaurant = "???????????????";
        restaurantId = 1L;
        limitPeople = 5;
        location1 = "?????????";
        nowPeople = 1;
        content = "?????????";
        viewCount =1;

        meetingCreatRequestDto = new MeetingCreatRequestDto(
                meetingTitle, restaurant, restaurantId, endDate, startDate, meetingDate, location1, limitPeople, nowPeople, content
        );


        meeting = new Meeting(testUser,restaurant,restaurantId,meetingTitle,content,location1,startDate,endDate,  meetingDate,viewCount,  limitPeople, nowPeople);

    }


    @Test
    @DisplayName("?????? ??????")
    void test1() throws Exception {
        //given


        String postInfo = objectMapper.writeValueAsString(meetingCreatRequestDto);
        // when - then
        mvc.perform(post("/meetings")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("????????? ????????????")
    void test2() throws Exception {
        //given
        List<ParticipateInfoDto> participateInfoDtoList = new ArrayList<>();
        MeetingInfoResponseDto meetingInfoResponseDto = new MeetingInfoResponseDto(
                meeting.getUser().getNickname(),
                meeting.getMeetingTitle(),
                meeting.getStartDate(),
                meeting.getEndDate(),
                meeting.getMeetingDate(),
                meeting.getLocation(),
                meeting.getLimitPeople(),
                meeting.getNowPeople(),
                meeting.getContent(),
                meeting.getRestaurant(),
                meeting.getViewCount(),
                meeting.getModifiedAt(),
                meeting.getUser().getProfileImage(),
                meeting.getRestaurantId(),
                meeting.getUser().getId(),
                meeting.getId());
        List<MeetingCommentResponseDto> meetingCommentResponseDtoList = new ArrayList<>();
        MeetingDetailResponseDto meetingDetailResponseDto = new MeetingDetailResponseDto(participateInfoDtoList,meetingInfoResponseDto,meetingCommentResponseDtoList);

        String postInfo = objectMapper.writeValueAsString(meetingDetailResponseDto);
        //when-then
        mvc.perform(get("/meetings/{meetingId}",1 )
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("????????? ??????")
    void test3() throws Exception {
        //given

        //when-then
        mvc.perform(delete("/meetings/{meetingId}", 2)

                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(meetingService, times(1)).deleteMeeting(anyLong(), any());
    }


    @Test
    @DisplayName("????????? ??????????????? ??????")
    void test4() throws Exception {
        //given
        List<MeetingTotalListResponseDto> meetingTotalListResponseDtoList = new ArrayList<>();
        MeetingTotalListResponseDto meetingTotalDto = new MeetingTotalListResponseDto( meeting.getMeetingTitle(),
                meeting.getStartDate(),
                meeting.getEndDate(),
                meeting.getMeetingDate(),
                meeting.getLocation(),
                meeting.getLimitPeople(),
                meeting.getNowPeople(),
                meeting.getContent(),
                meeting.getRestaurant(),
                meeting.getViewCount(),
                meeting.getModifiedAt(),
                meeting.getUser().getId(),
                meeting.getId()
        );
        meetingTotalListResponseDtoList.add(meetingTotalDto);

        String postInfo = objectMapper.writeValueAsString(meetingTotalListResponseDtoList);
        //when-then
        mvc.perform(get("/meetings?page=0&size=2")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("????????? ????????? ??????")
    void test5() throws Exception {

        mvc.perform(get("/meetings/search?keyword=??????&page=0&size=2" )

                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

}










