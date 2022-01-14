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
    private String tteokbokkiType;
    private int spicy;
    private String image;
}
