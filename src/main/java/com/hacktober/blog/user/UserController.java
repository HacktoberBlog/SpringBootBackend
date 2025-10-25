package com.hacktober.blog.user;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.hacktober.blog.exceptions.ResourceNotFoundException;
import com.hacktober.blog.user.dto.UserDto;
import com.hacktober.blog.user.dto.UserMapper;
import com.hacktober.blog.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Users", description = "Endpoints for managing platform users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/create")
    @Operation(summary = "Create user", description = "Persist a new user profile in the database.")
    public ResponseEntity<ApiResponse<String>> createUser(@RequestBody UserDto user) throws InterruptedException, ExecutionException {
        String result = userService.create(userMapper.fromDtoToEntity(user));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(result, "User created successfully"));
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get user", description = "Retrieve a single user by username.")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable String username) throws InterruptedException, ExecutionException {
        User user = userService.getByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User with username '" + username + "' not found");
        }
        return ResponseEntity.ok(ApiResponse.success(user, "User retrieved successfully"));
    }

    @GetMapping("/all")
    @Operation(summary = "List users", description = "Retrieve all registered users.")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() throws InterruptedException, ExecutionException {
        List<UserDto> users = userService.getAll().stream()
                .map(userMapper::fromEntityToDto)
                .toList();       
         return ResponseEntity.ok(ApiResponse.success(users, "Users retrieved successfully"));
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update user", description = "Update a user's profile data, keeping the username immutable.")
    public ResponseEntity<ApiResponse<String>> updateUser(@PathVariable String username, @RequestBody User user) throws InterruptedException, ExecutionException {
        user.setUsername(username); // ensure username remains the document ID
        String result = userService.update(user);
        return ResponseEntity.ok(ApiResponse.success(result, "User updated successfully"));
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete user", description = "Remove a user and their profile information.")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable String username) throws InterruptedException, ExecutionException {
        String result = userService.delete(username);
        return ResponseEntity.ok(ApiResponse.success(result, "User deleted successfully"));
    }
}
