package com.example.foodmap.dto.Restaurant;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantSaveRequestDto {

        private String restaurantName;
        private double latitude;
        private double longitude;
        private String address;
        private String restaurantType;
        private String fried; //튀김판매유무
        private String sundae;
        private String tteokbokkiType; //밀떡,살떡

}