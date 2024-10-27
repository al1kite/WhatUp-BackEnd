package com.example.whatup.client;

import com.example.whatup.config.GptClientConfig;
import com.example.whatup.model.request.GptRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "gptClient", url = "${openai.api.url}", configuration = GptClientConfig.class)
public interface GptClient {

    @PostMapping("")
    String generatePowerData(@RequestBody GptRequest request);
}

