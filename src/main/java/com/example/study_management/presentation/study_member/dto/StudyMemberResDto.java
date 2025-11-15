package com.example.study_management.presentation.study_member.dto;

import com.example.study_management.domain.StudyGroup;
import com.example.study_management.domain.StudyMember;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudyMemberResDto {
    private Long id;
    private String name;

    private StudyMemberResDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StudyMemberResDto from(StudyMember studyMember){
        return new StudyMemberResDto(studyMember.getId(), studyMember.getName());
    }
}
