package com.example.study_management.infrastructure.external;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class DiscordMessageReqDto {
    private String memberName;
    private List<LocalDate> penaltyDays;

    public DiscordMessageReqDto(String memberName, List<LocalDate> penaltyDays) {
        this.memberName = memberName;
        this.penaltyDays = penaltyDays;
    }

    public static DiscordMessageReqDto from(String memberName, List<LocalDate> penaltyDays){
        return new DiscordMessageReqDto(memberName, penaltyDays);
    }
}
