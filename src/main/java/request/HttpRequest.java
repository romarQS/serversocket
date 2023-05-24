package request;

import lombok.Getter;

import java.util.Map;

@Getter
public class HttpRequest {

    public static final String HTTP_HEADER_HTTP_METHOD = "HTTP-Method";

    public static final String HTTP_HEADER_HTTP_VERSION = "HTTP-Version";

    public static final String HTTP_HEADER_REQUEST_URI = "HTTP-RequestURI";

    private final Map<String, String> requestHeaders;

    private final Map<String, String> requestParameters;

    private final String requestBody;

    public HttpRequest(Map<String, String> requestHeaders, Map<String, String> requestParameters, String requestBody) {
        this.requestHeaders = requestHeaders;
        this.requestParameters = requestParameters;
        this.requestBody = requestBody;
    }

    public String getRequestPath() {
        return requestHeaders.get(HTTP_HEADER_REQUEST_URI);
    }

    public String getHttpMethod() {
        return requestHeaders.get(HTTP_HEADER_HTTP_METHOD);
    }
}
