package com.example.foodmap.controller;

import com.example.foodmap.security.UserDetailsImpl;
import com.example.foodmap.service.RestaurantLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RestaurantLikesController {

    private final RestaurantLikesService restaurantLikesService;

    @PostMapping("/restaurants/wish/{restaurantId}")
    public ResponseEntity<String> bookmark(@PathVariable Long restaurantId,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {

        restaurantLikesService.clickReviewLikes(userDetails.getUser(),restaurantId);

        return ResponseEntity.ok()
                .body("success");
    }
}
