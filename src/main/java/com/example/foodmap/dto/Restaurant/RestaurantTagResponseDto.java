package com.example.foodmap.dto.Restaurant;


import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Field;

@Getter
public class RestaurantTagResponseDto {
    private int tagId; //0,1,2,3
    private int count;

    @Builder
    public RestaurantTagResponseDto(int tagId, int count) {
        this.tagId = tagId;
        this.count = count;
    }

}
