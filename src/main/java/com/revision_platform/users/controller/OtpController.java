package com.revision_platform.users.controller;

import com.revision_platform.users.service.OtpService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody OtpRequest request) {
        otpService.generateAndSendOtp(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<OtpResponse> verifyOtp(@RequestBody OtpVerifyRequest request) {
        boolean isValid = otpService.verifyOtp(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(new OtpResponse(isValid));
    }

    @Data
    public static class OtpRequest {
        private String email;
    }

    @Data
    public static class OtpVerifyRequest {
        private String email;
        private String otp;
    }

    @Data
    public static class OtpResponse {
        private final boolean valid;
        
        public OtpResponse(boolean valid) {
            this.valid = valid;
        }
    }
}
