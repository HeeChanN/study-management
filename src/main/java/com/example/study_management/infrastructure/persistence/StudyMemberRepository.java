package com.example.study_management.infrastructure.persistence;

import com.example.study_management.domain.StudyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {

    @Query("SELECT sm FROM StudyMember sm WHERE sm.studyGroup.id = :studyGroupId")
    List<StudyMember> findAllByStudyGroupId(Long studyGroupId);

    @Query("SELECT sm FROM StudyMember sm JOIN FETCH sm.dailySubmissions WHERE sm.studyGroup.id = :studyGroupId")
    List<StudyMember> findAllByStudyGroupIdWithDailySubmission(Long studyGroupId);
}
