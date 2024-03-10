package com.springboot.blog.service.impl;

import com.springboot.blog.entity.User;
import com.springboot.blog.exception.APIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.UserDto;
import com.springboot.blog.payload.UserResponse;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto getUserById(Long id) {
        return mapToDto(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id)));
    }

    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Username does not exist with {id} exists!."));
        userRepository.delete(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Username does not exist with {id} exists!."));
        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return mapToDto(userRepository.save(user));
    }

    @Override
    public UserDto getUserByUsername(String username) {
        return mapToDto(userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public UserDto getUserByEmail(String email) {
        return mapToDto(userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public UserDto getUserByName(String name) {
        return mapToDto(userRepository.findByName(name).orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public UserResponse getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<User> users = userRepository.findAll(pageable);
        List<UserDto> content = users.getContent().stream().map(this::mapToDto).toList();
        UserResponse userResponse = new UserResponse();
        userResponse.setContent(content);
        userResponse.setPageNo(pageNo);
        userResponse.setPageSize(pageSize);
        userResponse.setTotalElements(users.getTotalElements());
        userResponse.setTotalPages(users.getTotalPages());
        userResponse.setLast(users.isLast());

        return userResponse;
    }

    @Override
    public UserResponse searchUsers(UserDto userDto, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<User> users = userRepository.searchUsers(userDto, pageable);
        List<UserDto> content = users.getContent().stream().map(this::mapToDto).toList();
        UserResponse userResponse = new UserResponse();
        userResponse.setContent(content);
        userResponse.setPageNo(pageNo);
        userResponse.setPageSize(pageSize);
        userResponse.setTotalElements(users.getTotalElements());
        userResponse.setTotalPages(users.getTotalPages());
        userResponse.setLast(users.isLast());

        return userResponse;
    }

    private UserDto mapToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private User mapToEntity(UserDto user) {
        return modelMapper.map(user, User.class);
    }
}
