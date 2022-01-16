package com.example.foodmap.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoRequestDto {

    private String nickname;
    private double latitude;
    private double longitude;
    private String address;

    @Builder
    public UserInfoRequestDto(String nickname, double latitude, double longitude, String address) {
        this.nickname = nickname;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }
}
