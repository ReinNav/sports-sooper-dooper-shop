package com.example.demo.port.user.controller;

import java.util.*;
import java.util.stream.Collectors;

import com.example.demo.port.utils.JwtUtils;
import com.example.demo.core.domain.model.ERole;
import com.example.demo.core.domain.model.Role;
import com.example.demo.core.domain.model.User;
import com.example.demo.core.domain.service.interfaces.RoleRepository;
import com.example.demo.core.domain.service.interfaces.UserRepository;
import com.example.demo.core.domain.service.impl.UserDetailsImpl;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody Map<String, Object> loginPayload) {
        String username = (String) loginPayload.get("username");
        String password = (String) loginPayload.get("password");

        Map<String, Object> response = new HashMap<>();

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        response.put("jwt", jwt);
        response.put("id", userDetails.getId());
        response.put("username", userDetails.getUsername());
        response.put("email", userDetails.getEmail());
        response.put("roles", roles);

        return ResponseEntity
                .ok(response);
    }



    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody Map<String, Object> signUpPayload) {
        String username = (String) signUpPayload.get("username");
        String email = (String) signUpPayload.get("email");
        String password = (String) signUpPayload.get("password");
        List<String> strRoles = (List<String>) signUpPayload.get("role");


        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(username, email,
                encoder.encode(password));

        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "User registered successfully."));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changeUserPassword(@Valid @RequestBody Map<String, Object> passwordChangeRequest) {
        String username = (String) passwordChangeRequest.get("username");
        String oldPassword = (String) passwordChangeRequest.get("oldPassword");
        String newPassword = (String) passwordChangeRequest.get("newPassword");


        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, oldPassword)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid old password."));
        }

        if (oldPassword.equals(newPassword)) {
            return ResponseEntity.badRequest().body(Map.of("message", "New password must be different from the old password."));
        }

        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User Not Found with username: " + username)
        );

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Password changed successfully."));
    }
/*
    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";

        Pattern pattern = Pattern.compile(regex);

        if (password == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }*/

    private String getInvalidErrorMessage() {
        return "Password must match the following rules: \n" +
                "Must have at least 8 Characters. \n" +
                "Must include a capital letter. \n" +
                "Consists at least one digit. \n" +
                "Need to have one special symbol (i.e., @, #, $, %, etc.) \n" +
                "Doesn't contain spaces.";
    }
}