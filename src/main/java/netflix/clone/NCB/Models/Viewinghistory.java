package netflix.clone.NCB.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "viewinghistory")
public class Viewinghistory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "HistoryID", nullable = false)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "UserID")
  private User userID;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MovieID")
  private Movie movieID;

  @Column(name = "WatchDate")
  private Instant watchDate;

  @ColumnDefault("0")
  @Column(name = "Progress")
  private Integer progress;

}