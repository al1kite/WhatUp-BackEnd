package com.example.whatup.model.response;

import com.example.whatup.model.vo.PowerDataPoint;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PowerConsumptionResponse {
    private List<PowerDataPoint> powerData;
    private int fps;
    private String taskType;
}
