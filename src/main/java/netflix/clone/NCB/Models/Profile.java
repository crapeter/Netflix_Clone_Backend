package netflix.clone.NCB.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "profiles")
public class Profile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ProfileId", nullable = false)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "UserId")
  private netflix.clone.NCB.Models.User user;

  @Size(max = 50)
  @NotNull
  @Column(name = "ProfileName", nullable = false, length = 50)
  private String profileName;

  @Size(max = 255)
  @Column(name = "Password", nullable = false)
  private String password;

  @NotNull
  @Column(name = "Age", nullable = false)
  private Integer age;

}