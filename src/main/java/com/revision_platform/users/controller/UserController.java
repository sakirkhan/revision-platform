package com.revision_platform.users.controller;

import com.revision_platform.users.dto.UserRegistrationRequest;
import com.revision_platform.users.entity.User;
import com.revision_platform.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRegistrationRequest request) {
        if (userService.findByEmail(request.getEmail()).isPresent()) {
            userService.updatePreferences(request.getEmail(), request);
            return ResponseEntity.ok(userService.findByEmail(request.getEmail()).get());
        }
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PutMapping("/{email}/preferences")
    public ResponseEntity<com.revision_platform.users.entity.UserConfig> updatePreferences(
            @PathVariable String email,
            @RequestBody UserRegistrationRequest request) {
        return ResponseEntity.ok(userService.updatePreferences(email, request));
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribeUser(@RequestParam String email, @RequestParam(required = false) String reason) {
        try {
            userService.deactivateUser(email, reason);
            return ResponseEntity.ok("Unsubscribed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("User not found or unsubscription failed");
        }
    }
}
