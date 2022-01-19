package com.example.foodmap.service;

import com.example.foodmap.dto.Restaurant.RankingResponseDto;
import com.example.foodmap.dto.Restaurant.RestaurantResponseDto;
import com.example.foodmap.dto.meeting.MeetingTotalListResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, String> stringRedisTemplate;
    private final RedisTemplate<String, RestaurantResponseDto> redisNearbyRestaurantListDtoTemplate;
    private final RedisTemplate<String, RankingResponseDto> top3ListTemplate;
    private final RedisTemplate<String, MeetingTotalListResponseDto> meetingLIstTemplate;

    //좌표
    public void setNearbyRestaurantDtoList(String key, List<RestaurantResponseDto> nearbyRestaruantList){
        redisTemplate.delete(key);
        ListOperations<String, RestaurantResponseDto> list = redisNearbyRestaurantListDtoTemplate.opsForList();
        list.leftPushAll(key, nearbyRestaruantList);
        redisTemplate.expire(key, Duration.ofMinutes(5L));
    }

    public List<RestaurantResponseDto> getNearbyRestaurantDtoList(String key) {
       ListOperations<String, RestaurantResponseDto> list = redisNearbyRestaurantListDtoTemplate.opsForList();
        Long len = list.size(key) == null ? 0 : list.size(key);
        return list.range(key,  0 , len);
    }

    //Top3
    public void setTop3(String key, List<RankingResponseDto> value){
        redisTemplate.delete(key);
        ListOperations<String, RankingResponseDto> list = top3ListTemplate.opsForList();
        list.leftPushAll(key, value);
        redisTemplate.expire(key, Duration.ofDays(1L)); // 하루 유지
    }

    public List<RankingResponseDto> getTop3(String key) {
        ListOperations<String, RankingResponseDto> list = top3ListTemplate.opsForList();

        Long len = list.size(key) == null ? 0 : list.size(key);
        return list.range(key, 0 , len);
    }

    //모임전체리스트
    public void setMeeting(String key, List<MeetingTotalListResponseDto> value){
        redisTemplate.delete(key);
        ListOperations<String, MeetingTotalListResponseDto> list = meetingLIstTemplate.opsForList();
        list.leftPushAll(key, value);
        redisTemplate.expire(key, Duration.ofMinutes(30L));
    }

    public List<MeetingTotalListResponseDto> getMeeting(String key) {
        ListOperations<String, MeetingTotalListResponseDto> list = meetingLIstTemplate.opsForList();

        Long len = list.size(key) == null ? 0 : list.size(key);
        return list.range(key, 0 , len);
    }

    //상세페이지

//    //캐시
//    ValueOperations<String, RestaurantDetailResponseDto> op = detailRedisTemplate.opsForValue();
//    String key = "restaurant::" +restaurantId;
//                op.set(key, restaurantDetailResponseDto);

    //key가 레디스에 존재하는지 확인하는 함수
    public boolean isExist(String key) {
        return redisTemplate.hasKey(key);
    }


}
