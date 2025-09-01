package netflix.clone.NCB.Repos;

import netflix.clone.NCB.Models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepo extends JpaRepository<Movie, Long> {
}
