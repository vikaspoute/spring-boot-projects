package com.springboot.blog.service;

import com.springboot.blog.payload.UserDto;
import com.springboot.blog.payload.UserResponse;

public interface UserService {
    UserDto getUserById(Long id);

    void deleteUserById(Long id);

    UserDto updateUser(UserDto user, Long id);

    UserDto getUserByUsername(String username);

    UserDto getUserByEmail(String email);

    UserDto getUserByName(String name);

    UserResponse getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir);

    UserResponse searchUsers(UserDto userDto, int pageNo, int pageSize, String sortBy, String sortDir);
}
