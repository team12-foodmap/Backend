package com.example.foodmap.dto.mypage;


import com.example.foodmap.model.Location;
import com.example.foodmap.model.RestaurantLikes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MyLikeResponseDto {
    private Long restaurantLikeId;
    private Long restaurantId;
    private String restaurantName;
    private Location location;
    private String fried;
    private String sundae;
    private String tteokbokkiType;
    private int spicy;
    private int restaurantLikesCount;
    private String image;

}
