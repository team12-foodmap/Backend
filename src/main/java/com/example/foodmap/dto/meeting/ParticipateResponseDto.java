package com.example.foodmap.dto.meeting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipateResponseDto {

    private Long userId;
    private Long meetingId;

}
