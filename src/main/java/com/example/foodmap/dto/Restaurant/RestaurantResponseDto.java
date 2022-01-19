package com.example.foodmap.dto.Restaurant;

import com.example.foodmap.model.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RestaurantResponseDto {
    private Long restaurantId;
    private String restaurantName;
    private Location location;
    private String fried;
    private String sundae;
    private String tteokbokkiType;
    private int spicy;
    private int reviewCount;
    private int restaurantLikesCount;
    private String image;
    private double distance; //사용자와의 거리

}

