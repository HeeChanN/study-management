package com.example.study_management.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class StudyMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "studyMember", fetch = FetchType.LAZY)
    private List<DailySubmission> dailySubmissions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id")
    private StudyGroup studyGroup;

    public StudyMember(String name, StudyGroup studyGroup) {
        this.name = name;
        this.studyGroup = studyGroup;
    }

    public int calculateTotalPenaltyDay(List<LocalDate> weekdays) {
        long missedDays = weekdays.stream()
                .filter(date -> dailySubmissions.stream()
                        .noneMatch(sub -> sub.getSubmissionAt().toLocalDate().equals(date)))
                .count();

        return (int) missedDays;
    }
}
