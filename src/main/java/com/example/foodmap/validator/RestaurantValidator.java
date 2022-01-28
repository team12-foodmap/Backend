package com.example.foodmap.validator;

import com.example.foodmap.dto.Restaurant.RestaurantSaveRequestDto;
import com.example.foodmap.dto.review.ReviewRequestDto;

public class RestaurantValidator {

    public static void isValidRestaurant(RestaurantSaveRequestDto requestDto) {

        if (requestDto.getRestaurantName() == null || requestDto.getRestaurantName().trim().isEmpty()) {
            throw new NullPointerException("떡볶이집 이름을 입력해주세요.");
        }

        if (requestDto.getAddress() == null || requestDto.getAddress().trim().isEmpty()) {
            throw new NullPointerException("주소를 입력해주세요.");
        }

        if (requestDto.getRestaurantType() == null || requestDto.getRestaurantType().trim().isEmpty()) {
            throw new NullPointerException("식당 유형을 입력해주세요.");
        }

        if (requestDto.getFried() == null || requestDto.getFried().trim().isEmpty()) {
            throw new NullPointerException("튀김판매 유무를 선택해주세요.");
        }

        if (requestDto.getSundae() == null || requestDto.getSundae().trim().isEmpty()) {
            throw new NullPointerException("순대판매 유무를 선택해주세요.");
        }

        if (requestDto.getTteokbokkiType() == null || requestDto.getTteokbokkiType().trim().isEmpty()) {
            throw new NullPointerException("어떤 떡인지 입력해주세요.");
        }









    }
}
