package com.example.foodmap.model;


import com.example.foodmap.dto.review.ReviewRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Review extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int spicy;

    @Column(nullable = false)
    private int restaurantTags;

    @Column
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Restaurant restaurant;

    @ColumnDefault(value = "0")
    private int reviewLike;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    List<ReviewLikes> reviewLikes = new ArrayList<>();

    @Builder
    public Review(Long id, String content, int spicy, int restaurantTags, String image, User user, Restaurant restaurant, int reviewLike, List<ReviewLikes> reviewLikes) {
        this.id = id;
        this.content = content;
        this.spicy = spicy;
        this.restaurantTags = restaurantTags;
        this.image = image;
        this.user = user;
        this.restaurant = restaurant;
        this.reviewLike = reviewLike;
        this.reviewLikes = reviewLikes;
    }

    public void updateReview(String content, String imagePath, String spicy, String tag) {
        this.content = content;
        this.image = imagePath;
        this.spicy = Integer.parseInt(spicy);
        this.restaurantTags = Integer.parseInt(tag);
    }
}

//    public void addRestaurant(Restaurant restaurant){
//        this.restaurant=restaurant;
//        restaurant.getReviews().add(this);
//    }
//}