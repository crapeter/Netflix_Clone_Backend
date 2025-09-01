package netflix.clone.NCB.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class UserDTO {

  private String userName;

  @NotBlank
  private String email;

  @NotBlank
  private String password;

  private String phoneNum;

  private LocalDate dob;
}
