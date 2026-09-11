package filemarlin.filemarlinclient.websocket;

import filemarlin.filemarlinclient.websocket.records.ErrorResponse;
import filemarlin.filemarlinclient.websocket.records.GetClientsResponse;
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
    private final ConcurrentHashMap<String, CompletableFuture<Object>> pendingRequests = new ConcurrentHashMap<>();

    public Pair<String, CompletableFuture<Object>> registerRequest() {
        String id = UUID.randomUUID().toString();
        CompletableFuture<Object> future = new CompletableFuture<>();

        future.orTimeout(5, TimeUnit.SECONDS)
                .whenComplete((res, ex) -> pendingRequests.remove(id));

        pendingRequests.put(id, future);
        return new Pair<>(id, future);
    }

    public void completeRequest(String requestId, Object result) {
        pendingRequests.get(requestId).complete(result);
        pendingRequests.remove(requestId);
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
                    var response = objectMapper.treeToValue(payload, GetClientsResponse.class);
                    completeRequest(response.clientData().requestId(), response.clients());
                }
                case "error" -> {
                    var response = objectMapper.treeToValue(payload, ErrorResponse.class);
                    System.out.println(response.message());

                    if (response.ClientData().has("requestId")) {
                        var requestId = response.ClientData().get("requestId").asString();
                        pendingRequests.get(requestId).completeExceptionally(new Exception(response.message()));
                        pendingRequests.remove(requestId);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Something went wrong\n");
        }


        return WebSocket.Listener.super.onText(webSocket, data, last);
    }
}
