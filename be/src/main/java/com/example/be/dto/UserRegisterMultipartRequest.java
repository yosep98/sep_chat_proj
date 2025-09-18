package com.example.be.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserRegisterMultipartRequest {
    
    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;
    
    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 4, max = 20, message = "비밀번호는 4자 이상 20자 이하여야 합니다")
    private String password;
    
    @NotBlank(message = "비밀번호 확인은 필수입니다")
    @Size(min = 4, max = 20, message = "비밀번호 확인은 4자 이상 20자 이하여야 합니다")
    private String passwordConfirm;
    
    @NotBlank(message = "이름은 필수입니다")
    @Size(max = 10, message = "이름은 10자 이하여야 합니다")
    private String name;
    
    private MultipartFile profileImage;
}