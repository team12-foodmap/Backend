package com.example.foodmap.dto.review;

import com.example.foodmap.model.Restaurant;
import com.example.foodmap.model.Review;
import com.example.foodmap.model.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ReviewUpdateRequestDto {
    private String content;
    private String spicy;
    private String restaurantTags;

}
