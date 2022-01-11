package com.example.foodmap.controller;

import com.example.foodmap.security.UserDetailsImpl;
import com.example.foodmap.service.ReviewLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewLikesController {

    private final ReviewLikesService reviewLikesService;

    @PostMapping("/reviewLikes/{reviewId}")
    public ResponseEntity<String> clickReviewLikes(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @PathVariable Long reviewId)
                                                    {
        reviewLikesService.clickReviewLikes(userDetails.getUser(), reviewId);

        return ResponseEntity.ok()
                .body("success");
    }
}
