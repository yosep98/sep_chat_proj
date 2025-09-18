package com.example.be.service;

import com.example.be.domain.User;
import com.example.be.dto.LoginRequest;
import com.example.be.dto.LoginResponse;
import com.example.be.dto.UserRegisterMultipartRequest;
import com.example.be.dto.UserRegisterRequest;
import com.example.be.dto.UserRegisterResponse;
import com.example.be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;
    
    public UserRegisterResponse registerUser(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다");
        }
        
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .profileImagePath(request.getProfileUrl())
                .build();
        
        User savedUser = userRepository.save(user);
        
        return new UserRegisterResponse(
                savedUser.getUserId(),
                savedUser.getEmail(),
                savedUser.getName(),
                savedUser.getProfileImagePath(),
                savedUser.getCreatedAt()
        );
    }
    
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
        
        return new LoginResponse(user.getName(), user.getEmail(), user.getProfileImagePath());
    }
    
    public UserRegisterResponse registerUserWithImage(UserRegisterMultipartRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다");
        }
        
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
        
        String profileImagePath = null;
        if (request.getProfileImage() != null && !request.getProfileImage().isEmpty()) {
            profileImagePath = fileService.saveProfileImage(request.getProfileImage());
        }
        
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .profileImagePath(profileImagePath)
                .build();
        
        User savedUser = userRepository.save(user);
        
        return new UserRegisterResponse(
                savedUser.getUserId(),
                savedUser.getEmail(),
                savedUser.getName(),
                savedUser.getProfileImagePath(),
                savedUser.getCreatedAt()
        );
    }
}