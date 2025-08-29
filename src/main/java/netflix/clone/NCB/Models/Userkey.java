package netflix.clone.NCB.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "userkeys")
public class Userkey {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "KeyID", nullable = false)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "UserID")
  private User userID;

  @Size(max = 255)
  @Column(name = "Salt")
  private String salt;

}