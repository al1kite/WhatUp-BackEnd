package com.example.whatup.model.request;

import com.example.whatup.model.vo.Message;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GptRequest {
    private String model;
    private List<Message> messages;
}
