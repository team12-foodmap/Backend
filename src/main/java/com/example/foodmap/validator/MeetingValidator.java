package com.example.foodmap.validator;

import com.example.foodmap.dto.meeting.MeetingCreatRequestDto;
import com.example.foodmap.model.Meeting;
import com.example.foodmap.model.User;
import org.springframework.stereotype.Component;

@Component
public class MeetingValidator {

    public static void isValidMeeting(User user,MeetingCreatRequestDto meetingCreatRequestDto){

        if (user == null) {
            throw new NullPointerException("로그인이 필요합니다.");
        }
        if(meetingCreatRequestDto.getRestaurant() == null ||meetingCreatRequestDto.getRestaurant().trim().equals("")) {
            throw new NullPointerException("음식점은 필수 입력 값 입니다.");
        }
        if(meetingCreatRequestDto.getMeetingTitle() == null || meetingCreatRequestDto.getMeetingTitle().trim().equals("")) {
            throw new NullPointerException("글제목은 필수 입력 값 입니다.");
        }
        if(meetingCreatRequestDto.getContent() == null ||meetingCreatRequestDto.getContent().trim().equals("")) {
            throw new NullPointerException("소개글은 필수 입력 값 입니다.");
        }

        if(meetingCreatRequestDto.getStartDate() == null ||meetingCreatRequestDto.getStartDate().equals("")) {
            throw new NullPointerException("모집기간(시작)은 필수 입력 값 입니다.");
        }
        if(meetingCreatRequestDto.getEndDate() == null ||meetingCreatRequestDto.getEndDate().equals("")) {
            throw new NullPointerException("모집기간(끝)은 필수 입력 값 입니다.");
        }
        if(meetingCreatRequestDto.getMeetingDate() == null ||meetingCreatRequestDto.getMeetingDate().equals("")) {
            throw new NullPointerException("모임날짜는 필수 입력 값 입니다.");
        }

        if(!meetingCreatRequestDto.getStartDate().isBefore(meetingCreatRequestDto.getEndDate())){
            throw new IllegalArgumentException("모집마감 시간이 모집시작 시간보다 빠를 수 없습니다.");
        }
        if(!meetingCreatRequestDto.getEndDate().isBefore(meetingCreatRequestDto.getMeetingDate())){
            throw new IllegalArgumentException("만나는 날이 모집기간보다 빠를 수 없습니다.");
        }
        if(meetingCreatRequestDto.getLimitPeople() <= 0) {
            throw new IllegalArgumentException("제한 인원수는 1명 이상 이어야 합니다.");
        }

    }
    //댓글 등록 validator
    public static void isCommentValidator(Meeting meeting, String content){
        if(content == null ||content.trim().equals("")) {
            throw new NullPointerException("댓글내용은 필수 입력 값 입니다.");
        }
        if(meeting==null){
            throw new NullPointerException("존재하지 않는 게시물입니다.");
        }

    }
}
