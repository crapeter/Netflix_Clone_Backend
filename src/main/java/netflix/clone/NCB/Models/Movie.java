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
@Table(name = "movies")
public class Movie {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "MovieID", nullable = false)
  private Integer id;

  @Size(max = 100)
  @NotNull
  @Column(name = "Title", nullable = false, length = 100)
  private String title;

  @Lob
  @Column(name = "Description")
  private String description;

  @Column(name = "ReleaseDate")
  private LocalDate releaseDate;

  @Size(max = 50)
  @Column(name = "Genre", length = 50)
  private String genre;

  @Size(max = 10)
  @Column(name = "Rating", length = 10)
  private String rating;

  @Size(max = 255)
  @Column(name = "MoviePoster")
  private String moviePoster;

  @Size(max = 255)
  @Column(name = "VideoURL")
  private String videoURL;

  @OneToMany(mappedBy = "movieID")
  private Set<Viewinghistory> viewinghistories = new LinkedHashSet<>();

}