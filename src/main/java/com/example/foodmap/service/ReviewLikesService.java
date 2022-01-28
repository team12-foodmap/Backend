package com.example.foodmap.service;

import com.example.foodmap.model.Review;
import com.example.foodmap.model.ReviewLikes;
import com.example.foodmap.model.User;
import com.example.foodmap.repository.RestaurantRepository;
import com.example.foodmap.repository.ReviewLikesRepository;
import com.example.foodmap.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewLikesService {

    private final ReviewLikesRepository reviewLikesRepository;
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public void clickReviewLikes(User user, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(null);
        ReviewLikes existLike = reviewLikesRepository.findByUserAndReview(user, review);
        if(reviewLikesRepository.existsByUserAndReview(user,review)) {
            reviewLikesRepository.deleteById(existLike.getId());
            reviewRepository.downLikeCnt(reviewId);
        }
        else{
            ReviewLikes reviewLikes = ReviewLikes.builder()
                    .user(user)
                    .review(review)
                    .build();
            reviewLikes.addReview(review);
            reviewLikesRepository.save(reviewLikes);
            reviewRepository.upLikeCnt(reviewId);
        }
        reviewRepository.save(review);
    }
}
