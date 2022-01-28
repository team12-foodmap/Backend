package com.example.foodmap.dto.meeting;

import com.example.foodmap.model.Location;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MeetingInfoResponseDto {
    private String nickname;
    private String meetingTitle;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm",timezone = "Asia/Seoul")
    private LocalDateTime startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm",timezone = "Asia/Seoul")
    private LocalDateTime endDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm",timezone = "Asia/Seoul")
    private LocalDateTime meetingDate;
    private String location;
    private int limitPeople;
    private int nowPeople;
    private String content;
    private String restaurant;
    private int viewCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;
    private String profileImage;
    private Long restaurantId;
    private Long userId;
    private Long meetingId;
}
