package com.example.foodmap.dto.meeting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingSearchDto {

    private Long id;
    private String restaurant;
    private String address;
}
