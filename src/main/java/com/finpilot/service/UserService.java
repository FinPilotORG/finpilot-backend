package com.finpilot.service;

import com.finpilot.dto.UserRequest;
import com.finpilot.dto.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserRequest request);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(Long id, UserRequest request);

    void deleteUser(Long id);

    UserResponse getCurrentUser(String email);
}