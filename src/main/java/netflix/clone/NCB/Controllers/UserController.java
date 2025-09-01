package netflix.clone.NCB.Controllers;

import netflix.clone.NCB.Components.JwtUtil;
import netflix.clone.NCB.DTOs.UserDTO;
import netflix.clone.NCB.Models.User;
import netflix.clone.NCB.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserService userService;
  private final JwtUtil jwtUtil = new JwtUtil();

  // Constructor injection of UserService
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
    User user = userService.register(userDTO);
    if (user != null) {
      return ResponseEntity.ok("User registered successfully");
    } else {
      return ResponseEntity.status(400).body("User registration failed");
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@RequestBody UserDTO userDTO) {
    boolean success = userService.login(userDTO.getEmail(), userDTO.getPassword());
    if (success) {
      String token = jwtUtil.generateToken(userDTO.getEmail());
      return ResponseEntity.ok().body(Map.of("token", token));
    } else {
      return ResponseEntity.status(401).body("Invalid credentials");
    }
  }

  @GetMapping("/all")
  public ResponseEntity<?> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @PatchMapping("/update")
  public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) {
    // Implementation for updating user details
    boolean success = userService.update(userDTO);
    return ResponseEntity.ok("User updated successfully");
  }

  @PatchMapping("/{email}/picture")
  public ResponseEntity<?> updateProfilePicture(@PathVariable String email, @RequestBody String profilePicture) {
    // Implementation for updating profile picture
    boolean success = userService.updateProfilePicture(email, profilePicture);
    return ResponseEntity.ok("Profile picture updated successfully");
  }
}
