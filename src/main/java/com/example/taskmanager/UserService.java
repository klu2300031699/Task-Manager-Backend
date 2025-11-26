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
}
