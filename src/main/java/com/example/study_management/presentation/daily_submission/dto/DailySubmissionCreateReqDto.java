package com.example.study_management.presentation.daily_submission.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DailySubmissionCreateReqDto {
    private Long studyMemberId;
    private String content;
}
