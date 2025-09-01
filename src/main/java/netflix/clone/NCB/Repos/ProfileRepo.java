package netflix.clone.NCB.Repos;

import netflix.clone.NCB.Models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepo extends JpaRepository<Profile, Long> {
  List<Profile> findByUserId(Integer id);
  Profile findById(Integer id);
}
