package com.example.foodmap.model;

import com.example.foodmap.dto.Restaurant.RestaurantSaveRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;




@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Restaurant extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private String restaurantName;

    @Embedded
    private Location location;

    @Column(nullable = false)
    private String restaurantType;

    @Column
    private String fried;

    @Column(nullable = false)
    private String sundae;

    @Column(nullable = false)
    private String tteokbokkiType;

    private String image;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    List<RestaurantLikes> restaurantLikes = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    List<Review> reviews = new ArrayList<>();

    @ColumnDefault(value="0")
    private int restaurantLikesCount;

    @Builder
    public Restaurant(RestaurantSaveRequestDto requestDto, String imagePath, User foundUser) {
        this.user = foundUser;
        this.restaurantName = requestDto.getRestaurantName();
        this.location =  new Location(requestDto.getAddress(), requestDto.getLatitude(), requestDto.getLongitude());
        this.restaurantType = requestDto.getRestaurantType();
        this.fried = requestDto.getFried();
        this.sundae = requestDto.getSundae();
        this.tteokbokkiType = requestDto.getTteokbokkiType();
        this.image = imagePath;
    }


}
