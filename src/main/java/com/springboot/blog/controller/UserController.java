package com.springboot.blog.controller;

import com.springboot.blog.payload.UserDto;
import com.springboot.blog.payload.UserResponse;
import com.springboot.blog.service.UserService;
import com.springboot.blog.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Tag(
        name = "CRUD REST APIs for User Resource"
)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

/*    @Operation(
            summary = "Create User",
            description = "Create a new user in the system."
    )
    @ApiResponse(
            responseCode = "201",
            description = "User created successfully."
    )
    @SecurityRequirement(
            name = "Bearer"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }*/

    @Operation(
            summary = "Get All Users",
            description = "Retrieve all users registered in the system."
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of users fetched successfully."
    )
    @GetMapping
    public ResponseEntity<UserResponse> getAllUsers(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        UserResponse userResponse = userService.getAllUsers(pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(userResponse);
    }

    @Operation(
            summary = "Get All Users with search criteria",
            description = "Retrieve all users registered in the system."
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of users fetched successfully."
    )
    @GetMapping("/mixed")
    public ResponseEntity<UserResponse> getAllUsersWithSearchCriteria(
            @RequestBody @Nullable UserDto userDto,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        UserResponse userResponse = userService.searchUsers(userDto, pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(userResponse);
    }

    @Operation(
            summary = "Get User By Id",
            description = "Retrieve a user by their unique identifier."
    )
    @ApiResponse(
            responseCode = "200",
            description = "User retrieved successfully."
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(
            summary = "Update User",
            description = "Update an existing user's information."
    )
    @ApiResponse(
            responseCode = "200",
            description = "User updated successfully."
    )
    @SecurityRequirement(
            name = "Bearer"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable(name = "id") long id) {

        UserDto updatedUser = userService.updateUser(userDto, id);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete User",
            description = "Delete a user from the system."
    )
    @ApiResponse(
            responseCode = "200",
            description = "User deleted successfully."
    )
    @SecurityRequirement(
            name = "Bearer"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") long id) {

        userService.deleteUserById(id);

        return new ResponseEntity<>("User deleted successfully.", HttpStatus.OK);
    }
}
