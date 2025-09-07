package netflix.clone.NCB.Components;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class HmacUtil {
  private static final String HMAC_ALGO = "HmacSHA256";

  // Generate HMAC signature
  public static String sign(String data, String secretKey) {
    try {
      Mac mac = Mac.getInstance(HMAC_ALGO);
      SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), HMAC_ALGO);
      mac.init(keySpec);
      byte[] rawHmac = mac.doFinal(data.getBytes());
      return Base64.getUrlEncoder().withoutPadding().encodeToString(rawHmac);
    } catch (Exception e) {
      throw new RuntimeException("Failed to generate HMAC", e);
    }
  }

  // Verify HMAC signature
  public static boolean verify(String data, String signature, String secretKey) {
    String expected = sign(data, secretKey);
    return expected.equals(signature);
  }
}
