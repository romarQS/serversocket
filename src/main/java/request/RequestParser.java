package request;

import logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {

    public static Map<String, String> parseRequestHeaders(InputStream is) {
        Map<String, String> requestHeader = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        boolean isFirstLine = true;

        try {
            do {
                char readChar = (char) is.read();
                sb.append(readChar);

                if (readChar == '\n') {
                    String currentLine = sb.toString();

                    if (currentLine.isBlank()) {
                        break;
                    }

                    if (isFirstLine) {
                        isFirstLine = false;
                        parseRequestLine(requestHeader, currentLine);
                    } else {
                        parseHttpRequestHeader(requestHeader, currentLine);
                    }

                    sb = new StringBuilder();
                }
            } while (is.available() > 0);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return requestHeader;
    }

    private static void parseHttpRequestHeader(Map<String, String> requestHeaders, String currentLine) {
        if (currentLine.isBlank()) {
            Logger.warn("Given line was empty.");
            return;
        }
        if (!currentLine.contains(":")) {
            Logger.warn(String.format("Unknown header parameter found %s", currentLine));
        }

        String[] header = currentLine.split(":",2);
        String value = String.join(":", Arrays.asList(Arrays.copyOfRange(header, 1, header.length)));
        requestHeaders.put(header[0].trim(), value.trim());
    }

    private static void parseRequestLine(Map<String, String> requestHeaders, String requestLine) {
        String[] requestLineTokenized = requestLine.split(" ");

        final String httpMethod = requestLineTokenized[0].trim().toUpperCase();

        requestHeaders.put(HttpRequest.HTTP_HEADER_HTTP_METHOD, httpMethod);
        requestHeaders.put(HttpRequest.HTTP_HEADER_REQUEST_URI, requestLineTokenized[1].trim());
        requestHeaders.put(HttpRequest.HTTP_HEADER_HTTP_VERSION, requestLineTokenized[2].trim());
    }

    public static Map<String, String> getRequestParameter(String requestUri) {
        if (!requestUri.contains("?")) {
            Logger.warn(String.format("No request parameter found in URI: %s", requestUri));
            return Map.of();
        }

        String requestParams = requestUri.substring(requestUri.indexOf("?") + 1);

        if (requestParams.isBlank()) {
            Logger.warn(String.format("Invalid request URI found: %s", requestUri));
            return Map.of();
        }
        HashMap<String,String> parameterMap = new HashMap<>();
        final String[] splitParameter = requestParams.split("&");
        for(String keyValue : splitParameter){
            final String[] keyValueArray = keyValue.split("=");
            if(keyValueArray.length == 2){
                parameterMap.put(keyValueArray[0],keyValueArray[1]);
            }
        }

        return parameterMap;
    }

    public static String getRequestBody(InputStream is) {

        StringBuilder sb = new StringBuilder();

        try {
            while (is.available() > 0){
                sb.append((char) is.read());
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return sb.toString();
    }
}
