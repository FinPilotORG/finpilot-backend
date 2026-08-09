package com.finpilot.service.impl;
import com.finpilot.dto.*;
import com.finpilot.entity.RefreshToken;
import com.finpilot.security.service.RefreshTokenService;
import com.finpilot.entity.User;
import com.finpilot.enums.Role;
import com.finpilot.exception.EmailAlreadyExistsException;
import com.finpilot.exception.MobileNumberAlreadyExistsException;
import com.finpilot.repository.UserRepository;
import com.finpilot.security.jwt.JwtService;
import com.finpilot.security.service.CustomUserDetailsService;
import com.finpilot.service.AuthService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            CustomUserDetailsService customUserDetailsService,
            RefreshTokenService refreshTokenService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {

        // Check duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "Email is already registered"
            );
        }

        // Check duplicate mobile number
        if (userRepository.existsByMobileNumber(
                request.getMobileNumber())) {

            throw new MobileNumberAlreadyExistsException(
                    "Mobile number is already registered"
            );
        }

        // Create new User
        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        // Encrypt password
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setMobileNumber(request.getMobileNumber());

        // New users are USER by default
        user.setRole(Role.USER);

        user.setActive(true);

        // Save to database
        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getMobileNumber(),
                savedUser.getRole().name(),
                "User registered successfully"
        );
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        // 1. Authenticate email and password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Load user through CustomUserDetailsService
        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(
                        request.getEmail()
                );

        // 3. Generate Access JWT
        String token =
                jwtService.generateToken(userDetails);

        // 4. Find actual User entity
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        // 5. Create Refresh Token
        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        // 6. Return both tokens
        return new LoginResponse(
                token,
                refreshToken.getToken(),
                "Login successful"
        );
    }
    @Override
    public RefreshTokenResponse refreshToken(String token) {

        // 1. Find refresh token in database
        RefreshToken refreshToken =
                refreshTokenService.findByToken(token);

        // 2. Check whether it is expired
        refreshTokenService.verifyExpiration(refreshToken);

        // 3. Get the user associated with the refresh token
        User user = refreshToken.getUser();

        // 4. Load UserDetails
        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(
                        user.getEmail()
                );

        // 5. Generate a new access token
        String newAccessToken =
                jwtService.generateToken(userDetails);

        // 6. Return new access token
        return new RefreshTokenResponse(
                newAccessToken,
                refreshToken.getToken()
        );
    }
}