package com.example.foodmap.dto.meeting;

import com.example.foodmap.model.Meeting;
import com.example.foodmap.model.MeetingComment;
import com.example.foodmap.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MeetingCommentCreateRequestDto {

    private String content;
    private Long parentId;

    public MeetingComment toEntity(User user, Meeting meeting,MeetingComment meetingComment){
        return MeetingComment.builder()
                .content(content)
                .user(user)
                .meeting(meeting)
                .parent(meetingComment)
                .build();


    }
}
