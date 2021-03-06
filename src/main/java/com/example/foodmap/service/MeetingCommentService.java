package com.example.foodmap.service;

import com.example.foodmap.dto.meeting.MeetingCommentCreateRequestDto;
import com.example.foodmap.dto.meeting.MeetingUpdateRequestDto;
import com.example.foodmap.exception.CustomException;
import com.example.foodmap.model.Meeting;
import com.example.foodmap.model.MeetingComment;
import com.example.foodmap.repository.MeetingCommentRepository;
import com.example.foodmap.repository.MeetingRepository;
import com.example.foodmap.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.example.foodmap.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class MeetingCommentService {

    private final MeetingRepository meetingRepository;
    private final MeetingCommentRepository meetingCommentRepository;


    //모임 댓글 등록
    @Transactional
    public  MeetingComment createComment(MeetingCommentCreateRequestDto meetingCommentCreateRequestDto,Long meetingId, UserDetailsImpl userDetails) {

        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        MeetingComment comment = Optional.ofNullable(meetingCommentCreateRequestDto.getParentId())
                .map(id -> meetingCommentRepository.findById(id).orElseThrow(IllegalArgumentException::new))
                .orElse(null);

        MeetingComment meetingComment = meetingCommentCreateRequestDto.toEntity(userDetails.getUser(),meeting,comment);

        meetingComment.addMeeting(meeting);
        return meetingCommentRepository.save(meetingComment);


    }

    //모임댓글 삭제
    @Transactional
    public Long deleteComment(Long commentId, UserDetailsImpl userDetails) {
        MeetingComment comment = meetingCommentRepository.findById(commentId).orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));
        if (!comment.getUser().getUsername().equals(userDetails.getUsername())) throw new CustomException(UNAUTHORIZED_DELETE);
        meetingCommentRepository.delete(comment);

        return commentId;
    }

    //모임 댓글 수정
    @Transactional
    public void updateComment(MeetingUpdateRequestDto meetingUpdateRequestDto, Long commentId, UserDetailsImpl userDetails) {

        MeetingComment meetingComment = meetingCommentRepository.findById(commentId).orElseThrow(
                ()->new CustomException(COMMENT_NOT_FOUND)
        );

        if(meetingComment.getUser().getUsername().equals(userDetails.getUsername())) {
            meetingComment.updateMeeting(meetingUpdateRequestDto);
        }else {
            throw new CustomException(UNAUTHORIZED_UPDATE);
        }

    }

}
