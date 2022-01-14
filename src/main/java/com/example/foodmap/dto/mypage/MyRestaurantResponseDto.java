package com.example.foodmap.dto.mypage;

import com.example.foodmap.model.Location;
import com.example.foodmap.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MyRestaurantResponseDto {
    private Long restaurantId;
    private String restaurantName;
    private Location location;
    private String fried;
    private String sundae;
    private String tteokbokkiTyp;
    private String spicy;
    private double distance;
    private int reviewCount;
    private int restaurantLikesCount;
    private String image;

    public MyRestaurantResponseDto(Restaurant restaurant) {
        this.restaurantId = restaurant.getId();
        this.restaurantName = restaurant.getRestaurantName();
        this.location = restaurant.getLocation();
        this.distance = getDistance();
        this.reviewCount =getReviewCount();
        this.restaurantLikesCount =getRestaurantLikesCount();
        this.image =restaurant.getImage();

    }
}
