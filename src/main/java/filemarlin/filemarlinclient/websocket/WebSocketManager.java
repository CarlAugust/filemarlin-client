package filemarlin.filemarlinclient.websocket;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.CompletionException;

public class WebSocketManager {

    private HttpClient httpClient;
    private WebSocket serverSocket;


    public WebSocketManager() throws IOException, InterruptedException {

        var cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
        httpClient = HttpClient.newBuilder()
                .cookieHandler(cookieManager)
                .build();

        var loginRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("username=admin&password=password"))
                .build();

        HttpResponse<String> response = httpClient.send(loginRequest, HttpResponse.BodyHandlers.ofString());
        serverSocket = httpClient.newWebSocketBuilder().buildAsync(URI.create("ws://localhost:8080/ws"), new CustomListener()).join();
    }

    public void close() {
        serverSocket.sendClose(WebSocket.NORMAL_CLOSURE, "");
    }


}
