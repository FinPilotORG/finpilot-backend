package com.finpilot.service;

import com.finpilot.dto.*;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    RefreshTokenResponse refreshToken(String refreshToken);

    void logout(String refreshToken);
}