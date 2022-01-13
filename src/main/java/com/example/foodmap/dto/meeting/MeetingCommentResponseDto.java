package com.example.foodmap.dto.meeting;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingCommentResponseDto {

    private Long commentId;
    private Long userId;
    private String profileImage;
    private String nickname;
    private String content;
    private LocalDateTime modifiedAt;
    private List<MeetingCommentResponseDto> children = new ArrayList<>();

    @Builder
    public MeetingCommentResponseDto(Long id, String content,String profileImage, Long userId, String nickname,LocalDateTime modifiedAt) {
        this.commentId = id;
        this.content = content;
        this.profileImage= profileImage;
        this.userId = userId;
        this.nickname = nickname;
        this.modifiedAt=modifiedAt;
    }

}
