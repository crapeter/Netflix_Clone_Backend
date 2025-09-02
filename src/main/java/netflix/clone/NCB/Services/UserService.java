package netflix.clone.NCB.Services;

import netflix.clone.NCB.DTOs.UserDTO;
import netflix.clone.NCB.Models.Profile;
import netflix.clone.NCB.Models.User;
import netflix.clone.NCB.Repos.ProfileRepo;
import netflix.clone.NCB.Repos.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.List;

/**
 * User roles are "User" and "Admin"
*/

@Service
public class UserService {
  private final UserRepo userRepo;
  private final ProfileRepo profileRepo;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, ProfileRepo profileRepo) {
    this.userRepo = userRepo;
    this.passwordEncoder = passwordEncoder;
    this.profileRepo = profileRepo;
  }

  public User register(UserDTO newUser) {
    // Check if user with the same email already exists
    if (userRepo.findByEmail(newUser.getEmail()).isPresent()) {
      throw new IllegalArgumentException("Email already in use");
    }
    User user1 = new User();
    user1.setUserName(newUser.getUserName());
    user1.setEmail(newUser.getEmail());
    user1.setPassword(passwordEncoder.encode(newUser.getPassword()));
    user1.setPhoneNum(newUser.getPhoneNum());
    user1.setDob(newUser.getDob());
    user1.setAdmin(false);
    userRepo.save(user1);

    // find the users age
    int usersAge = Period.between(newUser.getDob(), java.time.LocalDate.now()).getYears();

    // add default profile
    Profile profile = new Profile();
    profile.setUser(user1);
    profile.setProfileName(user1.getUserName());
    profile.setAge(usersAge);
    profile.setPassword("password");
    profileRepo.save(profile);
    return user1;
  }

  public boolean login(String email, String password) {
    return userRepo.findByEmail(email)
        .map(user -> passwordEncoder.matches(password, user.getPassword()))
        .orElse(false);
  }

  public List<User> getAllUsers() {
    return userRepo.findAll();
  }

  public boolean update(UserDTO userDTO) {
    User user = userRepo.findByEmail(userDTO.getEmail()).orElse(null);
    if (user != null) {
      if (userDTO.getUserName() != null) user.setUserName(userDTO.getUserName());
      if (userDTO.getPassword() != null) user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
      if (userDTO.getPhoneNum() != null) user.setPhoneNum(userDTO.getPhoneNum());
      if (userDTO.getDob() != null) user.setDob(userDTO.getDob());
      userRepo.save(user);
      return true;
    }
    return false;
  }

  public boolean updateProfilePicture(String email, String newProfilePicture) {
    User user = userRepo.findByEmail(email).orElse(null);
    if (user != null) {
      user.setProfilePicture(newProfilePicture);
      userRepo.save(user);
      return true;
    }
    return false;
  }
}
