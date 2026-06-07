package com.oauth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oauth.dto.OAuthResponse;
import com.oauth.entity.OAuthUser;
import com.oauth.repository.OAuthUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private OAuthUserRepository oAuthUserRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String provider = authentication.getAuthorities()
                .toString().contains("google") ? "google" : "github";

        // Save or update user in DB
        OAuthUser user = oAuthUserRepository.findByEmail(email)
                .orElse(new OAuthUser(email, name, provider,
                        email, "ROLE_USER"));
        oAuthUserRepository.save(user);

        // Generate JWT token
        String token = jwtService.generateToken(email, user.getRole());

        // Return token as JSON response
        OAuthResponse oAuthResponse = new OAuthResponse(
                token, name, email, provider,
                user.getRole(), "OAuth2 Login Successful!"
        );

        response.setContentType("application/json");
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(oAuthResponse));
    }
}
