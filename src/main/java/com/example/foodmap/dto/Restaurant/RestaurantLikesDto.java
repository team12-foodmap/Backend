package com.example.foodmap.dto.Restaurant;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RestaurantLikesDto {
    private Long userId;

    public RestaurantLikesDto(Long userId) {
        this.userId = userId;
    }
}
