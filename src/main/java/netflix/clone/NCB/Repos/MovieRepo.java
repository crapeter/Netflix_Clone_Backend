package netflix.clone.NCB.Repos;

import netflix.clone.NCB.Models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepo extends JpaRepository<Movie, Long> {
  Optional<Movie> findById(Integer id);
}
