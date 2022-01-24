package com.example.foodmap.dto.Restaurant;

import com.example.foodmap.model.Location;
import com.example.foodmap.model.Restaurant;
import com.example.foodmap.model.User;
import lombok.*;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RestaurantSaveRequestDto {

        private String restaurantName;
        private double latitude;
        private double longitude;
        private String address;
        private String restaurantType;
        private String fried; //튀김판매유무
        private String sundae;
        private String tteokbokkiType; //밀떡,살떡

        //Dto -> Entity
        public Restaurant toEntity(User user, String image) {
                return Restaurant.builder()
                        .image(image)
                        .user(user)
                        .restaurantName(restaurantName)
                        .location(new Location(address, latitude, longitude))
                        .restaurantType(restaurantType)
                        .fried(fried)
                        .sundae(sundae)
                        .tteokbokkiType(tteokbokkiType)
                        .build();
        }
}
