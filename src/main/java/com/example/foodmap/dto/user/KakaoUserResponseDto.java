package com.example.foodmap.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class KakaoUserResponseDto {
    private String JWtToken;
    private boolean result;
}
