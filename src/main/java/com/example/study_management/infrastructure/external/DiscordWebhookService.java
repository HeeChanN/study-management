package com.example.study_management.infrastructure.external;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscordWebhookService {

    private final RestClient restClient;

    @Value("${discord.webhook.url}")
    private String webhookUrl;

    public void send(List<DiscordMessageReqDto> discordMessageReqDtos) {
        String content = makeMessage(discordMessageReqDtos);
        restClient.post()
                .uri(webhookUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "content", content,
                        "allowed_mentions", Map.of("parse", List.of())
                ))
                .retrieve()
                .toBodilessEntity();
    }

    private String makeMessage(List<DiscordMessageReqDto> discordMessageReqDtos){
        StringBuilder sb = new StringBuilder();
        sb.append("[ 이번 주 벌금 납부 대상]\n\n");
        String body = discordMessageReqDtos.isEmpty()
                ? "이번 주 벌금 대상자 없음"
                : discordMessageReqDtos.stream()
                .map(messageReqDto -> {
                    return "%s : %,d원".formatted(messageReqDto.getMemberName(), messageReqDto.getFine());
                })
                .collect(Collectors.joining("\n"));
        return sb.append(body).toString();
    }
}
