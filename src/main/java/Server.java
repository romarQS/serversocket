import logger.Logger;
import request.RequestHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    private static final int PORT = 8080;
    private static final String HOST = "localhost";

    public static void main(String[] args) {
        try(ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(HOST))) {
            while (true) {
                try (Socket socket = server.accept()) {
                    Logger.info(String.format("Accept connection on port %s", PORT));

                    RequestHandler.processRequest(socket, HOST, PORT);
                } catch (IOException e) {
                    Logger.error("Connection failed..", e);
                }
            }
        } catch (Exception e) {
            Logger.fatal("Received unchecked exception.", e);
        }
    }
}
