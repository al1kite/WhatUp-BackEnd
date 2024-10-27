package com.example.whatup.service;

import com.example.whatup.client.GptClient;
import com.example.whatup.model.request.GptRequest;
import com.example.whatup.model.vo.Message;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIService {

    private final GptClient gptClient;

    /**
     * 공통 GPT 요청 처리 메서드
     */
    private String callGptApi(String prompt) {
        // GPT API에 전달할 메시지 리스트 생성
        GptRequest request = GptRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(new Message("system", prompt)))
                .build();

        // GPT API 호출
        return gptClient.generatePowerData(request);
    }

    /**
     * 전력 소비 및 FPS 데이터 생성 메서드
     */
    public String generatePowerConsumptionData(String taskType) {
        // 전력 소비 데이터를 생성하는 데 사용할 프롬프트 구성
        String prompt = String.format(
                "작업 유형이 '%s'인 경우의 전력 소비와 FPS 데이터를 생성해주세요. 데이터를 JSON 형식으로 다음과 같이 제공해주세요:\n" +
                        "{\n" +
                        "    \"powerData\": [\n" +
                        "        {\"time\": \"2:08:43 PM\", \"power\": 90},\n" +
                        "        {\"time\": \"2:08:48 PM\", \"power\": 80}\n" +
                        "    ],\n" +
                        "    \"fps\": 60\n" +
                        "}\n" +
                        "전력 데이터는 5초 간격으로 나타내야 합니다.", taskType
        );

        // GPT API 호출 및 응답 반환
        return callGptApi(prompt);
    }

    /**
     * 프롬프트 생성 메서드 (추가 정보가 있는 경우)
     */
    private String createPrompt(String key, String value, String additionalContext) {
        return String.format(
                """
                        다음의 정보를 바탕으로 더 나은 설명을 제공하세요:
                        1. **키**: %s
                        2. **현재 설명**: %s
                        3. **배경 정보**: %s
                        - 이 정보를 바탕으로 더 구체적이고 유용한 설명을 제공해 주세요.
                        4. **기대하는 형식**: 결과는 한 문단으로 요약하고, 가능하다면 불릿 포인트 형식으로도 제공해 주세요.
                        """,
                key,
                value,
                additionalContext != null ? additionalContext : "추가적인 배경 정보는 없습니다."
        );
    }
}
