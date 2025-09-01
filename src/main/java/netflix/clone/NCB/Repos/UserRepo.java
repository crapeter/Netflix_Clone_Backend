package netflix.clone.NCB.Repos;

import netflix.clone.NCB.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
}
