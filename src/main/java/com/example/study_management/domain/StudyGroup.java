package com.example.study_management.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudyGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "fine_per_absence")
    private int finePerAbsence;

    @OneToMany(mappedBy = "studyGroup", fetch = FetchType.LAZY)
    private List<StudyMember> studyMembers = new ArrayList<>();

    @OneToMany(mappedBy = "studyGroup", fetch = FetchType.LAZY)
    private List<Penalty> penalties = new ArrayList<>();

    public StudyGroup(String name, String description, int finePerAbsence) {
        this.name = name;
        this.description = description;
        this.finePerAbsence = finePerAbsence;
    }
}
