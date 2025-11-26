package com.example.taskmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public String createAccount(@RequestBody CreateUserRequest request) {

        User user = new User(
                request.getFullName(),
                request.getEmail(),
                request.getUsername(),
                request.getPassword()
        );

        return userService.createAccount(user, request.getConfirmPassword());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userService.login(request.getUsername(), request.getPassword());
        if (user != null) {
            return ResponseEntity.ok(new UserResponse(user.getId(), user.getUsername(), user.getFullName(), user.getEmail()));
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @GetMapping("/all")
    public List<UserResponse> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getFullName(), user.getEmail()))
                .collect(Collectors.toList());
    }
}

// DTO class
class CreateUserRequest {
    private String fullName;
    private String email;
    private String username;
    private String password;
    private String confirmPassword;

    // Getters & Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}

class LoginRequest {
    private String username;
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

class UserResponse {
    private Long id;
    private String username;
    private String fullName;
    private String email;

    public UserResponse(Long id, String username, String fullName, String email) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
    }

    // Getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
}
