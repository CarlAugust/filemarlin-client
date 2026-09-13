package filemarlin.filemarlinclient.websocket;

import filemarlin.filemarlinclient.websocket.records.GetClientsRequest;
import tools.jackson.databind.ObjectMapper;

import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;

public class WebSocketMessenger {


    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WebSocket socket;
    private final WebSocketListener listener;

    public WebSocketMessenger(WebSocket webSocket, WebSocketListener webSocketListener) {
        socket = webSocket;
        listener = webSocketListener;
    }

    private void sendRequest(Object request) {
        socket.sendText(objectMapper.writeValueAsString(request), true);
    }

    public CompletableFuture<String[]> getClients() {

        var key_future = listener.registerRequest();
        var request = new GetClientsRequest("get-clients", new GetClientsRequest.ClientData(key_future.getKey()));
        sendRequest(request);
        return key_future.getValue().thenApply(obj -> (String[]) obj);
    }

}
