package netflix.clone.NCB.Repos;

import netflix.clone.NCB.Models.Viewinghistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewingHistoryRepo extends JpaRepository<Viewinghistory, Long> {
}
