package com.example.foodmap.controller;

import com.example.foodmap.dto.meeting.ParticipateResponseDto;
import com.example.foodmap.security.UserDetailsImpl;
import com.example.foodmap.service.MeetingParticipateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MeetingParticipateController {

    private final MeetingParticipateService meetingParticipateService;
    //모임참가하기
    @PostMapping("/meetings/participate/{meetingId}")
    public ParticipateResponseDto participateMeeting(@PathVariable Long meetingId, @AuthenticationPrincipal UserDetailsImpl userDetails){

        return meetingParticipateService.participateMeeting(meetingId,userDetails);
    }
}
