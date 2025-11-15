package com.example.study_management.global.scheduler;

import com.example.study_management.service.PenaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PenaltyScheduler {

    private final PenaltyService penaltyService;

    /**
     * 매주 토요일 밤 10시에 주간 벌금 부과
     * cron 표현식: 초 분 시 일 월 요일
     * 0 0 22 * * SAT = 매주 토요일 22시 00분 00초
     */
    @Scheduled(cron = "0 0 22 * * SAT", zone = "Asia/Seoul")
    public void scheduleWeeklyPenalties() {
        penaltyService.applyWeeklyPenalties(false);
    }
}
