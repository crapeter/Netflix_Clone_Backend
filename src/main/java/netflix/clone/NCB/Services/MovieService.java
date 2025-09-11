package netflix.clone.NCB.Services;

import netflix.clone.NCB.Components.HmacUtil;
import netflix.clone.NCB.DTOs.MovieDTO;
import netflix.clone.NCB.Models.Movie;
import netflix.clone.NCB.Repos.MovieRepo;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.logging.Logger;

@Service
public class MovieService {
//  private static final Logger logger = Logger.getLogger(MovieService.class.getName());
  private static final long CHUNK_SIZE = 1024 * 1024;
  private final MovieRepo movieRepo;
  private final String[] movieCategories = {
      "Children & Family",
      "Action & Adventure",
      "Comedies",
      "Sci-Fi",
      "Fantasy",
      "Romance",
      "Dramas",
      "Western",
      "Horror",
      "Thrillers",
      "Classic",
      "Documentaries",
      "Anime",
      "Foreign",
      "Independent",
      "LGBTQ+",
      "Music",
      "Sports",
      "TV Shows",
      "Teen TV Shows",
      "Christmas",
      "Others",
  };

  public MovieService(MovieRepo movieRepo) {
    this.movieRepo = movieRepo;
  }

  public ResponseEntity<Resource> streamMovie(String filename, String rangeHeader) throws IOException {
    Path moviePath = Paths.get("../Movies").resolve(filename).toAbsolutePath();
    File movieFile = moviePath.toFile();

    if (!movieFile.exists()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    long fileLength = movieFile.length();
    long start = 0;
    long end = fileLength - 1;

    if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
      String[] ranges = rangeHeader.substring(6).split("-");
      try {
        start = Long.parseLong(ranges[0]);
        if (ranges.length > 1 && !ranges[1].isEmpty()) {
          end = Long.parseLong(ranges[1]);
        } else {
          end = Math.min(start + CHUNK_SIZE - 1, end);
        }
      } catch (NumberFormatException e) {
        start = 0;
        end = fileLength - 1;
      }
    }

    if (end >= fileLength) {
      end = fileLength - 1;
    }

    long contentLength = end - start + 1;

    InputStream inputStream = new BufferedInputStream(new FileInputStream(movieFile));
    inputStream.skip(start);

    Resource resource = new InputStreamResource(new LimitedInputStream(inputStream, contentLength));

    String contentType = Files.probeContentType(moviePath);
    if (contentType == null) {
      contentType = "application/octet-stream";
    }

    return ResponseEntity.status(rangeHeader == null ? HttpStatus.OK : HttpStatus.PARTIAL_CONTENT)
        .header(HttpHeaders.CONTENT_TYPE, contentType)
        .header(HttpHeaders.ACCEPT_RANGES, "bytes")
        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength))
        .header(HttpHeaders.CONTENT_RANGE, String.format("bytes %d-%d/%d", start, end, fileLength))
        .body(resource);
  }


  public ResponseEntity<?> addMovie(MovieDTO movieDTO) {
    Movie movie = mapToMovie(movieDTO);
    try {
      movieRepo.save(movie);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    return ResponseEntity.ok(Map.of("message", "Movie added successfully"));
  }

  private Movie mapToMovie(MovieDTO movieDTO) {
    Movie movie = new Movie();
    movie.setMoviePoster(movieDTO.getMoviePoster());
    movie.setDescription(movieDTO.getDescription());
    movie.setRating(movieDTO.getRating());
    movie.setDirector(movieDTO.getDirector());
    movie.setReleaseDate(movieDTO.getReleaseDate());
    movie.setTitle(movieDTO.getTitle());
    movie.setVideoURL(movieDTO.getVideoURL());
    movie.setGenre(movieDTO.getGenre());
    return movie;
  }

  public ResponseEntity<?> getMovies() {
    Movie[] movies = movieRepo.findAll().toArray(Movie[]::new);
    return ResponseEntity.ok(movies);
  }

  public String generateSignedUrl(String filename, int i) {
    long expiresAt = Instant.now().getEpochSecond() + i;
    String signature = HmacUtil.sign(filename + expiresAt, System.getenv("SECRET_KEY"));
    return String.format("/api/movies/stream/%s?expires=%d&signature=%s", filename, expiresAt, signature);
  }

  public Resource getMoviePoster(String filename) throws IOException {
    Path posterPath = Paths.get("../MoviePosters").resolve(filename).normalize().toAbsolutePath();
    return new UrlResource(posterPath.toUri());
  }
}
