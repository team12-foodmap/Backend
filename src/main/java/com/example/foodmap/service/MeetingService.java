package com.example.foodmap.service;

import com.example.foodmap.dto.meeting.MeetingSearchDto;
import com.example.foodmap.dto.meeting.*;
import com.example.foodmap.exception.CustomException;
import com.example.foodmap.model.Meeting;
import com.example.foodmap.model.MeetingComment;
import com.example.foodmap.model.MeetingParticipate;
import com.example.foodmap.model.Restaurant;
import com.example.foodmap.repository.*;
import com.example.foodmap.security.UserDetailsImpl;
import com.example.foodmap.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.example.foodmap.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final MeetingParticipateRepository meetingParticipateRepository;
    private final MeetingCommentRepository meetingCommentRepository;
    private final RestaurantRepository restaurantRepository;
    private final RedisService redisService;
    private final RedisTemplate<String, MeetingTotalListResponseDto> meetingLIstTemplate;

    //모임등록글
    @Transactional
    public void creatMeeting(MeetingCreatRequestDto meetingCreatRequestDto, UserDetailsImpl userDetails) {

        UserValidator.isValidUser(userDetails.getUser());
        Meeting meeting = meetingCreatRequestDto.toEntity(userDetails.getUser());


        meetingRepository.save(meeting);

        //모임등록 한사람 자동참가인원+1 자기자신
        MeetingParticipate meetingParticipate = MeetingParticipate.builder()
                .meeting(meeting)
                .userDetails(userDetails)
                .build();
        meetingParticipateRepository.save(meetingParticipate);
        meeting.addnowPeople();

//        meetingCache(meeting);

    }

    private void meetingCache(Meeting meeting) {
        //cache
        String key = "meeting::" + 0 + "/" + 10;
        MeetingTotalListResponseDto meetingTotalDto = new MeetingTotalListResponseDto(
                meeting.getMeetingTitle(),
                meeting.getStartDate(),
                meeting.getEndDate(),
                meeting.getMeetingDate(),
                meeting.getLocation(),
                meeting.getLimitPeople(),
                meeting.getNowPeople(),
                meeting.getContent(),
                meeting.getRestaurant(),
                meeting.getViewCount(),
                meeting.getModifiedAt(),
                meeting.getUser().getId(),
                meeting.getId()
        );

        meetingLIstTemplate.opsForList().leftPushIfPresent(key, meetingTotalDto);

    }

    //상세모임 게시글
    @Transactional
    public MeetingDetailResponseDto getMeeting(Long meetingId, UserDetailsImpl userDetails) {


        UserValidator.isValidUser(userDetails.getUser());
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow( ()-> new CustomException(POST_NOT_FOUND));

        List<MeetingParticipate> participates = meeting.getMeetingParticipates(); //추가
        List<ParticipateInfoDto> participateInfoDtoList = new ArrayList<>();

        for(MeetingParticipate participate: participates){
           participateInfoDtoList.add(new ParticipateInfoDto(participate.getUser().getId()));
       }

        MeetingInfoResponseDto meetingInfoResponseDto = new MeetingInfoResponseDto(
                meeting.getUser().getNickname(),
                meeting.getMeetingTitle(),
                meeting.getStartDate(),
                meeting.getEndDate(),
                meeting.getMeetingDate(),
                meeting.getLocation(),
                meeting.getLimitPeople(),
                meeting.getNowPeople(),
                meeting.getContent(),
                meeting.getRestaurant(),
                meeting.getViewCount(),
                meeting.getModifiedAt(),
                meeting.getUser().getProfileImage().isEmpty()? "" :StorageService.CLOUD_FRONT_DOMAIN_NAME + "/" +meeting.getUser().getProfileImage(),
                meeting.getRestaurantId(),
                meeting.getUser().getId(),
                meeting.getId()
        );

        List<MeetingComment> meetingComments = meeting.getMeetingComments();
        List<MeetingCommentResponseDto> meetingCommentResponseDtos = convertNestedStructure(meetingComments);

        return new MeetingDetailResponseDto(participateInfoDtoList, meetingInfoResponseDto,meetingCommentResponseDtos);
    }


    //댓글 계층구조만들기
    private List<MeetingCommentResponseDto> convertNestedStructure(List<MeetingComment> comments) { //계층형 구조 만들기
        List<MeetingCommentResponseDto> result = new ArrayList<>();
        Map<Long, MeetingCommentResponseDto> map = new HashMap<>();
        comments.forEach(c -> {
            MeetingCommentResponseDto dto = convertCommentToDto(c);
            map.put(dto.getCommentId(), dto);
            if(c.getParent() != null) map.get(c.getParent().getId()).getChildren().add(dto);//양방향 연관관계를 사용해서 자식 코멘트에 댓글 등록
            else result.add(dto);
        });
        return result;
    }

    public MeetingCommentResponseDto convertCommentToDto(MeetingComment comment){
        return new MeetingCommentResponseDto(
                comment.getId(),comment.getContent(),
                comment.getUser().getProfileImage().isEmpty()? "" :StorageService.CLOUD_FRONT_DOMAIN_NAME + "/" +comment.getUser().getProfileImage(),
                comment.getUser().getId(),
                comment.getUser().getNickname(),
                comment.getModifiedAt()
        );
    }


    //모임글 삭제
    @Transactional
    public void deleteMeeting(Long meetingId, UserDetailsImpl userDetails) {

        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(
                ()->new CustomException(POST_NOT_FOUND)
        );

        if(meeting.getUser().getUsername().equals(userDetails.getUsername())){
            meetingRepository.deleteById(meetingId);
        }else {
            throw new CustomException(UNAUTHORIZED_DELETE);
        }
    }

    //모임전체 조회리스트
    @Transactional
    public List<MeetingTotalListResponseDto> getMeetingList(UserDetailsImpl userDetails,int page,int size) {
        UserValidator.isValidUser(userDetails.getUser());


        List<MeetingTotalListResponseDto> meetingTotalListResponseDtoList = new ArrayList<>();


        //반환 목록에 들어갈 데이터 찾을 리스트
        Pageable pageable = PageRequest.of(page,size);
        Page <Meeting> meetingList = meetingRepository.findByOrderByEndDateDesc(pageable);

        for(Meeting meeting:meetingList){
            MeetingTotalListResponseDto meetingTotalDto = new MeetingTotalListResponseDto(
                    meeting.getMeetingTitle(),
                    meeting.getStartDate(),
                    meeting.getEndDate(),
                    meeting.getMeetingDate(),
                    meeting.getLocation(),
                    meeting.getLimitPeople(),
                    meeting.getNowPeople(),
                    meeting.getContent(),
                    meeting.getRestaurant(),
                    meeting.getViewCount(),
                    meeting.getModifiedAt(),
                    meeting.getUser().getId(),
                    meeting.getId()
            );
            meetingTotalListResponseDtoList.add(meetingTotalDto);

        }

//        //cache
//        String key = "meeting::" + page + "/" + size;
//        if (redisService.isExist(key)) {
//            return redisService.getMeeting(key);
//        }
//        if(meetingTotalListResponseDtoList.size() != 0) {
//            redisService.setMeeting(key, meetingTotalListResponseDtoList);
//        }
        return meetingTotalListResponseDtoList;
    }

    //모임 음식점 검색
    @Transactional
    public List<MeetingSearchDto>searchPaging(String restaurantName,String location,int page,int size,UserDetailsImpl userDetails) {
        UserValidator.isValidUser(userDetails.getUser());

        Pageable pageable = PageRequest.of(page,size);
        Page<Restaurant> restaurants = restaurantRepository.findAllSearch(restaurantName,location,pageable);

        List<MeetingSearchDto> meetingSearchDtoList = new ArrayList<>();
        for (Restaurant restaurant:restaurants){

            MeetingSearchDto meetingSearchDto = new MeetingSearchDto(restaurant.getId(),restaurant.getRestaurantName(),restaurant.getLocation().getAddress());
            meetingSearchDtoList.add(meetingSearchDto);
        }

        return meetingSearchDtoList;
    }
}
