package com.example.study_management.service;

import com.example.study_management.domain.DailySubmission;
import com.example.study_management.domain.StudyMember;
import com.example.study_management.infrastructure.persistence.DailySubmissionRepository;
import com.example.study_management.infrastructure.persistence.StudyMemberRepository;
import com.example.study_management.presentation.daily_submission.dto.DailySubmissionCreateReqDto;
import com.example.study_management.presentation.daily_submission.dto.DailySubmissionResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DailySubmissionService {

    private final DailySubmissionRepository dailySubmissionRepository;
    private final StudyMemberRepository studyMemberRepository;


    public void createDailySubmission(DailySubmissionCreateReqDto dailySubmissionCreateReqDto ){
        StudyMember studyMember = studyMemberRepository.findById(dailySubmissionCreateReqDto.getStudyMemberId())
                .orElseThrow();
        dailySubmissionRepository.save(new DailySubmission(studyMember, dailySubmissionCreateReqDto.getContent()));
    }

    public List<DailySubmissionResDto> getDailySubmissionsInStudyMember(Long studyMemberId){
        return dailySubmissionRepository.findAllByStudyMemberId(studyMemberId)
                .stream().map(dailySubmission -> DailySubmissionResDto.from(dailySubmission))
                .toList();
    }
}
