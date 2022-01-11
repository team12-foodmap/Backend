package com.example.foodmap.dto.mypage;


import com.example.foodmap.model.Location;
import com.example.foodmap.model.RestaurantLikes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MyLikeResponseDto {
    private Long restaurantLikeId;
    private String restaurantName;
    private Location location;
    private double distance;
    private int reviewCount;
    private int restaurantLikesCount;
    private String image;

    public MyLikeResponseDto(RestaurantLikes restaurantLikes) {
        this.restaurantLikeId = restaurantLikes.getId();
        this.restaurantName = restaurantLikes.getRestaurant().getRestaurantName();
        this.location = restaurantLikes.getRestaurant().getLocation();
        this.distance = getDistance();
        this.reviewCount = getReviewCount();
        this.restaurantLikesCount = getReviewCount();
        this.image = restaurantLikes.getRestaurant().getImage();
    }

}
