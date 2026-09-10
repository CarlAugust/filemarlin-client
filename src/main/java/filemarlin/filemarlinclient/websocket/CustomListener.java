package filemarlin.filemarlinclient.websocket;

import java.net.URI;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

public class CustomListener implements WebSocket.Listener {

    @Override
    public void onOpen(WebSocket webSocket) {
        System.out.println("Connected to websockets");
        webSocket.sendText("Meow", true);
        WebSocket.Listener.super.onOpen(webSocket);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        System.out.println("Received message:\n" + data);
        return WebSocket.Listener.super.onText(webSocket, data, last);
    }
}
