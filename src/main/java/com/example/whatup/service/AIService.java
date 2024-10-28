package com.example.whatup.service;

import com.example.whatup.client.GptClient;
import com.example.whatup.model.request.GptRequest;
import com.example.whatup.model.vo.Message;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.util.List;

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
        // GPT API로 전력 소비 데이터를 요청
        String prompt = String.format(
                """
                        작업 유형이 '%s'인 경우의 전력 소비와 FPS 데이터를 생성해주세요. 데이터를 JSON 형식으로 다음과 같이 제공해주세요:
                        {
                            "powerData": [
                                {"time": "2:08:43 PM", "power": 90},
                                {"time": "2:08:48 PM", "power": 80}
                            ],
                            "fps": FPS는 전력 소비량에 따라 동적으로 결정해주세요.
                        }
                        전력 데이터는 5초 간격으로 나타내야 합니다.""", taskType
        );

        // GPT API 호출 및 응답 받기
        String response = callGptApi(prompt);

        // 응답에서 JSON 데이터를 파싱
        JSONObject jsonResponse;
        try {
            JSONObject gptResponse = new JSONObject(response);
            String content = gptResponse.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

            jsonResponse = new JSONObject(content);
            if (!jsonResponse.has("powerData")) {
                throw new JSONException("Response does not contain 'powerData'");
            }
        } catch (JSONException e) {
            System.err.println("Invalid JSON format: " + e.getMessage());
            return "{ \"error\": \"Invalid response format from GPT API\" }";
        }

        JSONArray powerDataArray = jsonResponse.getJSONArray("powerData");

        // 전력 소비량에 따른 FPS 계산 로직 추가
        int totalPower = 0;
        for (int i = 0; i < powerDataArray.length(); i++) {
            JSONObject entry = powerDataArray.getJSONObject(i);
            int power = entry.getInt("power");
            totalPower += power;
        }

        // 평균 전력 소비를 기반으로 FPS 결정
        int avgPower = totalPower / powerDataArray.length();
        int calculatedFps = calculateFpsBasedOnPower(avgPower);

        // FPS 값을 응답에 추가
        jsonResponse.put("fps", calculatedFps);

        return jsonResponse.toString();
    }

    /**
     * 전력 소비량에 따른 FPS 계산 메서드
     */
    private int calculateFpsBasedOnPower(int avgPower) {
        // 전력 소비량에 따라 FPS 값을 동적으로 결정
        if (avgPower > 100) {
            return (int) (Math.random() * (90 - 80 + 1)) + 80; // 80~90 FPS
        } else if (avgPower > 70) {
            return (int) (Math.random() * (70 - 50 + 1)) + 50; // 50~70 FPS
        } else {
            return (int) (Math.random() * (40 - 30 + 1)) + 30; // 30~40 FPS
        }
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
