package response;

import java.io.OutputStream;
import java.io.PrintWriter;

public class ResponseHandler {
    public static void writeResponse(OutputStream os, String host, int port, int statusCode,
                                     String responseBody, String mimeType) {
        PrintWriter pw = new PrintWriter(os);
        pw.println("HTTP/1.1 " + getHttpStatus(statusCode));
        pw.println(String.format("Via: HTTP/1.1 %s:%s", host, port));
        pw.println("Server: Java/11");
        pw.println("Content-type: " + mimeType);
        pw.println("Content-length: " + responseBody.toCharArray().length);
        pw.println("");
        pw.println(responseBody);
        pw.println("");
        pw.flush();
    }

    private static String getHttpStatus(int statusCode) {
        return switch (statusCode) {
            case 400 -> statusCode + " Bad Request";
            case 401 -> statusCode + " Unauthorized";
            case 500 -> statusCode + " Internal Server Error";
            case 200, default -> statusCode + " OK";
        };
    }
}
