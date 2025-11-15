package com.example.study_management.infrastructure.persistence;


import com.example.study_management.domain.DailySubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DailySubmissionRepository extends JpaRepository<DailySubmission, Long>{

    @Query("SELECT ds FROM DailySubmission ds JOIN FETCH ds.studyMember WHERE ds.studyMember.id = :studyMemberId")
    public List<DailySubmission> findAllByStudyMemberId(Long studyMemberId);
}
