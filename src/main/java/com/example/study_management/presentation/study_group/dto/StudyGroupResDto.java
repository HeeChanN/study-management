package com.example.study_management.presentation.study_group.dto;

import com.example.study_management.domain.StudyGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudyGroupResDto {

    private Long id;
    private String name;
    private String description;
    private int finePerAbsence;

    private StudyGroupResDto(Long id, String name, String description, int finePerAbsence) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.finePerAbsence = finePerAbsence;
    }

    public static StudyGroupResDto from(StudyGroup studyGroup){
        return new StudyGroupResDto(
                studyGroup.getId(),
                studyGroup.getName(),
                studyGroup.getDescription(),
                studyGroup.getFinePerAbsence()
        );
    }
}
