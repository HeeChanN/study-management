package com.example.study_management.infrastructure.persistence;

import com.example.study_management.domain.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyGroupRepository extends JpaRepository <StudyGroup, Long>{
}
