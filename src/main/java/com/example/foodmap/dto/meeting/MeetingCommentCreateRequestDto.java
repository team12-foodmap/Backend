package com.example.foodmap.dto.meeting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MeetingCommentCreateRequestDto {

    private String content;
    private Long parentId;
}
