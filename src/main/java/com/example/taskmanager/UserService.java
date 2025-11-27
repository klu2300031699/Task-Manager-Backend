package com.example.taskmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String createAccount(User user, String confirmPassword) {

        if (!user.getPassword().equals(confirmPassword)) {
            return "Password and Confirm Password do not match!";
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Email already exists!";
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Username already exists!";
        }

        userRepository.save(user);
        return "Account Created Successfully!";
    }

    public User login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.orElse(null);
    }

    public String updateProfile(Long userId, String fullName, String username, String email) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return "User not found!";
        }

        User user = userOpt.get();

        // Check if new username is already taken by another user
        if (!user.getUsername().equals(username)) {
            Optional<User> existingUser = userRepository.findByUsername(username);
            if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
                return "Username already exists!";
            }
        }

        // Check if new email is already taken by another user
        if (!user.getEmail().equals(email)) {
            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
                return "Email already exists!";
            }
        }

        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        userRepository.save(user);

        return "Profile updated successfully!";
    }

    public String changePassword(Long userId, String currentPassword, String newPassword, String confirmPassword) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return "User not found!";
        }

        User user = userOpt.get();

        if (!user.getPassword().equals(currentPassword)) {
            return "Current password is incorrect!";
        }

        if (!newPassword.equals(confirmPassword)) {
            return "New password and confirm password do not match!";
        }

        if (newPassword.length() < 6) {
            return "Password must be at least 6 characters long!";
        }

        user.setPassword(newPassword);
        userRepository.save(user);

        return "Password changed successfully!";
    }
}
