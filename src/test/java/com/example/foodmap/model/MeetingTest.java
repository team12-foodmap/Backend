

package com.example.foodmap.model;


import com.example.foodmap.dto.meeting.MeetingCreatRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MeetingTest {


    private Long id;
    private Long restaurantId;
    private User user;
    private String meetingTitle;
    private LocalDateTime startDate;
    private String restaurant;
    private LocalDateTime endDate;
    private LocalDateTime meetingDate;
    private Location location;
    private String location1;
    private int limitPeople;
    private int nowPeople;
    private String content;


    @BeforeEach
    void setup() {
        location = new Location("강남구",123.231,12.234);
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
        id = 1L;

        meetingTitle = "악어떡볶기 가실분?";
        startDate = LocalDateTime.of(2021, 12, 24, 05, 00);
        endDate = LocalDateTime.of(2021, 12, 25, 12, 00);
        meetingDate = LocalDateTime.of(2021, 12, 28, 14, 00);
        restaurant = "악어떡볶이";
        limitPeople = 5;
        location1 = "강남역";
        restaurantId= 1L;
        nowPeople = 1;
        content = "졸맛집";

    }

    @Nested
    @DisplayName("정상케이스")
    class 정상케이스 {

        @Test
        @DisplayName("정상케이스")
        void 정상케이스() {
            //given
            MeetingCreatRequestDto meetingCreatRequestDto = new MeetingCreatRequestDto(
                    meetingTitle, startDate, restaurant,restaurantId, endDate, meetingDate, location1, limitPeople, nowPeople, content
            );

            //when
            Meeting meeting = new Meeting(user, meetingCreatRequestDto);

            //then

            assertNull(meeting.getId());
            assertEquals(meetingTitle, meeting.getMeetingTitle());
            assertEquals(startDate, meeting.getStartDate());
            assertEquals(restaurant, meeting.getRestaurant());
            assertEquals(endDate, meeting.getEndDate());
            assertEquals(meetingDate, meeting.getMeetingDate());
            assertEquals(location1, meeting.getLocation());
            assertEquals(limitPeople, meeting.getLimitPeople());
            assertEquals(nowPeople, meeting.getNowPeople());
            assertEquals(content, meeting.getContent());

        }

    }

    @Nested
    @DisplayName("실패 케이스")
    class FailCases {
        @Nested
        @DisplayName("유저")
        class 유저 {
            @Test
            @DisplayName("null")
            void fail() {
                //given
                user = null;

                MeetingCreatRequestDto meetingCreatRequestDto = new MeetingCreatRequestDto(
                        meetingTitle, startDate, restaurant,restaurantId, endDate, meetingDate, location1, limitPeople, nowPeople, content
                );

                //when
                Exception exception = assertThrows(NullPointerException.class, () -> {
                    new Meeting(user, meetingCreatRequestDto);
                });

                //then
                assertEquals("로그인이 필요합니다.", exception.getMessage());

            }

        }

        @Nested
        @DisplayName("타이틀")
        class 타이틀 {
            @Test
            @DisplayName("null")
            void fail() {
                //given
                meetingTitle = null;

                MeetingCreatRequestDto meetingCreatRequestDto = new MeetingCreatRequestDto(
                        meetingTitle, startDate, restaurant,restaurantId, endDate, meetingDate, location1, limitPeople, nowPeople, content
                );

                //when
                Exception exception = assertThrows(NullPointerException.class, () -> {
                    new Meeting(user, meetingCreatRequestDto);
                });
                //then
                assertEquals("글제목은 필수 입력 값 입니다.", exception.getMessage());

            }

            @Test
            @DisplayName("빈값")
            void fail2() {
                //given
                meetingTitle = "";
                MeetingCreatRequestDto meetingCreatRequestDto = new MeetingCreatRequestDto(
                        meetingTitle, startDate, restaurant,restaurantId, endDate, meetingDate, location1, limitPeople, nowPeople, content
                );

                //when
                Exception exception = assertThrows(NullPointerException.class, () -> {
                    new Meeting(user, meetingCreatRequestDto);
                });
                //then
                assertEquals("글제목은 필수 입력 값 입니다.", exception.getMessage());

            }
        }

        @Nested
        @DisplayName("음식점")
        class 음식점 {
            @Test
            @DisplayName("null")
            void fail() {
                //given
                restaurant = null;

                MeetingCreatRequestDto meetingCreatRequestDto = new MeetingCreatRequestDto(
                        meetingTitle, startDate, restaurant,restaurantId, endDate, meetingDate, location1, limitPeople, nowPeople, content
                );

                //when
                Exception exception = assertThrows(NullPointerException.class, () -> {
                    new Meeting(user, meetingCreatRequestDto);
                });
                //then
                assertEquals("음식점은 필수 입력 값 입니다.", exception.getMessage());

            }

            @Test
            @DisplayName("빈값")
            void fail2() {
                //given
                restaurant = "";
                MeetingCreatRequestDto meetingCreatRequestDto = new MeetingCreatRequestDto(
                        meetingTitle, startDate, restaurant,restaurantId, endDate, meetingDate, location1, limitPeople, nowPeople, content
                );

                //when
                Exception exception = assertThrows(NullPointerException.class, () -> {
                    new Meeting(user, meetingCreatRequestDto);
                });
                //then
                assertEquals("음식점은 필수 입력 값 입니다.", exception.getMessage());

            }
        }

        @Nested
        @DisplayName("소개내용")
        class 소개내용 {
            @Test
            @DisplayName("null")
            void fail() {
                //given
                content = null;

                MeetingCreatRequestDto meetingCreatRequestDto = new MeetingCreatRequestDto(
                        meetingTitle, startDate, restaurant,restaurantId, endDate, meetingDate, location1, limitPeople, nowPeople, content
                );

                //when
                Exception exception = assertThrows(NullPointerException.class, () -> {
                    new Meeting(user, meetingCreatRequestDto);
                });
                //then
                assertEquals("소개글은 필수 입력 값 입니다.", exception.getMessage());

            }

            @Test
            @DisplayName("빈값")
            void fail2() {
                //given
                content = "";
                MeetingCreatRequestDto meetingCreatRequestDto = new MeetingCreatRequestDto(
                        meetingTitle, startDate, restaurant,restaurantId, endDate, meetingDate, location1, limitPeople, nowPeople, content
                );

                //when
                Exception exception = assertThrows(NullPointerException.class, () -> {
                    new Meeting(user, meetingCreatRequestDto);
                });
                //then
                assertEquals("소개글은 필수 입력 값 입니다.", exception.getMessage());

            }
        }






        @Nested
        @DisplayName("제한인원")
        class 인원 {
            @Test
            @DisplayName("음수 or 0")
            void fail() {
                //given
                limitPeople = 0;

                MeetingCreatRequestDto meetingCreatRequestDto = new MeetingCreatRequestDto(
                        meetingTitle, startDate, restaurant,restaurantId, endDate, meetingDate, location1, limitPeople, nowPeople, content
                );

                //when
                Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                    new Meeting(user, meetingCreatRequestDto);
                });
                //then
                assertEquals("제한 인원수는 1명 이상 이어야 합니다.", exception.getMessage());

            }

            @Nested
            @DisplayName("모집기간")
            class 모집기간 {
                @Test
                @DisplayName("모집시작 null")
                void fail() {
                    //given
                    startDate = null;


                    MeetingCreatRequestDto meetingCreatRequestDto = new MeetingCreatRequestDto(
                            meetingTitle, startDate, restaurant,restaurantId, endDate, meetingDate, location1, limitPeople, nowPeople, content
                    );

                    //when
                    Exception exception = assertThrows(NullPointerException.class, () -> {
                        new Meeting(user, meetingCreatRequestDto);
                    });
                    //then
                    assertEquals("모집기간(시작)은 필수 입력 값 입니다.", exception.getMessage());

                }

                @Test
                @DisplayName("모집마감 null")
                void fail2() {
                    //given

                    endDate = null;


                    MeetingCreatRequestDto meetingCreatRequestDto = new MeetingCreatRequestDto(
                            meetingTitle, startDate, restaurant,restaurantId, endDate, meetingDate, location1, limitPeople, nowPeople, content
                    );

                    //when
                    Exception exception = assertThrows(NullPointerException.class, () -> {
                        new Meeting(user, meetingCreatRequestDto);
                    });
                    //then

                    assertEquals("모집기간(끝)은 필수 입력 값 입니다.", exception.getMessage());

                }

                @Test
                @DisplayName("모임날짜 null")
                void fail3() {
                    //given

                    meetingDate = null;

                    MeetingCreatRequestDto meetingCreatRequestDto = new MeetingCreatRequestDto(
                            meetingTitle, startDate, restaurant,restaurantId, endDate, meetingDate, location1, limitPeople, nowPeople, content
                    );

                    //when
                    Exception exception = assertThrows(NullPointerException.class, () -> {
                        new Meeting(user, meetingCreatRequestDto);
                    });
                    //then

                    assertEquals("모임날짜는 필수 입력 값 입니다.", exception.getMessage());

                }

                @Test
                @DisplayName("모집시작보다 모집마감 빠른날짜선택 시")
                void fail4() {
                    //given
                    startDate = LocalDateTime.of(2021, 12, 24, 05, 00);
                    endDate = LocalDateTime.of(2021, 12, 23, 04, 59);

                    MeetingCreatRequestDto meetingCreatRequestDto = new MeetingCreatRequestDto(
                            meetingTitle, startDate, restaurant,restaurantId, endDate, meetingDate, location1, limitPeople, nowPeople, content
                    );

                    //when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Meeting(user, meetingCreatRequestDto);
                    });
                    //then

                    assertEquals("모집마감 시간이 모집시작 시간보다 빠를 수 없습니다.", exception.getMessage());

                }

                @Test
                @DisplayName("모집마감보다 만나는 날짜 빠른날짜선택 시")
                void fail5() {
                    //given
                    startDate = LocalDateTime.of(2021, 12, 24, 05, 00);
                    endDate = LocalDateTime.of(2021, 12, 27, 05, 00);
                    meetingDate = LocalDateTime.of(2021, 12, 26, 05, 00);

                    MeetingCreatRequestDto meetingCreatRequestDto = new MeetingCreatRequestDto(
                            meetingTitle, startDate, restaurant,restaurantId, endDate, meetingDate, location1, limitPeople, nowPeople, content
                    );

                    //when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Meeting(user, meetingCreatRequestDto);
                    });
                    //then

                    assertEquals("만나는 날이 모집기간보다 빠를 수 없습니다.", exception.getMessage());

                }
            }
        }


    }
}

