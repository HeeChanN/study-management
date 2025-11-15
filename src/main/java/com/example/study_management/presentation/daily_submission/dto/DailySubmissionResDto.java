package com.example.study_management.presentation.daily_submission.dto;

import com.example.study_management.domain.DailySubmission;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class DailySubmissionResDto {
    private Long id;
    private String studyMemberName;
    private LocalDateTime submissionAt;
    private String content;

    private DailySubmissionResDto(Long id, String studyMemberName, LocalDateTime submissionAt, String content) {
        this.id = id;
        this.studyMemberName = studyMemberName;
        this.submissionAt = submissionAt;
        this.content = content;
    }

    public static DailySubmissionResDto from(DailySubmission dailySubmission){
        return new DailySubmissionResDto(
                dailySubmission.getId(),
                dailySubmission.getStudyMember().getName(),
                dailySubmission.getSubmissionAt(),
                dailySubmission.getContent()
        );
    }


}
