package lk.ijse.studentmanagement.service;

import lk.ijse.studentmanagement.dto.request.LoginRequest;
import lk.ijse.studentmanagement.dto.request.RegisterRequest;
import lk.ijse.studentmanagement.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
