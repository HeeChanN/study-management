package com.example.study_management.service;

import com.example.study_management.domain.StudyGroup;
import com.example.study_management.infrastructure.persistence.StudyGroupRepository;
import com.example.study_management.presentation.study_group.dto.StudyGroupCreateReqDto;
import com.example.study_management.presentation.study_group.dto.StudyGroupResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyGroupService {
    private final StudyGroupRepository studyGroupRepository;

    @Transactional
    public void createStudyGroup(StudyGroupCreateReqDto studyGroupCreateReqDto){
        StudyGroup studyGroup = new StudyGroup(
                studyGroupCreateReqDto.getName(),
                studyGroupCreateReqDto.getContent(),
                studyGroupCreateReqDto.getFinePerAbsence()
        );
        studyGroupRepository.save(studyGroup);
    }

    public StudyGroupResDto getStudyGroup(Long id){
        return StudyGroupResDto.from(studyGroupRepository.findById(id).orElseThrow());
    }

    public List<StudyGroupResDto> getStudyGroups(){
        return studyGroupRepository.findAll().stream()
                .map(studyGroup -> StudyGroupResDto.from(studyGroup))
                .toList();
    }

    public void deleteStudyGroup(Long id){
        studyGroupRepository.deleteById(id);
    }
}
