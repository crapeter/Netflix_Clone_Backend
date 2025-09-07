package netflix.clone.NCB.Controllers;

import netflix.clone.NCB.Components.HmacUtil;
import netflix.clone.NCB.Components.JwtUtil;
import netflix.clone.NCB.DTOs.MovieDTO;
import netflix.clone.NCB.Services.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
  private final MovieService movieService;

  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  @PostMapping("/add")
  public ResponseEntity<?> addMovie(@RequestBody MovieDTO movieDTO) {
    return movieService.addMovie(movieDTO);
  }

  @GetMapping("/all")
  public ResponseEntity<?> getAllMovies() {
    return movieService.getMovies();
  }

  @GetMapping("/stream/{filename}")
  public ResponseEntity<?> streamMovie(
      @PathVariable String filename,
      @RequestParam("expires") long expiresAt,
      @RequestParam("signature") String signature,
      @RequestHeader(value = "Range", required = false) String range)
      throws IOException {
    if (Instant.now().getEpochSecond() > expiresAt) {
      return ResponseEntity.status(401).body("Expired URL");
    }

    if (!HmacUtil.verify(filename + expiresAt, signature, System.getenv("SECRET_KEY"))) {
      return ResponseEntity.status(401).body("Invalid signature");
    }

    return movieService.streamMovie(filename, range);
  }

  @GetMapping("/secure-url/{filename}")
  public ResponseEntity<Map<String, String>> getSecureUrl(@PathVariable String filename,
                                                          @RequestHeader("Authorization") String authHeader) {
    String token = authHeader.substring(7);

    JwtUtil jwtUtil = new JwtUtil();
    if (!jwtUtil.validateToken(token)) {
      return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }

    String signedUrl = movieService.generateSignedUrl(filename, 5 * 60);

    Map<String, String> response = Map.of("signedUrl", signedUrl);
    return ResponseEntity.ok(response);
  }
}
