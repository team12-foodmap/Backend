package com.example.foodmap.dto.Restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RankingResponseDto {
    private Long restaurantId;
    private String restaurantName;
    private int restaurantLikesCount;
    private String image;;
    private double distance; //사용자와의 거리
}
