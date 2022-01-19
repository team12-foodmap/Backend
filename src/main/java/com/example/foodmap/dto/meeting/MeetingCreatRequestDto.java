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
public class MeetingCreatRequestDto {

    private String meetingTitle;
    private String restaurant;
    private Long restaurantId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm",timezone = "Asia/Seoul")
    private LocalDateTime meetingDate;
    private String location;
    private int limitPeople;
    private int nowPeople;
    private String content;
}
