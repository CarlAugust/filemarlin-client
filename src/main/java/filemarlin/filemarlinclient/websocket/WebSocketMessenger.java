package filemarlin.filemarlinclient.websocket;

import java.net.http.WebSocket;

public class WebSocketMessenger {

    private final WebSocket socket;
    private final WebSocketListener listener;

    public WebSocketMessenger(WebSocket webSocket, WebSocketListener webSocketListener) {
        socket = webSocket;
        listener = webSocketListener;
    }
}
