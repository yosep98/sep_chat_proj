package com.example.be.controller;

import com.example.be.dto.ApiResponse;
import com.example.be.dto.LoginRequest;
import com.example.be.dto.LoginResponse;
import com.example.be.dto.UserRegisterMultipartRequest;
import com.example.be.dto.UserRegisterRequest;
import com.example.be.dto.UserRegisterResponse;
import com.example.be.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    
    private final UserService userService;
    
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserRegisterResponse>> signup(@Valid @RequestBody UserRegisterRequest request) {
        try {
            log.info("회원가입 요청 - email: {}", request.getEmail());
            
            UserRegisterResponse response = userService.registerUser(request);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("회원가입이 완료되었습니다", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/signup-with-image")
    public ResponseEntity<ApiResponse<UserRegisterResponse>> signupWithImage(@Valid @ModelAttribute UserRegisterMultipartRequest request) {
        try {
            log.info("이미지 포함 회원가입 요청 - email: {}", request.getEmail());
            
            UserRegisterResponse response = userService.registerUserWithImage(request);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("회원가입이 완료되었습니다", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        try {
            log.info("로그인 요청 - email: {}", request.getEmail());
            
            LoginResponse response = userService.login(request);
            session.setAttribute("userEmail", response.getEmail());
            session.setAttribute("userName", response.getName());
            
            return ResponseEntity.ok(ApiResponse.success("로그인 성공", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(ApiResponse.success("로그아웃 성공"));
    }
}