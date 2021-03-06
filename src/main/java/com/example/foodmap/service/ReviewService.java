package com.example.foodmap.service;


import com.example.foodmap.dto.review.*;
import com.example.foodmap.exception.CustomException;
import com.example.foodmap.model.Restaurant;
import com.example.foodmap.model.Review;
import com.example.foodmap.model.ReviewLikes;
import com.example.foodmap.model.User;
import com.example.foodmap.repository.RestaurantRepository;
import com.example.foodmap.repository.ReviewRepository;
import com.example.foodmap.repository.UserRepository;
import com.example.foodmap.validator.ReviewValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.example.foodmap.exception.ErrorCode.*;
import static java.net.URLDecoder.decode;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final StorageService storageService;

    //region 리뷰 작성
    @Transactional
    public void createReview(Long restaurantId, ReviewRequestDto reviewRequestDto, User user, MultipartFile image) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new CustomException(RESTAURANT_NOT_FOUND)
        );
        ReviewValidator.isValidReview(reviewRequestDto); //리뷰 유효성검사


        String imagePath = storageService.uploadFile(image, "review"); //s3 review 폴더에 업로드

        Review review = reviewRequestDto.toEntity(user, restaurant, imagePath);

//        review.addRestaurant(restaurant);
        reviewRepository.save(review);


    }
    //endregion


    //region 리뷰 수정
    @Transactional
    public void updateReview(Long reviewId, ReviewUpdateRequestDto reviewUpdateRequestDto, User user, MultipartFile image) {


        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new CustomException(REVIEW_NOT_FOUND)
        );

        if(!(review.getUser().getId().equals(user.getId()))){
            throw new CustomException(UNAUTHORIZED_UPDATE);
        }
        String imagePath = review.getImage();
        if (image != null) {
            imagePath = storageService.updateFile(imagePath,image,"review");
        }else {
            imagePath = review.getImage();
        }


        String content;
        if (reviewUpdateRequestDto.getContent() == null || reviewUpdateRequestDto.getContent().trim().isEmpty()) {
            content = review.getContent();
        } else {
            content = reviewUpdateRequestDto.getContent();
        }

        String spicy;
        if (reviewUpdateRequestDto.getSpicy() == null || reviewUpdateRequestDto.getSpicy().trim().isEmpty()) {
            spicy = Integer.toString(review.getSpicy());
        } else {
            spicy = reviewUpdateRequestDto.getSpicy();
        }

        String tag;
        if (reviewUpdateRequestDto.getRestaurantTags() == null || reviewUpdateRequestDto.getRestaurantTags().trim().isEmpty()) {
            tag = Integer.toString(review.getRestaurantTags());
        } else {
            tag = reviewUpdateRequestDto.getRestaurantTags();
        }
        review.updateReview(content, imagePath, spicy, tag);



    }

    // region 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId, User user) {


        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new CustomException(REVIEW_NOT_FOUND)
        );

        if(!(review.getUser().getId().equals(user.getId()))){
            throw new CustomException(UNAUTHORIZED_UPDATE);
        }
        reviewRepository.deleteById(reviewId);

        //S3에서 이미지 삭제
        String imagePath = review.getImage();
        storageService.deleteFile(imagePath);

        if(reviewRepository.findAllByRestaurantId(review.getRestaurant().getId()).size() == 0) {
            restaurantRepository.deleteById(review.getRestaurant().getId());
        }

    }

    //region 다른 사람이 쓴 리뷰 조회
    public ReviewResponseDto showReview(Long reviewId) {

        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new CustomException(REVIEW_NOT_FOUND)
        );

        List<ReviewLikesDto> reviewLikesDto = getReviewLikes(review);
        Restaurant restaurant = review.getRestaurant();
        List<Review> isSize = reviewRepository.findAllByRestaurantId(restaurant.getId());
        return ReviewResponseDto.builder()
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
                .reviewLikesDtoList(reviewLikesDto)
                .listSize(isSize.size())
                .build();
    }

    private List<ReviewLikesDto> getReviewLikes(Review review) {
        List<ReviewLikesDto> reviewLikesDtoList = new ArrayList<>();

        if (review.getReviewLikes().size() != 0) {
            List<ReviewLikes> reviewLikesList = review.getReviewLikes();
            for (ReviewLikes reviewLikes : reviewLikesList) {
                ReviewLikesDto reviewLikesDto = new ReviewLikesDto(reviewLikes.getUser().getId());
                reviewLikesDtoList.add(reviewLikesDto);
            }
        }
        return reviewLikesDtoList;
    }


    // endregion

    //region 리뷰 전체 조회

    public List<ReviewAllResponseDto> showAllReview(Long restaurantId, int page, int size) {


        PageRequest pageable = PageRequest.of(page, size);

        List<ReviewAllResponseDto> reviewLists = new ArrayList<>();
        List<Review> reviewList = reviewRepository.findAllByRestaurantIdOrderByReviewLikeDesc(restaurantId, pageable);

        for (Review review : reviewList) {
            List<ReviewLikesDto> reviewLikesDto = getReviewLikes(review);
            ReviewAllResponseDto reviewResponseDto = ReviewAllResponseDto.builder()
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
                    .reviewLikesDtoList(reviewLikesDto)
                    .build();
            reviewLists.add(reviewResponseDto);
        }
        return reviewLists;
    }
    //endregion
}