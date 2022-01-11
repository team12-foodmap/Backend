package com.example.foodmap.dto.meeting;

import com.example.foodmap.model.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MeetingCreatRequestDto {

    private String meetingTitle;
    private LocalDateTime startDate;
    private String restaurant;
    private Long restaurantId;
    private LocalDateTime endDate;
    private LocalDateTime meetingDate;
    private String location;
    private int limitPeople;
    private int nowPeople;
    private String content;
}
