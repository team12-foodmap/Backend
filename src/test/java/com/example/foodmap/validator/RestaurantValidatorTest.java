package com.example.foodmap.validator;

import com.example.foodmap.dto.Restaurant.RestaurantSaveRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RestaurantValidatorTest {

    private String restaurantName = "간판없는 떡볶이" ;
    private double latitude = 126.97688085411811;
    private double longitude = 37.55414219765539;
    private String address = "봉래동1가 104-1번지 2층 중구 서울특별시 KR";
    private String restaurantType = "포장마차";
    private String fried = "true";
    private String sundae = "내장포함";
    private String tteokbokkiType = "밀떡";

    private RestaurantSaveRequestDto requestDto;

    @Test
    @DisplayName("떡볶이집 이름 입력안헀을 경우")
    void fail1() {

        restaurantName = "";
        requestDto = createRequestDto();


        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            RestaurantValidator.isValidRestaurant(requestDto);
        });

        assertThat(exception.getMessage()).isEqualTo("떡볶이집 이름을 입력해주세요.");
    }

    @Test
    @DisplayName("주소 입력안헀을 경우")
    void fail2() {

        address = "";
        requestDto = createRequestDto();


        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            RestaurantValidator.isValidRestaurant(this.requestDto);
        });

        assertThat(exception.getMessage()).isEqualTo("주소를 입력해주세요.");
    }

    @Test
    @DisplayName("식당유형 입력안헀을 경우")
    void fail3() {

        restaurantType = "";
        requestDto = createRequestDto();


        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            RestaurantValidator.isValidRestaurant(this.requestDto);
        });

        assertThat(exception.getMessage()).isEqualTo("식당 유형을 입력해주세요.");
    }

    @Test
    @DisplayName("튀김판매 유무 입력안헀을 경우")
    void fail4() {

        fried = "";
        requestDto = createRequestDto();


        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            RestaurantValidator.isValidRestaurant(this.requestDto);
        });

        assertThat(exception.getMessage()).isEqualTo("튀김판매 유무를 선택해주세요.");
    }

    @Test
    @DisplayName("순대판매 유무 입력안헀을 경우")
    void fail5() {

        sundae = "";
        requestDto = createRequestDto();


        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            RestaurantValidator.isValidRestaurant(this.requestDto);
        });

        assertThat(exception.getMessage()).isEqualTo("순대판매 유무를 선택해주세요.");
    }

    @Test
    @DisplayName("떡 타입 입력안헀을 경우")
    void fail6() {

        tteokbokkiType = "";
        requestDto = createRequestDto();


        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            RestaurantValidator.isValidRestaurant(this.requestDto);
        });

        assertThat(exception.getMessage()).isEqualTo("어떤 떡인지 입력해주세요.");
    }

    private RestaurantSaveRequestDto createRequestDto() {
        return RestaurantSaveRequestDto.builder()
                .restaurantName(restaurantName)
                .latitude(latitude)
                .longitude(longitude)
                .address(address)
                .restaurantType(restaurantType)
                .fried(fried)
                .sundae(sundae)
                .tteokbokkiType(tteokbokkiType)
                .build();
    }
}
