package com.oauth.controller;

import com.oauth.dto.OAuthResponse;
import com.oauth.entity.OAuthUser;
import com.oauth.repository.OAuthUserRepository;
import com.oauth.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class OAuth2Controller {

    @Autowired
    private OAuthUserRepository oAuthUserRepository;

    @Autowired
    private JwtService jwtService;

    // OAuth2 Login URLs (open in browser):
    // Google: http://localhost:8086/oauth2/authorization/google
    // GitHub: http://localhost:8086/oauth2/authorization/github

    // Get current logged-in user info
    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getUserInfo(
            @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.ok(
                    Map.of("message", "Not logged in via OAuth2"));
        }
        return ResponseEntity.ok(principal.getAttributes());
    }

    // Get all registered OAuth2 users
    @GetMapping("/users")
    public ResponseEntity<List<OAuthUser>> getAllUsers() {
        return ResponseEntity.ok(oAuthUserRepository.findAll());
    }

    // Validate token
    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(
            @RequestParam String token) {
        boolean valid = jwtService.isTokenValid(token);
        String username = valid ? jwtService.extractUsername(token) : null;
        return ResponseEntity.ok(Map.of(
                "valid", valid,
                "username", username != null ? username : "invalid"
        ));
    }

    // OAuth2 failure handler
    @GetMapping("/oauth2/failure")
    public ResponseEntity<Map<String, String>> failure() {
        return ResponseEntity.ok(
                Map.of("message", "OAuth2 Login Failed!"));
    }

    // Test secured endpoint
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok(
                "OAuth2 secured endpoint accessed successfully!");
    }
}
