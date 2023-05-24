package request;

import authentification.Authentication;
import authentification.domain.User;
import logger.Logger;
import response.ResponseHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

public class RequestHandler {
    public static void processRequest(Socket socket, String host, int port) throws IOException {
        try {
            HttpRequest httpRequest = getHttpRequest(socket.getInputStream());
            Map<String, String> headers = httpRequest.getRequestHeaders();

            if (Authentication.hasAuthentication(headers)){
                if(Authentication.isUserAuthenticated(headers)){
                    User currentUser = Authentication.currentUser;
                    Logger.fatal(String.format("Current user is %s : %s", currentUser.getLogin(),
                            currentUser.getPassword()));

                    logRequestData(httpRequest);

                    ResponseHandler.writeResponse(socket.getOutputStream(),
                            host, port, 200, httpRequest.getRequestBody(), "text/plain");
                } else {
                    Logger.fatal("Such user does not exist!");
                }
            } else {
                Logger.fatal("No Authentication!");
            }
        } catch (Exception e) {
            Logger.error("Error while processing request.", e);
            ResponseHandler.writeResponse(socket.getOutputStream(), host, port,
                    400, "error","text/plain");
        }
    }

    private static HttpRequest getHttpRequest(InputStream is) {
        Map<String, String> requestHeaders = RequestParser.parseRequestHeaders(is);

        Map<String, String> requestParameter = RequestParser.getRequestParameter(
                requestHeaders.get(HttpRequest.HTTP_HEADER_REQUEST_URI));

        String requestBody = RequestParser.getRequestBody(is);

        return new HttpRequest(requestHeaders, requestParameter, requestBody);
    }

    private static void logRequestData (HttpRequest httpRequest){
        Logger.info(String.format("Received request headers: %s", httpRequest.getRequestHeaders()));

        Logger.info(String.format("Received request parameter: %s", httpRequest.getRequestParameters()));

        Logger.info(String.format("Received request body: %s", httpRequest.getRequestBody()));

        Logger.info(String.format("Request: %s to %s.", httpRequest.getHttpMethod(),
                httpRequest.getRequestPath()));

        Logger.info(String.format("Request: %s", httpRequest.getRequestBody()));
    }
}
