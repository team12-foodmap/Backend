package com.example.foodmap.controller;

import com.example.foodmap.dto.mypage.*;
import com.example.foodmap.model.User;
import com.example.foodmap.security.UserDetailsImpl;
import com.example.foodmap.service.MyPageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"마이 페이지"})
@RestController
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;


    @ApiOperation(value = "내가 쓴 리뷰 조회")
    @GetMapping("/myPage/review")
    public ResponseEntity<List<MyReviewResponseDto>> showMyReview(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        List<MyReviewResponseDto> requestDto = myPageService.showMyReview(user);

        return ResponseEntity.ok()
                .body(requestDto);
    }

    @ApiOperation(value = "내가 찜한 식당 조회")
    @GetMapping("/myPage/likeRestaurant")
    public ResponseEntity<List<MyLikeResponseDto>> showMyLike(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        List<MyLikeResponseDto> myLikeResponseDto = myPageService.showMyLike(user);

        return ResponseEntity.ok()
                .body(myLikeResponseDto);
    }

    @ApiOperation(value = "내가 쓴 식당 조회")
    @GetMapping("/myPage/restaurant")
    public ResponseEntity<List<MyRestaurantResponseDto>> showMyRestaurant(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        List<MyRestaurantResponseDto> myRestaurantResponseDto = myPageService.showMyRestaurant(user);

        return ResponseEntity.ok()
                .body(myRestaurantResponseDto);
    }

    @ApiOperation(value = "내가 참가한 미팅 조회")
    @GetMapping("/myPage/meeting")
    public ResponseEntity<List<MyMeetingResponseDto>> showMyMeeting(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        List<MyMeetingResponseDto> myMeetingResponseDto = myPageService.showMyMeeting(user);

        return ResponseEntity.ok()
                .body(myMeetingResponseDto);
    }

    @ApiOperation(value = "내가 쓴 리뷰 상세조회")
    @GetMapping("myPage/review/{reviewId}")
    public ResponseEntity<MyReviewDetailResponseDto> showMyDetailReview(@PathVariable Long reviewId,
                                                                        @AuthenticationPrincipal UserDetailsImpl userDetails){

        User user = userDetails.getUser();
        MyReviewDetailResponseDto myReviewDetailResponseDto = myPageService.showMyDetailReview(reviewId,user);

        return ResponseEntity.ok()
                .body(myReviewDetailResponseDto);

    }
}