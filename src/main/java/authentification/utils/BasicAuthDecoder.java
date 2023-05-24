package authentification.utils;

import java.util.Base64;

public class BasicAuthDecoder {
    public static String decodeBasicAuth(String authHeader) {
        String encodedCredentials = authHeader.substring("Basic ".length()).trim();
        byte[] decodedBytes = Base64.getDecoder().decode(encodedCredentials);
        return new String(decodedBytes);
    }
}
