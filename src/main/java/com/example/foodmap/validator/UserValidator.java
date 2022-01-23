package com.example.foodmap.validator;


import com.example.foodmap.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    public static void isValidUser(User user) {
        if (user == null) {
            throw new NullPointerException("로그인이 필요합니다.");
        }
    }
}
