package com.emmyscode.spendle.service;

import com.emmyscode.spendle.dto.UserRequestDTO;
import com.emmyscode.spendle.dto.UserResponseDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO requestDTO);

    UserResponseDTO getUserById(UUID id);

    UserResponseDTO getUserByEmail(String email);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO updateUser(UUID id, UserRequestDTO requestDTO);

    void deleteUser(UUID id);
}
