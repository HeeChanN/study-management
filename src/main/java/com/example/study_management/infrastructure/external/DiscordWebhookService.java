package com.example.study_management.infrastructure.external;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.study_management.global.Constants.PENALTY_AMOUNT_PER_DAY;

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

    private String makeMessage(List<DiscordMessageReqDto> discordMessageReqDtos) {
        StringBuilder sb = new StringBuilder();
        sb.append("[ 이번 주 벌금 납부 대상 ]\n\n");

        if (discordMessageReqDtos.isEmpty()) {
            sb.append("이번 주 벌금 대상자 없음");
            return sb.toString();
        }

        String body = discordMessageReqDtos.stream()
                // penaltyDays 없는 사람은 제외
                .filter(dto -> dto.getPenaltyDays() != null && !dto.getPenaltyDays().isEmpty())
                .map(dto -> {
                    int amount = dto.getPenaltyDays().size() * PENALTY_AMOUNT_PER_DAY;

                    String daysText = dto.getPenaltyDays().stream()
                            .sorted()
                            .map(this::toKoreanDayOfWeek)   // LocalDate -> "월", "화" ...
                            .distinct()
                            .collect(Collectors.joining(", "));

                    // 예: "찬미 4,000원 (화, 수)"
                    return "%s %,d원 (%s)".formatted(dto.getMemberName(), amount, daysText);
                })
                .collect(Collectors.joining("\n"));

        if (body.isBlank()) {
            sb.append("이번 주 벌금 대상자 없음");
        } else {
            sb.append(body);
        }

        return sb.toString();
    }

    private String toKoreanDayOfWeek(LocalDate date) {
        return switch (date.getDayOfWeek()) {
            case MONDAY    -> "월";
            case TUESDAY   -> "화";
            case WEDNESDAY -> "수";
            case THURSDAY  -> "목";
            case FRIDAY    -> "금";
            case SATURDAY  -> "토";
            case SUNDAY    -> "일";
        };
    }

}
