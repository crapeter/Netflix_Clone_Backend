package netflix.clone.NCB.Controllers;

import netflix.clone.NCB.DTOs.ProfileDTO;
import netflix.clone.NCB.Services.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
  private final ProfileService profileService;

  public ProfileController(ProfileService profileService) {
    this.profileService = profileService;
  }

  @GetMapping("/{email}")
  public List<ProfileDTO> getProfilesByEmail(@PathVariable String email) {
    return profileService.getProfilesByEmail(email);
  }

  @PostMapping("/add/{email}")
  public ResponseEntity<?> addProfile(@PathVariable String email, @RequestBody ProfileDTO profileDTO) {
    return profileService.addProfile(email, profileDTO) ?
        ResponseEntity.ok("Profile added successfully") :
        ResponseEntity.status(400).body("Failed to add profile");
  }

  @PostMapping("/verifyPassword/{id}")
  public ResponseEntity<?> verifyPassword(@PathVariable int id, @RequestParam String password) {
    return profileService.verifyPassword(id, password) ?
        ResponseEntity.ok("Password is correct") :
        ResponseEntity.status(400).body("Incorrect password");
  }
}
