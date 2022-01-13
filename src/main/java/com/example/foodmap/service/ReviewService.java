package com.example.foodmap.service;

import com.example.foodmap.dto.review.ReviewAllResponseDto;
import com.example.foodmap.dto.review.ReviewRequestDto;
import com.example.foodmap.dto.review.ReviewResponseDto;
import com.example.foodmap.dto.review.ReviewUpdateRequestDto;
import com.example.foodmap.exception.CustomException;
import com.example.foodmap.model.Restaurant;
import com.example.foodmap.model.Review;
import com.example.foodmap.model.User;
import com.example.foodmap.repository.RestaurantRepository;
import com.example.foodmap.repository.ReviewRepository;
import com.example.foodmap.validator.ReviewValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.example.foodmap.exception.ErrorCode.RESTAURANT_NOT_FOUND;
import static com.example.foodmap.exception.ErrorCode.REVIEW_NOT_FOUND;
import static java.net.URLDecoder.decode;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final StorageService storageService;

    //region 리뷰 작성
    @Transactional
    public void createReview(Long restaurantId, ReviewRequestDto reviewRequestDto, User user, MultipartFile image)  {

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new CustomException(RESTAURANT_NOT_FOUND)
        );
        ReviewValidator.isValidReview(reviewRequestDto); //리뷰 유효성검사

        String imagePath = storageService.uploadFile(image, "review"); //s3 review 폴더에 업로드

        Review review = new Review(reviewRequestDto, user, restaurant, imagePath);
        reviewRepository.save(review);
    }
    //endregion


    //region 리뷰 수정
    @Transactional
    public void updateReview(Long reviewId, ReviewUpdateRequestDto reviewUpdateRequestDto, User user,MultipartFile image)  {

        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new CustomException(REVIEW_NOT_FOUND)
        );

        Restaurant restaurant = restaurantRepository.findById(reviewId).orElseThrow(
                () -> new CustomException(RESTAURANT_NOT_FOUND)
        );

        String imagePath;
        if (image.isEmpty()) {
            imagePath = review.getImage();
        } else {
            imagePath = storageService.uploadFile(image, "review");
            String oldImageUrl = decode(
                    review.getImage().replace(
                            "https://team12-images.s3.ap-northeast-2.amazonaws.com/", ""
                    ),
                    StandardCharsets.UTF_8
            );
            storageService.deleteFile(oldImageUrl);
        }

        String content ;
        if (reviewUpdateRequestDto.getContent() == null || reviewUpdateRequestDto.getContent().trim().isEmpty()) {
            content = review.getContent();
        } else {
            content = reviewUpdateRequestDto.getContent();
        }

        String spicy ;
        if (reviewUpdateRequestDto.getSpicy() == null || reviewUpdateRequestDto.getSpicy().trim().isEmpty()) {
            spicy =Integer.toString(review.getSpicy());
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

        if(!user.getId().equals(review.getUser().getId())){
            throw new IllegalArgumentException(("댓글의 작성자만 삭제가 가능합니다."));
        }
        reviewRepository.deleteById(reviewId);

        //S3에서 이미지 삭제
        String oldImageUrl = decode(
                review.getImage().replace(
                        "https://team12-images.s3.ap-northeast-2.amazonaws.com/", ""
                ),
                StandardCharsets.UTF_8
        );
        storageService.deleteFile(oldImageUrl);

    }

    //region 리뷰 조회
    public List<ReviewResponseDto> showReview(Long userId,int page, int size) {

        PageRequest pageable = PageRequest.of(page, size);

        List<ReviewResponseDto> reviewLists = new ArrayList<>();
        List<Review> reviewList = reviewRepository.findAllByUserId(userId,pageable);
        for (Review review : reviewList) {
            ReviewResponseDto reviewResponseDto = ReviewResponseDto.builder()
                    .reviewId(review.getId())
                    .userId(review.getUser().getId())
                    .restaurantId(review.getRestaurant().getId())
                    .image(StorageService.CLOUD_FRONT_DOMAIN_NAME + "/" +review.getImage())
                    .build();
            reviewLists.add(reviewResponseDto);
        }
        return reviewLists;
    }

    // endregion

    //region 리뷰 전체 조회

    public List<ReviewAllResponseDto> showAllReview(Long restaurantId,int page, int size) {

        PageRequest pageable = PageRequest.of(page, size);

        List<ReviewAllResponseDto> reviewLists = new ArrayList<>();
        List<Review> reviewList = reviewRepository.findAllByRestaurantIdOrderByReviewLikeDesc(restaurantId,pageable);
        for (Review review : reviewList) {
            ReviewAllResponseDto reviewResponseDto = ReviewAllResponseDto.builder()
                    .reviewId(review.getId())
                    .userId(review.getUser().getId())
                    .restaurantId(review.getRestaurant().getId())
                    .content(review.getContent())
                    .reviewLikes(review.getReviewLike())
                    .restaurantTags(review.getRestaurantTags())
                    .image(StorageService.CLOUD_FRONT_DOMAIN_NAME + "/" +review.getImage())
                    .build();
            reviewLists.add(reviewResponseDto);
        }
        return reviewLists;
    }
    //endregion
}