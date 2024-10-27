package com.example.whatup.model.vo;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String role;
    private String content;
}
