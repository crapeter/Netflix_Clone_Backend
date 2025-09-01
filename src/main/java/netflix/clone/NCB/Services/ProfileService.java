package netflix.clone.NCB.Services;

import netflix.clone.NCB.DTOs.ProfileDTO;
import netflix.clone.NCB.Models.Profile;
import netflix.clone.NCB.Models.User;
import netflix.clone.NCB.Repos.ProfileRepo;
import netflix.clone.NCB.Repos.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {
  private final UserRepo userRepo;
  private final ProfileRepo profileRepo;
  private final PasswordEncoder passwordEncoder;

  public ProfileService(UserRepo userRepo , ProfileRepo profileRepo, PasswordEncoder passwordEncoder) {
    this.userRepo = userRepo;
    this.profileRepo = profileRepo;
    this.passwordEncoder = passwordEncoder;
  }

  public List<ProfileDTO> getProfilesByEmail(String email) {
    User user = userRepo.findByEmail(email).orElse(null);
    if (user != null) {
      List<Profile> profiles = profileRepo.findByUserId(user.getId());
      return profileMapper(profiles);
    }
    return null;
  }

  private List<ProfileDTO> profileMapper(List<Profile> profiles) {
    return profiles.stream().map(profile -> {
      ProfileDTO dto = new ProfileDTO();
      dto.setId(profile.getId());
      dto.setName(profile.getProfileName());
      dto.setAge(profile.getAge());
      return dto;
    }).toList();
  }

  public boolean addProfile(String email, ProfileDTO profileDTO) {
    User user = userRepo.findByEmail(email).orElse(null);
    if (user != null) {
      Profile profile = new Profile();
      profile.setUser(user);
      profile.setProfileName(profileDTO.getName());
      profile.setAge(profileDTO.getAge());
      profile.setPassword(passwordEncoder.encode(profileDTO.getPassword()));
      profileRepo.save(profile);
      return true;
    }
    return false;
  }

  public boolean verifyPassword(int id, String password) {
    Profile profile = profileRepo.findById(id);
    if (profile != null) {
      return passwordEncoder.matches(password, profile.getPassword());
    }
    return false;
  }
}
