package com.example.foodmap.dto.user;

import com.example.foodmap.model.Location;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoResponseDto {
    private String profileImage;
    private String nickname;
    private Location location;

    @Builder
    public UserInfoResponseDto(String profileImage,String nickname, Location location) {
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.location = location;
    }
}
