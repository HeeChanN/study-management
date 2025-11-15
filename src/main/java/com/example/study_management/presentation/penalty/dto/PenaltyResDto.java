package com.example.study_management.presentation.penalty.dto;

import com.example.study_management.domain.Penalty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PenaltyResDto {
    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private int amount;

    private PenaltyResDto(Long id, String title, LocalDateTime createdAt, int amount) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.amount = amount;
    }

    public static PenaltyResDto from(Penalty penalty){
        return new PenaltyResDto(penalty.getId(), penalty.getTitle(), penalty.getCreatedAt(),penalty.getAmount());
    }
}
