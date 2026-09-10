package filemarlin.filemarlinclient.websocket;

import javafx.util.Pair;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.http.WebSocket;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


public class WebSocketListener implements WebSocket.Listener {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ConcurrentHashMap<String, CompletableFuture<JsonNode>> pendingRequests = new ConcurrentHashMap<>();

    public Pair<String, CompletableFuture<JsonNode>> registerRequest() {
        String id = UUID.randomUUID().toString();
        CompletableFuture<JsonNode> future = new CompletableFuture<>();

        future.orTimeout(5, TimeUnit.SECONDS)
                .whenComplete((res, ex) -> pendingRequests.remove(id));

        pendingRequests.put(id, future);
        return new Pair<>(id, future);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {

        try {
            var payload = objectMapper.readTree(data.toString());
            var type = payload.get("type").asString();

            switch (type) {
                case "webrtc-signal" -> {

                }
                case "get-clients" -> {
                    var clientData = payload.get("client-data");
                    var requestId = clientData.get("requestId").asString();

                    var clients = payload.get("clients");
                    pendingRequests.get(requestId).complete(clients);
                    pendingRequests.remove(requestId);
                }
                case "error" -> {

                }
            }

        } catch (Exception e) {
            System.out.println("Something went wrong\n");
        }


        return WebSocket.Listener.super.onText(webSocket, data, last);
    }
}
