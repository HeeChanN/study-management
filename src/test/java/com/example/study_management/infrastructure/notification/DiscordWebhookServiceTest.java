package com.example.study_management.infrastructure.notification;

import com.example.study_management.infrastructure.external.DiscordMessageReqDto;
import com.example.study_management.infrastructure.external.DiscordWebhookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DiscordWebhookServiceTest {

    @Autowired
    private DiscordWebhookService discordWebhookService;

    @Test
    void twoPersonPenaltyTest() {
        DiscordMessageReqDto dto1 = DiscordMessageReqDto.from("안희찬",2000);
        DiscordMessageReqDto dto2 = DiscordMessageReqDto.from("허지우",2000);

        discordWebhookService.send(List.of(dto1,dto2));
    }

    @Test
    void emptyPenaltyTest(){
        discordWebhookService.send(List.of());
    }
}
