package com.example.foodmap.service;


import com.example.foodmap.config.CacheKey;
import com.example.foodmap.dto.Restaurant.*;
import com.example.foodmap.dto.meeting.MeetingTotalListResponseDto;
import com.example.foodmap.exception.CustomException;
import com.example.foodmap.model.*;
import com.example.foodmap.repository.RestaurantRepository;
import com.example.foodmap.repository.ReviewRepository;
import com.example.foodmap.repository.UserRepository;
import com.example.foodmap.validator.RestaurantValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.foodmap.config.CacheKey.*;
import static com.example.foodmap.exception.ErrorCode.POST_NOT_FOUND;
import static com.example.foodmap.exception.ErrorCode.USER_NOT_FOUND;



@Slf4j
@EnableCaching
@RequiredArgsConstructor
@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final StorageService storageService;
    private final UserRepository userRepository;
    private final RedisService redisService;
    private final RedisTemplate<String, RestaurantResponseDto> redisNearbyRestaurantListDtoTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    public static final double DISTANCE = 1.5;

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

        Restaurant restaurant = requestDto.toEntity(user, imagePath);

//        //cache적용
//        double userlat = user.getLocation().getLatitude();
//        double userlon = user.getLocation().getLongitude();
//
//        RestaurantResponseDto restaurantResponseDto = getRestaurantResponseDto(userlat, userlon, restaurant);
//
//        double lat1 = Math.floor(requestDto.getLatitude() * 100) / 100;
//        double lon1 = Math.floor(requestDto.getLongitude() * 100) / 100;
//
//        //등록한 식당이 리스트 맨 위에 올라오도록?
//        String key = "restaurant::" + lat1 +"/" + lon1 + "/"+0+"/"+10;
//        redisNearbyRestaurantListDtoTemplate.opsForList().leftPushIfPresent(key, restaurantResponseDto);

        return restaurantRepository.save(restaurant).getId();
    }

    //내 근처 식당 조회 -> 추가: 위치기반으로 조회해서 가까운 순으로 정렬
    public List<RestaurantResponseDto> getRestaurants(double userLat, double userLon, int page, int size) {

        //cache
        double lat1 = Math.floor(userLat * 100) / 100;
        double lon1 = Math.floor(userLon * 100) / 100;

        String key = "restaurant::" + lat1 +"/" + lon1 + "/"+page+"/"+size;
        if (redisService.isExist(key)) {
//           return redisNearbyRestaurantListDtoTemplate.opsForList().range(key, 0, -1);
            return redisService.getNearbyRestaurantDtoList(key);
        }

        List<RestaurantResponseDto> restaurants = new ArrayList<>();

        PageRequest pageable = PageRequest.of(page, size);

        List<Restaurant> restaurantList = restaurantRepository.findRestaurantByLocation(userLat, userLon, DISTANCE, pageable);
        if (restaurantList != null) {

            for (Restaurant restaurant : restaurantList) {

                RestaurantResponseDto responseDto = getRestaurantResponseDto(userLat, userLon, restaurant);

                restaurants.add(responseDto);
            }
        }

        if(restaurants.size() != 0) {
            redisService.setNearbyRestaurantDtoList(key, restaurants);
        }

        return restaurants;
    }


    private RestaurantResponseDto getRestaurantResponseDto(double userLat, double userLon, Restaurant restaurant) {
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
                .image(restaurant.getImage().isEmpty()? "" :StorageService.CLOUD_FRONT_DOMAIN_NAME + "/" + restaurant.getImage())
                .build();
        return responseDto;
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

        RestaurantDetailResponseDto restaurantDetailResponseDto = RestaurantDetailResponseDto.builder()
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
                .image(restaurant.getImage().isEmpty() ? "" : StorageService.CLOUD_FRONT_DOMAIN_NAME + "/" + restaurant.getImage())
                .build();


        return restaurantDetailResponseDto;
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

    //태그값 내림차순 정렬
    private List<RestaurantTagResponseDto> getTagList(Restaurant restaurant) {
        List<RestaurantTagResponseDto> taglist = new ArrayList<>();
        String[] array = {"👍인생맛집이에요", "😇서비스가 좋아요", "💸가성비가 좋아요", "🥉아쉬워요"};

        for (int i = 1; i < 5; i++) {
            int sum = reviewRepository.countRestaurantTags(restaurant.getId(), i);
            RestaurantTagResponseDto tagsDto = RestaurantTagResponseDto.builder()
                    .tagId(array[i-1])
                    .count(sum)
                    .build();
            taglist.add(tagsDto);

        }
        return taglist.stream().sorted(Comparator.comparing(RestaurantTagResponseDto::getCount).reversed()).collect(Collectors.toList());
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
                    .image(restaurantList.getImage().isEmpty()? "" :StorageService.CLOUD_FRONT_DOMAIN_NAME + "/" + restaurantList.getImage())
                    .distance(distance)
                    .build();

            myLikeList.add(rankingResponseDto);

        }

        myLikeList.sort((a, b) -> b.getRestaurantLikesCount() - a.getRestaurantLikesCount());
        if (myLikeList.size() > 3) {
            List<RankingResponseDto> collect = myLikeList.stream()
                    .limit(3)
                    .collect(Collectors.toList());

            redisService.setTop3(TOP3, collect);
            return collect;
        }
        if(myLikeList.size() != 0) {
            redisService.setTop3(TOP3, myLikeList);
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
