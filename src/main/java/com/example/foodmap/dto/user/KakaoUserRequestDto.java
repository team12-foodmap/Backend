package com.example.foodmap.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserRequestDto {

    private Long id;
    private String username;
    private String email;
    private String profileImage;

}
