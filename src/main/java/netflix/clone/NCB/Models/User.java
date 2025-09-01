package netflix.clone.NCB.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "UserID", nullable = false)
  private Integer id;

  @Size(max = 50)
  @NotNull
  @Column(name = "UserName", nullable = false, length = 50)
  private String userName;

  @Size(max = 100)
  @NotNull
  @Column(name = "Email", nullable = false, length = 100)
  private String email;

  @Size(max = 255)
  @NotNull
  @Column(name = "Password", nullable = false)
  private String password;

  @Size(max = 15)
  @Column(name = "PhoneNum", length = 15)
  private String phoneNum;

  @Column(name = "DOB")
  private LocalDate dob;

  @Column(name = "Admin")
  private Boolean admin;

  @Size(max = 255)
  @Column(name = "ProfilePicture")
  private String profilePicture;

  @OneToMany(mappedBy = "user")
  private Set<Profile> profiles = new LinkedHashSet<>();

  @OneToMany(mappedBy = "userID")
  private Set<netflix.clone.NCB.Models.Viewinghistory> viewinghistories = new LinkedHashSet<>();

}