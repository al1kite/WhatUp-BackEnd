package com.example.whatup.service;

import com.example.whatup.model.response.PowerConsumptionResponse;
import com.example.whatup.model.vo.PowerDataPoint;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class WhatUpService {

    private final AIService aiService;
    private final ObjectMapper objectMapper;

    public PowerConsumptionResponse generatePowerConsumptionData(String taskType) {
        // AIService를 통해 GPT에 전력 소비 데이터 요청
        String gptResponse = aiService.generatePowerConsumptionData(taskType);

        try {
            // GPT의 응답에서 JSON 객체를 직접 파싱
            JsonNode rootNode = objectMapper.readTree(gptResponse);

            // "powerData" 배열과 fps 값 추출
            JsonNode powerDataArray = rootNode.path("powerData");
            int fps = rootNode.path("fps").asInt();

            // 전력 소비 차트 데이터를 리스트로 변환
            List<PowerDataPoint> powerData = new ArrayList<>();
            for (JsonNode node : powerDataArray) {
                String time = node.path("time").asText();
                int power = node.path("power").asInt();
                powerData.add(new PowerDataPoint(time, power));
            }

            // PowerConsumptionResponse 객체를 생성하여 반환
            return new PowerConsumptionResponse(powerData, fps, taskType);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing GPT response", e);
        }
    }
}
