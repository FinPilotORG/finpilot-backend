package com.finpilot.dto;

public class RegisterResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String role;
    private String message;

    public RegisterResponse() {
    }

    public RegisterResponse(
            Long id,
            String firstName,
            String lastName,
            String email,
            String mobileNumber,
            String role,
            String message) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.role = role;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getRole() {
        return role;
    }

    public String getMessage() {
        return message;
    }
}