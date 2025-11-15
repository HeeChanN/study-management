package com.example.study_management.service;

import com.example.study_management.domain.StudyGroup;
import com.example.study_management.domain.StudyMember;
import com.example.study_management.infrastructure.persistence.StudyGroupRepository;
import com.example.study_management.infrastructure.persistence.StudyMemberRepository;
import com.example.study_management.presentation.study_member.dto.StudyMemberCreateReqDto;
import com.example.study_management.presentation.study_member.dto.StudyMemberResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyMemberService {

    private final StudyMemberRepository studyMemberRepository;
    private final StudyGroupRepository studyGroupRepository;

    public void createStudyMember(StudyMemberCreateReqDto studyMemberCreateReqDto){
        StudyGroup studyGroup = studyGroupRepository.findById(studyMemberCreateReqDto.getStudyGroupId())
                .orElseThrow();
        StudyMember studyMember = new StudyMember(studyMemberCreateReqDto.getName(),studyGroup);
        studyMemberRepository.save(studyMember);
    }

    public List<StudyMemberResDto> getStudyMembers(Long studyGroupId){
        return studyMemberRepository.findAllByStudyGroupId(studyGroupId)
                .stream().map(studyMember -> StudyMemberResDto.from(studyMember))
                .toList();
    }
}
