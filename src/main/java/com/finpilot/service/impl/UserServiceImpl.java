package com.finpilot.service.impl;

import com.finpilot.dto.UserRequest;
import com.finpilot.dto.UserResponse;
import com.finpilot.entity.User;
import com.finpilot.exception.EmailAlreadyExistsException;
import com.finpilot.exception.MobileNumberAlreadyExistsException;
import com.finpilot.exception.UserNotFoundException;
import com.finpilot.mapper.UserMapper;
import com.finpilot.repository.UserRepository;
import com.finpilot.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse createUser(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists.");
        }

        if (userRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new MobileNumberAlreadyExistsException("Mobile number already exists.");
        }

        User user = UserMapper.toEntity(request);

        User savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with id : " + id));

        return UserMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with id : " + id));

        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {

            throw new EmailAlreadyExistsException("Email already exists.");
        }

        if (!user.getMobileNumber().equals(request.getMobileNumber())
                && userRepository.existsByMobileNumber(request.getMobileNumber())) {

            throw new MobileNumberAlreadyExistsException("Mobile number already exists.");
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setMobileNumber(request.getMobileNumber());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(request.getPassword());
        }

        User updatedUser = userRepository.save(user);

        return UserMapper.toResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with id : " + id));

        userRepository.delete(user);
    }
    @Override
    public UserResponse getCurrentUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with email: " + email
                        )
                );

        return UserMapper.toResponse(user);
    }
}