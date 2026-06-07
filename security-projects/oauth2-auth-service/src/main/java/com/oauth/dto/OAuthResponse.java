package com.oauth.dto;

public class OAuthResponse {

    private String token;
    private String name;
    private String email;
    private String provider;
    private String role;
    private String message;

    public OAuthResponse() {}

    public OAuthResponse(String token, String name, String email,
                         String provider, String role, String message) {
        this.token = token;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.role = role;
        this.message = message;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
