//
//package com.example.foodmap.controller;
//
//import com.example.foodmap.MockSpringSecurityFilter;
//import com.example.foodmap.dto.meeting.ParticipateResponseDto;
//import com.example.foodmap.model.Location;
//import com.example.foodmap.model.User;
//import com.example.foodmap.model.UserRoleEnum;
//import com.example.foodmap.security.UserDetailsImpl;
//import com.example.foodmap.security.WebSecurityConfig;
//import com.example.foodmap.service.MeetingParticipateService;
//import com.example.foodmap.service.MeetingService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.security.Principal;
//
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(
//        controllers = MeetingParticipateController.class,
//        excludeFilters = {
//                @ComponentScan.Filter(
//                        type = FilterType.ASSIGNABLE_TYPE,
//                        classes = WebSecurityConfig.class
//                )
//        }
//)
//@MockBean(JpaMetamodelMappingContext.class)
//class MeetingParticipateControllerTest {
//
//    private MockMvc mvc;
//    private Principal mockPrincipal;
//    private User testUser;
//    private UserDetailsImpl testUserDetails;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//    @Autowired
//    private WebApplicationContext context;
//    @MockBean
//    MeetingParticipateService meetingParticipateService;
//    @MockBean
//    MeetingService meetingService;
//
//
//    @BeforeEach
//    public void setup() {
//        mvc = MockMvcBuilders.webAppContextSetup(context)
//                .apply(springSecurity(new MockSpringSecurityFilter()))
//                .build();
//        Location location = new Location("강남구",123.231,12.234);
//        testUser = new User(
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
//        testUserDetails = new UserDetailsImpl(testUser);
//        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
//
//
//    }
//
//
//    @Test
//    @DisplayName("모임 참가")
//    void test1() throws Exception {
//        //given
//
//        ParticipateResponseDto participateResponseDto = new ParticipateResponseDto(testUser.getId(),1L);
//
//        String postInfo = objectMapper.writeValueAsString(participateResponseDto);
//
//        mvc.perform(post("/meetings/participate/{meetingId}",1)
//                        .content(postInfo)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .characterEncoding("utf-8")
//                        .principal(mockPrincipal)
//                )
//                .andExpect(status().isOk())
//                .andDo(print());
//
//    }
//
//
//
//}
//
