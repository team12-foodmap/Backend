package com.example.foodmap.dto.Restaurant;


import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Field;

@Getter
public class RestaurantTagResponseDto{
    private String tagId;
    private int count;

    @Builder
    public RestaurantTagResponseDto(String tagId, int count) {
        this.tagId = tagId;
        this.count = count;
    }


}
