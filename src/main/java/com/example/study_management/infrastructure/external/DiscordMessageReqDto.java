package com.example.study_management.infrastructure.external;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DiscordMessageReqDto {
    private String memberName;
    private int fine;

    private DiscordMessageReqDto(String memberName, int fine) {
        this.memberName = memberName;
        this.fine = fine;
    }

    public static DiscordMessageReqDto from(String memberName, int fine){
        return new DiscordMessageReqDto(memberName, fine);
    }
}
