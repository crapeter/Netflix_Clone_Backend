package netflix.clone.NCB.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class ProfileDTO {
  public int id;

  @NotBlank
  public String name;

  @NotBlank
  public int age;

  public String password;
}
