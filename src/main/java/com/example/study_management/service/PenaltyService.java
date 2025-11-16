package com.example.study_management.service;

import com.example.study_management.domain.Penalty;
import com.example.study_management.domain.StudyMember;
import com.example.study_management.global.Constants;
import com.example.study_management.infrastructure.external.DiscordWebhookService;
import com.example.study_management.infrastructure.external.DiscordMessageReqDto;
import com.example.study_management.infrastructure.persistence.DailySubmissionRepository;
import com.example.study_management.infrastructure.persistence.StudyMemberRepository;
import com.example.study_management.infrastructure.persistence.PenaltyRepository;
import com.example.study_management.presentation.penalty.dto.PenaltyResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static com.example.study_management.global.Constants.PENALTY_AMOUNT_PER_DAY;
import static com.example.study_management.global.Constants.SESAC_GROUP_ID;

@Service
@RequiredArgsConstructor
public class PenaltyService {

    private final PenaltyRepository penaltyRepository;
    private final StudyMemberRepository memberRepository;
    private final DiscordWebhookService discordNotificationService;


    public List<PenaltyResDto> getPenalties(Long studyGroupId){
        return penaltyRepository.findAllByStudyGroupId(studyGroupId).stream()
                .map(penalty -> PenaltyResDto.from(penalty))
                .toList();
    }

    /**
     * 주간 벌금 부과 (기본: 지난 주)
     */
    @Transactional
    public void applyWeeklyPenalties() {
        applyWeeklyPenalties(true);
    }


    /** 시연을 위해 평일에도 동작하도록 작성 */
    @Transactional
    public void applyWeeklyPenalties(boolean useLastWeek) {
        LocalDate today = LocalDate.now();
        LocalDate monday = useLastWeek
                ? today.with(TemporalAdjusters.previous(DayOfWeek.MONDAY))
                : today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        List<LocalDate> checkDates = generateWeekdays(monday, useLastWeek, today);
        List<StudyMember> allMembers = memberRepository.findAllByStudyGroupIdWithDailySubmission(SESAC_GROUP_ID);

        List<DiscordMessageReqDto> discordMessageReqDtos = allMembers.stream().map(
                studyMember -> {
                    return DiscordMessageReqDto.from(
                            studyMember.getName(),studyMember.checkPenaltyDay(checkDates));
        }).toList();

        int totalAmount = discordMessageReqDtos.stream()
                .mapToInt(dto -> dto.getPenaltyDays().size())
                .sum() * PENALTY_AMOUNT_PER_DAY;

        Penalty penalty = new Penalty(makeTitle(checkDates,monday),totalAmount,allMembers.get(0).getStudyGroup());
        penaltyRepository.save(penalty);

        discordNotificationService.send(discordMessageReqDtos);
    }

    /**
     * 검사할 날짜 범위 생성
     * - 주말(토, 일): 월~금 (5일)
     * - 평일(월~금): 월~오늘까지
     */
    private List<LocalDate> generateWeekdays(LocalDate monday, boolean useLastWeek, LocalDate today) {
        // 지난 주인 경우: 무조건 월~금 전체
        if (useLastWeek) {
            return Stream.of(0, 1, 2, 3, 4)
                    .map(monday::plusDays)
                    .toList();
        }

        // 이번 주인 경우: 주말이면 월~금, 평일이면 월~오늘
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return Stream.of(0, 1, 2, 3, 4)
                    .map(monday::plusDays)
                    .toList();
        }

        long daysFromMonday = ChronoUnit.DAYS.between(monday, today);
        return LongStream.rangeClosed(0, daysFromMonday)
                .mapToObj(monday::plusDays)
                .toList();
    }

    private String makeTitle(List<LocalDate> checkDates, LocalDate monday){
        LocalDate periodEnd = checkDates.isEmpty()
                ? monday
                : checkDates.get(checkDates.size() - 1);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        String periodLabel = "%s ~ %s 주간 벌금"
                .formatted(monday.format(fmt), periodEnd.format(fmt));
        return periodLabel;
    }
}
