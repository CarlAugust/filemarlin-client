package filemarlin.filemarlinclient.websocket;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.net.http.WebSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class WebSocketMessenger {


    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WebSocket socket;
    private final WebSocketListener listener;

    public WebSocketMessenger(WebSocket webSocket, WebSocketListener webSocketListener) {
        socket = webSocket;
        listener = webSocketListener;
    }

    public CompletableFuture<JsonNode> getClients() {
        var request = new HashMap<>();
        request.put("type", "get-clients");

        var clientData = new HashMap<>();
        var key_future = listener.registerRequest();
        clientData.put("requestId", key_future.getKey());
        request.put("client-data", clientData);

        socket.sendText(objectMapper.writeValueAsString(request), true);
        return key_future.getValue();
    }
}
