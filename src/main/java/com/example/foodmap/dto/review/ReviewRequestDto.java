package com.example.foodmap.dto.review;

import com.example.foodmap.model.Restaurant;
import com.example.foodmap.model.Review;
import com.example.foodmap.model.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ReviewRequestDto {
    private String content;
    private String spicy;
    private String restaurantTags;

    public Review toEntity(User user, Restaurant restaurant,String imagePath) {
        return Review.builder()
                .user(user)
                .restaurant(restaurant)
                .image(imagePath)
                .content(content)
                .spicy(Integer.parseInt(spicy))
                .restaurantTags(Integer.parseInt(restaurantTags))
                .build();
    }
}
