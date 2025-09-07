package netflix.clone.NCB.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter

public class MovieDTO {
  @NotNull
  private String title;

  @NotNull
  private String director;

  private String description;

  private LocalDate releaseDate;

  @NotNull
  private String genre;

  private String rating;

  private String moviePoster;

  private String videoURL;
}
