package com.example.foodmap.dto.user;

import com.example.foodmap.model.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoInfoResponseDto {

    private String nickname;
    private String profileImage;
    private Long kakaoId;
    private Long userId;
    private Long level;
    private Location location;

}
