package com.finpilot.service;

import com.finpilot.dto.LoginRequest;
import com.finpilot.dto.LoginResponse;
import com.finpilot.dto.RegisterRequest;
import com.finpilot.dto.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}