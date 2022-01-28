package com.example.foodmap.controller;


import com.example.foodmap.dto.review.ReviewAllResponseDto;
import com.example.foodmap.dto.review.ReviewRequestDto;
import com.example.foodmap.dto.review.ReviewResponseDto;
import com.example.foodmap.dto.review.ReviewUpdateRequestDto;
import com.example.foodmap.security.UserDetailsImpl;
import com.example.foodmap.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = {"리뷰"})
@RestController
@RequiredArgsConstructor
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;

    @ApiOperation(value = "리뷰 작성하기")
    @PostMapping("/restaurants/review/{restaurantId}")
    public ResponseEntity<String> createReview(@PathVariable Long restaurantId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @ModelAttribute ReviewRequestDto reviewRequestDto,
                                               @RequestParam MultipartFile image) {

        reviewService.createReview(restaurantId, reviewRequestDto, userDetails.getUser(), image);

        return ResponseEntity.ok()
                .body("리뷰 작성 완료!");
    }

    @ApiOperation(value = "리뷰 수정하기")
    @PutMapping("/restaurants/review/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable Long reviewId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @ModelAttribute ReviewUpdateRequestDto reviewUpdateRequestDto,
                                               @RequestParam(required = false) MultipartFile image) {

        reviewService.updateReview(reviewId, reviewUpdateRequestDto, userDetails.getUser(), image);

        return ResponseEntity.ok()
                .body("리뷰 수정 완료!");
    }

    @ApiOperation(value = "리뷰 삭제하기")
    @DeleteMapping("/restaurants/review/{reviewId}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Long reviewId,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.deleteReview(reviewId, userDetails.getUser());

        return ResponseEntity.ok()
                .body("리뷰 삭제 완료!");
    }

    @ApiOperation(value = "다른 사람이 쓴 리뷰 조회")
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDto> showReview(@PathVariable Long reviewId
                                                             ) {


        ReviewResponseDto requestDto = reviewService.showReview(reviewId);

        return ResponseEntity.ok()
                .body(requestDto);

    }

    @ApiOperation(value = "식당 리뷰 전체조회")
    @GetMapping("/reviews/all/{restaurantId}")
    public ResponseEntity<List<ReviewAllResponseDto>> showAllReview(@PathVariable Long restaurantId,
                                                                    @RequestParam int page,
                                                                    @RequestParam int size
    ) {

        List<ReviewAllResponseDto> responseDto = reviewService.showAllReview(restaurantId,page,size);

        return ResponseEntity.ok()
                .body(responseDto);
    }
}