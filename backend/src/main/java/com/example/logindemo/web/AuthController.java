package com.example.logindemo.web;

import com.example.logindemo.model.User;
import com.example.logindemo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    record LoginRequest(String username, String password) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        return userRepository.findByUsername(req.username())
                .map(u -> {
                    // plaintext comparison for demo; use bcrypt in prod.
                    if (u.getPassword().equals(req.password())) {
                        // demo token encodes username|role
                        String token = u.getUsername() + "|" + u.getRole();
                        return ResponseEntity.ok(Map.of("token", token, "role", u.getRole()));
                    } else {
                        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
                    }
                })
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of("error", "Invalid credentials")));
    }

    // a small endpoint to create a user for testing
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody LoginRequest req) {
        if (userRepository.findByUsername(req.username()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "User exists"));
        }
        User u = new User();
        u.setUsername(req.username());
        u.setPassword(req.password());
        // demo: if username == 'admin' create admin role (insecure)
        if ("admin".equalsIgnoreCase(req.username())) {
            u.setRole("ADMIN");
        }
        userRepository.save(u);
        return ResponseEntity.ok(Map.of("ok", true));
    }
}
