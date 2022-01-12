package com.example.foodmap.service;


import com.example.foodmap.dto.Restaurant.*;
import com.example.foodmap.exception.CustomException;
import com.example.foodmap.model.*;
import com.example.foodmap.repository.RestaurantRepository;
import com.example.foodmap.repository.ReviewRepository;
import com.example.foodmap.repository.UserRepository;
import com.example.foodmap.validator.RestaurantValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.foodmap.exception.ErrorCode.POST_NOT_FOUND;
import static com.example.foodmap.exception.ErrorCode.USER_NOT_FOUND;



@Slf4j
@RequiredArgsConstructor
@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final StorageService storageService;
    private final UserRepository userRepository;

    public static final int DISTANCE = 3;

    //region 식당등록
    @Transactional
    public Long saveRestaurant(@ModelAttribute RestaurantSaveRequestDto requestDto, User user,MultipartFile image)   {

        User foundUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );

        RestaurantValidator.isValidRestaurant(requestDto);

        String imagePath = "";
        if (image != null) {
             imagePath = storageService.uploadFile(image, "restaurant");
        }

        Restaurant restaurant = new Restaurant(requestDto, imagePath, foundUser);

        return restaurantRepository.save(restaurant).getId();
    }

    //내 근처 식당 조회 -> 추가: 위치기반으로 조회해서 가까운 순으로 정렬
    public List<RestaurantResponseDto> getRestaurants(double userLat, double userLon, int page, int size) {

        List<RestaurantResponseDto> restaurants = new ArrayList<>();

        PageRequest pageable = PageRequest.of(page, size);

        List<Restaurant> restaurantList = restaurantRepository.findRestaurantByLocation(userLat, userLon, DISTANCE, pageable);
        if (restaurantList != null) {

            for (Restaurant restaurant : restaurantList) {

                double restLat = restaurant.getLocation().getLatitude();
                double restLon = restaurant.getLocation().getLongitude();

                double distance = getDistance(userLat, userLon, restLat, restLon);

                List<Review> reviews = restaurant.getReviews();

                int spicySum = 0;
                int spicyAvg = 0;
                for (Review review : reviews) {
                    spicySum += review.getSpicy();

                }
                if (spicySum != 0) {
                    spicyAvg = Math.round(spicySum / reviews.size()); //맵기 평균값
                }

                RestaurantResponseDto responseDto = RestaurantResponseDto.builder()
                        .restaurantId(restaurant.getId())
                        .restaurantName(restaurant.getRestaurantName())
                        .location(restaurant.getLocation())
                        .fried(restaurant.getFried())
                        .sundae(restaurant.getSundae())
                        .tteokbokkiType(restaurant.getTteokbokkiType())
                        .spicy(spicyAvg)
                        .restaurantLikesCount(restaurant.getRestaurantLikesCount())
                        .distance(distance)
                        .reviewCount(reviews.size())
                        .image(StorageService.CLOUD_FRONT_DOMAIN_NAME + "/" + restaurant.getImage())
                        .build();

                restaurants.add(responseDto);
            }
        }
            return restaurants;
    }
    //endregion

    //region 식당 상세 조회
    public RestaurantDetailResponseDto getRestaurantDetail(Long restaurantId, User user) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new CustomException(POST_NOT_FOUND)
        );

        //리뷰 좋아요가 가장 많은 5개 리뷰만
        List<Review> reviews = reviewRepository.findTop5ByRestaurantOrderByReviewLikeDesc(restaurant);
        List<RestaurantReviewResponseDto> restaurantReviewResponseDtos = new ArrayList<>();

        int spicySum = 0;
        int spicyAvg = 0;
        if (!reviews.isEmpty()) {
            for (Review review : reviews) {
                spicySum += review.getSpicy();

                RestaurantReviewResponseDto responseDto = RestaurantReviewResponseDto.builder()
                        .reviewId(review.getId())
                        .image(StorageService.CLOUD_FRONT_DOMAIN_NAME + "/" +review.getImage())
                        .build();

                restaurantReviewResponseDtos.add(responseDto);
            }
            spicyAvg = Math.round(spicySum / restaurantReviewResponseDtos.size()); //맵기 평균값
        }

        //거리
        double userLat = user.getLocation().getLatitude();
        double userLon = user.getLocation().getLongitude();

        double restLat = restaurant.getLocation().getLatitude();
        double restLon = restaurant.getLocation().getLongitude();

        double distance = getDistance(userLat, userLon, restLat, restLon);

        //식당리뷰 태그
        List<RestaurantTagResponseDto> tagList = getTagList(restaurant);

        //식당 즐겨찾기 리스트
        List<RestaurantLikesDto> restaurantLikesDtoList = getRestaurantLikesDtos(restaurant);

        return RestaurantDetailResponseDto.builder()
                .restaurantId(restaurantId)
                .restaurantName(restaurant.getRestaurantName())
                .location(restaurant.getLocation())
                .restaurantType(restaurant.getRestaurantType())
                .fried(restaurant.getFried())
                .sundae(restaurant.getSundae())
                .tteokbokkiType(restaurant.getTteokbokkiType())
                .spicy(spicyAvg)
                .restaurantTagSum(reviews.size())
                .restaurantTags(tagList)
                .restaurantLikesList(restaurantLikesDtoList)
                .restaurantReviews(restaurantReviewResponseDtos)
                .distance(distance)
                .image(StorageService.CLOUD_FRONT_DOMAIN_NAME + "/" + restaurant.getImage())
                .build();
    }

    private List<RestaurantLikesDto> getRestaurantLikesDtos(Restaurant restaurant) {
        List<RestaurantLikesDto> restaurantLikesDtoList = new ArrayList<>();

        if (restaurant.getRestaurantLikes().size() != 0) {
            List<RestaurantLikes> restaurantLikesList = restaurant.getRestaurantLikes();
            for (RestaurantLikes restaurantLikes : restaurantLikesList) {
                RestaurantLikesDto restaurantLikesDto = new RestaurantLikesDto(restaurantLikes.getUser().getId());
                restaurantLikesDtoList.add(restaurantLikesDto);
            }
        }
        return restaurantLikesDtoList;
    }

    private List<RestaurantTagResponseDto> getTagList(Restaurant restaurant) {
        List<RestaurantTagResponseDto> taglist = new ArrayList<>();

        for (int i = 1; i < 5; i++) {
            int sum = reviewRepository.countRestaurantTags(restaurant.getId(), i);
            RestaurantTagResponseDto tagsDto = RestaurantTagResponseDto.builder()
                    .tagId(i)
                    .count(sum)
                    .build();
            taglist.add(tagsDto);
            System.out.println(tagsDto.toString());
        }
        return taglist;
    }
    //endregion

    //식당순위 Top3
    public List<RankingResponseDto> getTop3ByRestaurant(Restaurant restaurant, User user) {

        ArrayList<RankingResponseDto> myLikeList = new ArrayList<>();
        double userLat = user.getLocation().getLatitude();
        double userLon = user.getLocation().getLongitude();

        List<Restaurant> restaurantLikesList = restaurantRepository.findRestaurantsByRestaurantLikesCountDesc(restaurant);

        for (Restaurant restaurantList : restaurantLikesList) {
            double restLat = restaurantList.getLocation().getLatitude();
            double restLon = restaurantList.getLocation().getLongitude();
            double distance = getDistance(userLat, userLon, restLat, restLon);

            RankingResponseDto rankingResponseDto = RankingResponseDto.builder()
                    .restaurantId(restaurantList.getId())
                    .restaurantName(restaurantList.getRestaurantName())
                    .restaurantLikesCount(restaurantList.getRestaurantLikesCount())
                    .image(StorageService.CLOUD_FRONT_DOMAIN_NAME + "/" + restaurantList.getImage())
                    .distance(distance)
                    .build();

            myLikeList.add(rankingResponseDto);

        }

        myLikeList.sort((a, b) -> b.getRestaurantLikesCount() - a.getRestaurantLikesCount());
        myLikeList.removeIf((a) -> a.getDistance() > 3000);
        if (myLikeList.size() > 3) {
            return myLikeList.stream()
                    .limit(3)
                    .collect(Collectors.toList());
        }
        return myLikeList;
    }

    /**
     * 두 지점간의 거리 계산
     *
     * @param lat1 지점 1 위도
     * @param lon1 지점 1 경도
     * @param lat2 지점 2 위도
     * @param lon2 지점 2 경도
     */
    private static double getDistance(double lat1, double lon1, double lat2, double lon2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1609.344; //미터로 계산

        return (dist);
    }

    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}