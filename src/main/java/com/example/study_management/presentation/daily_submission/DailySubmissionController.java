package com.example.study_management.presentation.daily_submission;

import com.example.study_management.presentation.daily_submission.dto.DailySubmissionCreateReqDto;
import com.example.study_management.presentation.daily_submission.dto.DailySubmissionResDto;
import com.example.study_management.service.DailySubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class DailySubmissionController {

    private final DailySubmissionService dailySubmissionService;

    @PostMapping("/daily-submissions")
    public void createDailySubmission(@RequestBody DailySubmissionCreateReqDto dailySubmissionCreateReqDto){
        dailySubmissionService.createDailySubmission(dailySubmissionCreateReqDto);
    }

    @GetMapping("/study-members/{id}/daily-submissions")
    public List<DailySubmissionResDto> getDailySubmissions(@PathVariable("id") Long studyMemberId){
        return dailySubmissionService.getDailySubmissionsInStudyMember(studyMemberId);
    }
}
