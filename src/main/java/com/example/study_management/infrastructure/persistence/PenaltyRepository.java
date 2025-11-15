package com.example.study_management.infrastructure.persistence;

import com.example.study_management.domain.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PenaltyRepository extends JpaRepository <Penalty, Long>{

    @Query("SELECT p FROM Penalty p WHERE p.studyGroup.id = :studyGroupId")
    List<Penalty> findAllByStudyGroupId(Long studyGroupId);
}
