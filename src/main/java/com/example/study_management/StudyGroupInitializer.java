package com.example.study_management;

import com.example.study_management.domain.StudyGroup;
import com.example.study_management.infrastructure.persistence.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class StudyGroupInitializer implements ApplicationRunner {

    private final StudyGroupRepository studyGroupRepository;

    @Transactional
    @Override
    public void run(ApplicationArguments args) {
        if (!studyGroupRepository.existsById(1L)) {
            studyGroupRepository.save(
                    new StudyGroup(
                            "코테 문제풀이 인증 스터디",
                            "평일 하루 1문제 Commit 기록 남기기",
                            2000
                    )
            );
        }
    }
}
