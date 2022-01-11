package com.example.foodmap.dto.meeting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class MeetingDetailResponseDto<T> {

    private MeetingInfoResponseDto meetingInfo;
    private List<T> comment;
}
