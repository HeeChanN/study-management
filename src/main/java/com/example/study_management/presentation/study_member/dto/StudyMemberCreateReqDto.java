package com.example.study_management.presentation.study_member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudyMemberCreateReqDto {
    private String name;
    private Long studyGroupId;
}
