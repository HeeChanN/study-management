package com.example.study_management.presentation.study_group.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudyGroupCreateReqDto {
    private String name;
    private String content;
    private Integer finePerAbsence;
}
