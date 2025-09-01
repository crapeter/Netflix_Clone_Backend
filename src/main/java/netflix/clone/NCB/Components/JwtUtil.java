package netflix.clone.NCB.Components;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
  private final String SECRET_KEY = System.getenv("SECRET_KEY");
  private final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(
      Base64.getEncoder().encodeToString(SECRET_KEY.getBytes())
  ));
  private final long expiration = 3600000; // 1 hour in milliseconds

  public String generateToken(String subj) {
    return Jwts.builder()
        .setSubject(subj)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(key)
        .compact();
  }

  public String extractEmail(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (Exception ignored) {
      return false;
    }
  }
}
