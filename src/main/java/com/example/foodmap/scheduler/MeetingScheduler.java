package com.example.foodmap.scheduler;

import com.example.foodmap.model.Meeting;
import com.example.foodmap.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MeetingScheduler {

    private final MeetingRepository meetingRepository;

    @Scheduled(cron ="0 0 0 * * *")
    @Transactional
    public void deleteSchedule(){
        LocalDateTime daytime = LocalDateTime.now().minusDays(2);
        meetingRepository.deleteAllByMeetingDateLessThan(daytime);


    }
}
