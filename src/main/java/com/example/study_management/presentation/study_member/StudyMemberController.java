package com.example.study_management.presentation.study_member;

import com.example.study_management.presentation.study_member.dto.StudyMemberCreateReqDto;
import com.example.study_management.presentation.study_member.dto.StudyMemberResDto;
import com.example.study_management.service.StudyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StudyMemberController {

    private final StudyMemberService studyMemberService;

    @PostMapping("/study-groups/{id}/study-members")
    public void createStudyMembers(@RequestParam StudyMemberCreateReqDto studyMemberCreateReqDto){
        studyMemberService.createStudyMember(studyMemberCreateReqDto);
    }

    @GetMapping("/study-groups/{id}/study-members")
    public List<StudyMemberResDto> getStudyMembers(@PathVariable("id") Long studyGroupId){
        return studyMemberService.getStudyMembers(studyGroupId);
    }
}
