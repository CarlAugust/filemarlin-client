package filemarlin.filemarlinclient.websocket;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.*;


/*
I definitly need to improve the error handling of this class and like the initilization
such that it reconnects...

Prompts you if the login does not work
What if the websocket does not connect ect...
 */

public class WebSocketManager {

    private static WebSocketManager instance;

    private final HttpClient httpClient;
    private final WebSocket serverSocket;
    private final WebSocketListener listener;
    private final WebSocketMessenger messenger;

    private WebSocketManager() throws IOException, InterruptedException {

        var cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
        httpClient = HttpClient.newBuilder()
                .cookieHandler(cookieManager)
                .build();

        var loginRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("username=admin&password=password"))
                .build();

        httpClient.send(loginRequest, HttpResponse.BodyHandlers.ofString());

        listener = new WebSocketListener();
        serverSocket = httpClient.newWebSocketBuilder().buildAsync(URI.create("ws://localhost:8080/ws"), listener).join();

        messenger = new WebSocketMessenger(serverSocket, listener);
    }


    public void close() {
        serverSocket.sendClose(WebSocket.NORMAL_CLOSURE, "");
    }

    // Single global access point
    public static synchronized WebSocketManager getInstance() throws IOException, InterruptedException {
        if (instance == null) {
            instance = new WebSocketManager();
        }
        return instance;
    }

    public WebSocketMessenger getMessenger() {
        return messenger;
    }
}
