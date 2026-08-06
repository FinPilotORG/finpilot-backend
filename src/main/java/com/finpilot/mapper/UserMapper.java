package com.finpilot.mapper;

import com.finpilot.dto.UserRequest;
import com.finpilot.dto.UserResponse;
import com.finpilot.entity.User;

public class UserMapper {

    public static User toEntity(UserRequest request) {

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setMobileNumber(request.getMobileNumber());

        return user;
    }

    public static UserResponse toResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setMobileNumber(user.getMobileNumber());
        response.setRole(user.getRole());

        return response;
    }
}