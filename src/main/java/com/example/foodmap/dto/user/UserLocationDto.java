package com.example.foodmap.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserLocationDto {
    private double latitude;
    private double longitude;
    private String address;

    @Builder
    public UserLocationDto(double latitude, double longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }
}