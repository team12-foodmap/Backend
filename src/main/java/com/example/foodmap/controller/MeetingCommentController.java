package com.example.foodmap.controller;

import com.example.foodmap.dto.meeting.MeetingCommentCreateRequestDto;
import com.example.foodmap.dto.meeting.MeetingUpdateRequestDto;
import com.example.foodmap.security.UserDetailsImpl;
import com.example.foodmap.service.MeetingCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MeetingCommentController {

    private final MeetingCommentService meetingCommentService;

    //모임 댓글 등록
    @PostMapping("/meetings/comment/{meetingId}")
    public ResponseEntity<String> createComment (@PathVariable Long meetingId, @RequestBody MeetingCommentCreateRequestDto meetingCommentCreateRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        meetingCommentService.createComment(meetingCommentCreateRequestDto,meetingId,userDetails);
        return ResponseEntity.ok().body("댓글 등록 성공");
    }

    //모임 댓글 수정
    @PutMapping("/meetings/comment/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId, @RequestBody MeetingUpdateRequestDto meetingUpdateRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){

        meetingCommentService.updateComment(meetingUpdateRequestDto,commentId,userDetails);
        return ResponseEntity.ok().body("댓글 수정 성공");
    }

    //모임 댓글 삭제
    @DeleteMapping("/meetings/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        meetingCommentService.deleteComment(commentId,userDetails);
        return ResponseEntity.ok().body("댓글 삭제 성공");
    }
}
