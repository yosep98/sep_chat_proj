package com.example.be.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserRegisterResponse {
    
    private Long userId;
    private String email;
    private String name;
    private String profileImagePath;
    private LocalDateTime createdAt;
}