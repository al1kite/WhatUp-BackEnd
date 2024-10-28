package com.example.whatup.controller;


import com.example.whatup.model.response.PowerConsumptionResponse;
import com.example.whatup.service.WhatUpService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wattup")
@AllArgsConstructor
public class WhatUpController {

    private final WhatUpService whatUpService;

    @GetMapping("/generate")
    public ResponseEntity<PowerConsumptionResponse> getPowerConsumptionData(@RequestParam(name = "taskType") String taskType) {
        PowerConsumptionResponse response = whatUpService.generatePowerConsumptionData(taskType);
        return ResponseEntity.ok(response);
    }
}
