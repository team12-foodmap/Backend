package com.example.foodmap.service;

import com.example.foodmap.dto.Restaurant.RestaurantReviewResponseDto;
import com.example.foodmap.dto.mypage.*;
import com.example.foodmap.exception.CustomException;
import com.example.foodmap.model.*;
import com.example.foodmap.repository.MeetingParticipateRepository;
import com.example.foodmap.repository.RestaurantLikesRepository;
import com.example.foodmap.repository.RestaurantRepository;
import com.example.foodmap.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.foodmap.exception.ErrorCode.REVIEW_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class MyPageService {
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final RestaurantLikesRepository restaurantLikesRepository;
    private final MeetingParticipateRepository meetingParticipateRepository;


    //region 리뷰 조회
    public List<MyReviewResponseDto> showMyReview(User user, int page, int size) {

        PageRequest pageable = PageRequest.of(page, size);

        List<MyReviewResponseDto> myReviewList = new ArrayList<>();
        List<Review> reviewList = reviewRepository.findAllByUser(user, pageable);
        for (Review review : reviewList) {
            MyReviewResponseDto myReviewResponseDto = MyReviewResponseDto.builder()
                    .reviewId(review.getId())
                    .userId(review.getUser().getId())
                    .restaurantId(review.getRestaurant().getId())
                    .image(StorageService.CLOUD_FRONT_DOMAIN_NAME + "/" + review.getImage())
                    .build();
            myReviewList.add(myReviewResponseDto);
        }
        return myReviewList;
    }

    //endregion


    //region 좋아요 한 식당 조회
    public List<MyLikeResponseDto> showMyLike(User user, int page, int size) {

        PageRequest pageable = PageRequest.of(page, size);

        List<MyLikeResponseDto> myLikeList = new ArrayList<>();
        List<RestaurantLikes> restaurantLikesList = restaurantLikesRepository.findAllByUser(user,pageable);
        for (RestaurantLikes restaurantLikes : restaurantLikesList) {
            double userLat = user.getLocation().getLatitude();
            double userLon = user.getLocation().getLongitude();

            double restLat = restaurantLikes.getRestaurant().getLocation().getLatitude();
            double restLon = restaurantLikes.getRestaurant().getLocation().getLongitude();
            double distance = getDistance(userLat, userLon, restLat, restLon);

            List<Review> reviews = restaurantLikes.getRestaurant().getReviews();
            List<RestaurantReviewResponseDto> restaurantReviewResponseDtos = new ArrayList<>();

            int spicySum = 0;
            int spicyAvg = 0;
            if (!reviews.isEmpty()) {
                for (Review review : reviews) {
                    spicySum += review.getSpicy();

                    RestaurantReviewResponseDto responseDto = RestaurantReviewResponseDto.builder()
                            .reviewId(review.getId())
                            .build();

                    restaurantReviewResponseDtos.add(responseDto);
                }
                spicyAvg = Math.round(spicySum / restaurantReviewResponseDtos.size()); //맵기 평균값
            }
            MyLikeResponseDto myLikeResponseDto = MyLikeResponseDto.builder()
                    .restaurantLikeId(restaurantLikes.getId())
                    .restaurantId(restaurantLikes.getRestaurant().getId())
                    .restaurantName(restaurantLikes.getRestaurant().getRestaurantName())
                    .location(restaurantLikes.getRestaurant().getLocation())
                    .fried(restaurantLikes.getRestaurant().getFried())
                    .sundae(restaurantLikes.getRestaurant().getSundae())
                    .tteokbokkiType(restaurantLikes.getRestaurant().getTteokbokkiType())
                    .spicy(spicyAvg)
                    .restaurantLikesCount(restaurantLikes.getRestaurant().getRestaurantLikesCount())
                    .image(restaurantLikes.getRestaurant().getImage().isEmpty()? "" :StorageService.CLOUD_FRONT_DOMAIN_NAME + "/" + restaurantLikes.getRestaurant().getImage())
                    .build();
            myLikeList.add(myLikeResponseDto);
        }
        return myLikeList;
    }

    //endregion

    //region 내가 작성 한 식당 조회
    public List<MyRestaurantResponseDto> showMyRestaurant(User user,int page, int size) {

        PageRequest pageable = PageRequest.of(page, size);

        List<MyRestaurantResponseDto> myRestaurantList = new ArrayList<>();
        List<Restaurant> restaurantList = restaurantRepository.findAllByUser(user,pageable);
        for (Restaurant restaurant : restaurantList) {

            List<Review> reviews = restaurant.getReviews();
            List<RestaurantReviewResponseDto> restaurantReviewResponseDtos = new ArrayList<>();

            int spicySum = 0;
            int spicyAvg = 0;
            if (!reviews.isEmpty()) {
                for (Review review : reviews) {
                    spicySum += review.getSpicy();

                    RestaurantReviewResponseDto responseDto = RestaurantReviewResponseDto.builder()
                            .reviewId(review.getId())
                            .build();

                    restaurantReviewResponseDtos.add(responseDto);
                }
                spicyAvg = Math.round(spicySum / restaurantReviewResponseDtos.size()); //맵기 평균값
            }


            MyRestaurantResponseDto myRestaurantResponseDto = MyRestaurantResponseDto.builder()
                    .restaurantId(restaurant.getId())
                    .restaurantName(restaurant.getRestaurantName())
                    .location(restaurant.getLocation())
                    .fried(restaurant.getFried())
                    .sundae(restaurant.getSundae())
                    .tteokbokkiType(restaurant.getTteokbokkiType())
                    .spicy(spicyAvg)
                    .image(restaurant.getImage().isEmpty()? "" :StorageService.CLOUD_FRONT_DOMAIN_NAME + "/" + restaurant.getImage())
                    .build();
            myRestaurantList.add(myRestaurantResponseDto);
        }
        return myRestaurantList;

    }
    //endregion

    //region 내가 참가 한 모임
    public List<MyMeetingResponseDto> showMyMeeting(User user,int page, int size) {

        PageRequest pageable = PageRequest.of(page, size);

        List<MyMeetingResponseDto> myMeetingList = new ArrayList<>();
        List<MeetingParticipate> meetingList = meetingParticipateRepository.findAllByUser(user,pageable);

        for (MeetingParticipate meetingParticipate : meetingList) {

            MyMeetingResponseDto myMeetingResponseDto = MyMeetingResponseDto.builder()
                    .meetingId(meetingParticipate.getMeeting().getId())
                    .userId(meetingParticipate.getUser().getId())
                    .meetingDate(meetingParticipate.getMeeting().getMeetingDate())
                    .meetingTitle(meetingParticipate.getMeeting().getMeetingTitle())
                    .content(meetingParticipate.getMeeting().getContent())
                    .nowPeople(meetingParticipate.getMeeting().getNowPeople())
                    .limitPeople(meetingParticipate.getMeeting().getLimitPeople())
                    .location(meetingParticipate.getMeeting().getLocation())
                    .build();
            myMeetingList.add(myMeetingResponseDto);
        }
        return myMeetingList;
    }
    //endregion

    //region 내 리뷰 상세조회

    public MyReviewDetailResponseDto showMyDetailReview(Long reviewId, User user) {


        Review review = reviewRepository.findAllByUserAndId(user, reviewId);
        if (review == null) {
            reviewRepository.findById(reviewId).orElseThrow(
                    () -> new CustomException(REVIEW_NOT_FOUND));
        }

        assert review != null;
        return MyReviewDetailResponseDto.builder()
                .restaurantId(review.getRestaurant().getId())
                .reviewId(review.getId())
                .userId(review.getUser().getId())
                .reviewLikes(review.getReviewLike())
                .content(review.getContent())
                .location(review.getRestaurant().getLocation())
                .nickname(review.getUser().getNickname())
                .restaurantName(review.getRestaurant().getRestaurantName())
                .createdAt(review.getCreatedAt())
                .modifiedAt(review.getModifiedAt())
                .image(StorageService.CLOUD_FRONT_DOMAIN_NAME + "/" + review.getImage())
                .build();

    }

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