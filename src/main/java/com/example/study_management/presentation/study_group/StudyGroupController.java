package com.example.study_management.presentation.study_group;

import com.example.study_management.presentation.study_group.dto.StudyGroupResDto;
import com.example.study_management.service.StudyGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StudyGroupController {
    private final StudyGroupService studyGroupService;

    @PostMapping("/study-groups")
    public String createStudyGroup(){
        return "현재 하나의 스터디만 지원하고 있습니다. (코딩 테스트 문제풀이 스터디)";
    }

    @GetMapping("/study-groups")
    public List<StudyGroupResDto> getStudyGroups(){
        return studyGroupService.getStudyGroups();
    }
}
