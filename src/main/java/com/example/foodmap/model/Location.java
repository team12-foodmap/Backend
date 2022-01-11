package com.example.foodmap.model;

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


    public Location(UserLocationDto locationDto) {
        this.address = locationDto.getAddress();
        this.latitude = locationDto.getLatitude();
        this.longitude = locationDto.getLongitude();
    }
    @Builder
    public Location(String address, double latitude, double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
