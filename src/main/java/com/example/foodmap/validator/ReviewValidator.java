package com.example.foodmap.validator;

import com.example.foodmap.dto.review.ReviewRequestDto;

public class ReviewValidator {

    public static void isValidReview(ReviewRequestDto requestDto) {


        if (requestDto.getContent() == null || requestDto.getContent().trim().isEmpty()) {
            throw new NullPointerException("내용을 입력해주세요.");
        }

        if (requestDto.getContent().length() < 10) {
            throw new NullPointerException("내용은 10자 이상 입력해주세요");
        }

        if (requestDto.getRestaurantTags() == null || requestDto.getRestaurantTags().trim().isEmpty()) {
            throw new NullPointerException("식당 태그는 필수로 한 개 선택해주세요.");
        }

        if (requestDto.getSpicy() == null || requestDto.getSpicy().trim().isEmpty()) {
            throw new NullPointerException("매운 정도는 필수로 한 개 선택해주세요");
        }
    }
}
