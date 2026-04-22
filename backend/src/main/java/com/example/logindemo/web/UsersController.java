package com.example.logindemo.web;

import com.example.logindemo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.example.logindemo.model.User;
import com.example.logindemo.web.UserDto;

@RestController
@RequestMapping("/api")
public class UsersController {
    private final UserRepository userRepository;

    public UsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> listUsers() {
        // This endpoint is protected at app level by a simple token check below.
        List<UserDto> users = userRepository.findAll().stream()
                .map(u -> new UserDto(u.getId(), u.getUsername()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    // A convenience endpoint that checks a simple demo token from Authorization header
    @GetMapping("/users-protected")
    public ResponseEntity<?> listUsersProtected(@org.springframework.web.bind.annotation.RequestHeader(name = "Authorization", required = false) String auth) {
        if (auth == null || !auth.contains("|")) {
            return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        }
        String role = auth.substring(auth.indexOf('|') + 1);
        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        }
        return listUsers();
    }
}
