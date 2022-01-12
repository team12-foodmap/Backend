package com.example.foodmap.model;


import com.example.foodmap.dto.user.UsernameCreateDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String email;

    @Column(nullable = false)
    private Long kakaoId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(nullable = false)
    private Long level;

    @Column
    private String nickname;

    @Column
    private String profileImage;

    @Embedded
    private Location location;


    @Builder
    public User(String username, String encodedPassword, Long kakaoId, String email , UserRoleEnum role, Long level,String profileImage, Location location,String nickname) {
        this.username=username;
        this.password=encodedPassword;
        this.kakaoId=kakaoId;
        this.email=email;
        this.role=role;
        this.level= level;
        this.profileImage= "";
        this.location = new Location("서울특별시 용산구", 126.97234632175427, 37.55612530289547);
        this.nickname = nickname;
    }

    public void update(UsernameCreateDto usernameCreateDto) {
        this.nickname= usernameCreateDto.getNickname();
    }

    public void saveLocation(Location location) {
        this.location = location;
    }

    public void updateUserInfo(String profileImagePath, String nickname, Location location) {
        this.nickname = nickname;
        this.location = location;

        if (!profileImagePath.isEmpty()) {
            this.profileImage = profileImagePath;
        }
    }
}