package com.example.foodmap.model;

import com.example.foodmap.dto.user.UserInfoRequestDto;
import com.example.foodmap.dto.user.UserLocationDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor
@Embeddable
public class Location {

    private String address;
    private double latitude;
    private double longitude;

    @Builder
    public Location(String address, double latitude, double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(UserInfoRequestDto requestDto) {
        this.address = requestDto.getAddress();
        this.latitude = requestDto.getLatitude();
        this.longitude = requestDto.getLongitude();
    }

}
