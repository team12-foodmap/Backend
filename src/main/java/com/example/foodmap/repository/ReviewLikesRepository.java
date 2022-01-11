package com.example.foodmap.repository;

import com.example.foodmap.model.Review;
import com.example.foodmap.model.ReviewLikes;
import com.example.foodmap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewLikesRepository extends JpaRepository<ReviewLikes, Long> {
    Boolean existsByUserAndReview(User user, Review review);

    ReviewLikes findByUserAndReview(User user, Review review);


}
