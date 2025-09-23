package com.examly.springapp.controller;

import com.examly.springapp.model.User;
import com.examly.springapp.repository.UserRepository;
import com.examly.springapp.service.QRCodeService;
import com.examly.springapp.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    private final UserService userService;

    private final QRCodeService qrCodeService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User saved = userService.registerUser(user);
            return ResponseEntity.ok("Registration successful! Verification email sent.");
        } catch (MessagingException e) {
            return ResponseEntity.internalServerError().body("Failed to send email.");
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        boolean verified = userService.verifyToken(token);
        return verified
                ? ResponseEntity.ok("Email verified successfully!")
                : ResponseEntity.badRequest().body("Invalid or expired token.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String email, @RequestParam String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getPassword().equals(password)) {

                if (user.isVerified()) {
                    return ResponseEntity.ok(user); // user is returned as JSON
                } else {
                    return ResponseEntity.status(403).body("User not verified");
                }

            } else {
                return ResponseEntity.status(401).body("Invalid password");
            }
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElseGet(
                        (Supplier<? extends ResponseEntity<User>>) ResponseEntity.status(404).body("User not found"));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable Long id, @RequestBody User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setVerified(updatedUser.isVerified());
            existingUser.setRole(updatedUser.getRole()); // ✅ Update role

            userRepository.save(existingUser);
            return ResponseEntity.ok("User updated successfully");
        }).orElse(ResponseEntity.status(404).body("User not found"));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    @GetMapping("/users/qrcode/{id}")
    public ResponseEntity<byte[]> generateUserQRCode(@PathVariable Long id) {
        return (ResponseEntity<byte[]>) userRepository.findById(id)
                .map(user -> {
                    try {
                        String qrContent = "https://8081-cbaacefbecfdaacedbdfdaffabfbdede.premiumproject.examly.io/profilepage/"
                                + user.getId();

                        byte[] qrImage = qrCodeService.generateQRCode(qrContent, 250, 250);

                        return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION,
                                        "inline; filename=\"user-" + user.getId() + "-qrcode.png\"")
                                .contentType(MediaType.IMAGE_PNG)
                                .body(qrImage);
                    } catch (Exception e) {
                        return ResponseEntity.internalServerError().build();
                    }
                })
                .orElse(ResponseEntity.status(404).body(null));
    }

    @PostMapping("/users/qrcode/verify")
    public ResponseEntity<?> verifyQRCode(@RequestParam String qrContent) {
        try {
            Long userId;

            if (qrContent.contains("/profilepage/")) {
                String idStr = qrContent.substring(qrContent.lastIndexOf("/") + 1);
                userId = Long.parseLong(idStr);
            } else {
                userId = Long.parseLong(qrContent);
            }

            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                return ResponseEntity.ok(optionalUser.get());
            } else {
                return ResponseEntity.status(404).body("User not found");
            }

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid QR code content");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to process QR code");
        }
    }
    
}

