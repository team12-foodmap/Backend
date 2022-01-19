package com.example.foodmap.controller;

import com.example.foodmap.dto.meeting.MeetingSearchDto;
import com.example.foodmap.dto.meeting.MeetingCreatRequestDto;
import com.example.foodmap.dto.meeting.MeetingDetailResponseDto;
import com.example.foodmap.dto.meeting.MeetingTotalListResponseDto;
import com.example.foodmap.security.UserDetailsImpl;
import com.example.foodmap.service.MeetingService;

import com.example.foodmap.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MeetingController {

    private final MeetingService meetingService;
    private final RedisService redisService;

    //모임게시글등록
    @PostMapping("/meetings")
    public ResponseEntity<String> creatMeeting(@RequestBody MeetingCreatRequestDto meetingCreatRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        meetingService.creatMeeting(meetingCreatRequestDto,userDetails);
        return ResponseEntity.ok().body("모임글 등록 성공");

    }
    //상세모임게시글
    @GetMapping("/meetings/{meetingId}")
    public MeetingDetailResponseDto getMeeting(@PathVariable Long meetingId, @AuthenticationPrincipal UserDetailsImpl userDetails){

        return  meetingService.getMeeting(meetingId,userDetails);

    }
//    //모임게시글 등록수정
//    @PutMapping("/meetings/{meetingId}")
//    public ResponseEntity<String> updateMeeting(@PathVariable Long meetingId,@RequestBody MeetingCreatRequestDto meetingCreatRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
//
//       meetingService.updateMeeting(meetingId,meetingCreatRequestDto,userDetails);
//       return ResponseEntity.ok().body("모임글 수정 성공");
//
//    }
    //모임게시글 삭제
    @DeleteMapping("/meetings/{meetingId}")
    public ResponseEntity<String> deleteMeeting(@PathVariable Long meetingId, @AuthenticationPrincipal UserDetailsImpl userDetails){

        meetingService.deleteMeeting(meetingId,userDetails);
        return ResponseEntity.ok().body("모임글 삭제 성공");
    }

    //모임전체 게시글 조회
    @GetMapping("/meetings")
    public List<MeetingTotalListResponseDto> meetingList(
            @RequestParam int page,
            @RequestParam int size,
            @AuthenticationPrincipal UserDetailsImpl userDetails){


        String key = "meeting::" + page + "/" + size;
        if (redisService.isExist(key)) {
            return redisService.getMeeting(key);
        }
        return meetingService.getMeetingList(userDetails,page,size);
    }

    //모임 음식점 search
    @GetMapping("/meetings/search")
    public List<MeetingSearchDto> searchPaging(
            @RequestParam(value = "keyword") String restaurantName,
            @RequestParam(value = "keyword") String location,
            @RequestParam int page,
            @RequestParam int size,
            @AuthenticationPrincipal UserDetailsImpl userDetails){

       return meetingService.searchPaging(restaurantName,location,page,size,userDetails);

    }



}
