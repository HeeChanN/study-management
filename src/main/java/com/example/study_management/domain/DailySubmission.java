package com.example.study_management.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DailySubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_member_id")
    private StudyMember studyMember;

    @Column(name = "submission_at")
    private LocalDateTime submissionAt;

    @Column(name = "content", length = 1000)
    private String content;

    public DailySubmission(StudyMember studyMember, String content) {
        this.studyMember = studyMember;
        this.submissionAt = LocalDateTime.now();
        this.content = content;
    }
}
