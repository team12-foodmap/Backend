package com.example.foodmap.service;

import com.example.foodmap.dto.meeting.ParticipateResponseDto;
import com.example.foodmap.exception.CustomException;
import com.example.foodmap.model.Meeting;
import com.example.foodmap.model.MeetingParticipate;
import com.example.foodmap.repository.MeetingParticipateRepository;
import com.example.foodmap.repository.MeetingRepository;
import com.example.foodmap.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.example.foodmap.exception.ErrorCode.POST_NOT_FOUND;


@RequiredArgsConstructor
@Service
public class MeetingParticipateService {
    private final MeetingParticipateRepository meetingParticipateRepository;
    private final MeetingRepository meetingRepository;

    //모임 참가하기&취소하기
    @Transactional
    public ParticipateResponseDto participateMeeting(Long meetingId, UserDetailsImpl userDetails) {
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(
                ()->new CustomException(POST_NOT_FOUND)
        );

        Optional<MeetingParticipate> participate = meetingParticipateRepository.findByMeetingAndUser(meeting, userDetails.getUser());
        //참가 인원수 확인
        if(checkLimitPeople(meetingId)){
            //Db에 참가되어 있는지 확인
            if(!participate.isPresent()){
                //DB에 참여 데이터 없음
                MeetingParticipate meetingParticipate = new MeetingParticipate(meeting,userDetails);
                //참여상태로 변환(저장)
                meetingParticipateRepository.save(meetingParticipate);
                meeting.addnowPeople();

            }else {
                //참여상태라면 -> 참여취소 상태로 변환
                meetingParticipateRepository.deleteById(participate.get().getId());
                meeting.subnowPeople();

            }
            meetingRepository.save(meeting);

            ParticipateResponseDto participateResponseDto = new ParticipateResponseDto(userDetails.getUser().getId(),meetingId);
            return participateResponseDto;
        }else if(equalPeople(meetingId)){

            if(participate.isPresent()) {
                meetingParticipateRepository.deleteById(participate.get().getId());
                meeting.subnowPeople();
                meetingRepository.save(meeting);

                ParticipateResponseDto participateResponseDto = new ParticipateResponseDto(userDetails.getUser().getId(),meetingId);
                return participateResponseDto;
            }else {
                throw new IllegalArgumentException("참가인원이 초과되었습니다.");
                }
        }else {
            throw new IllegalArgumentException("참가인원이 초과되었습니다.");
        }

    }

    //참가인원 제한수 확인
    public Boolean checkLimitPeople(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(
                ()->new CustomException(POST_NOT_FOUND)
        );
        int LimitPeople = meeting.getLimitPeople();
        int nowPeople= meeting.getNowPeople();

        return LimitPeople > nowPeople;
    }
    public boolean equalPeople(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(
                ()->new CustomException(POST_NOT_FOUND)
        );
        int LimitPeople = meeting.getLimitPeople();
        int nowPeople= meeting.getNowPeople();

        return LimitPeople == nowPeople;
    }
}
