package filemarlin.filemarlinclient.websocket;

import filemarlin.filemarlinclient.websocket.records.ErrorResponse;
import filemarlin.filemarlinclient.websocket.records.GetClientsRequest;
import filemarlin.filemarlinclient.websocket.records.GetClientsResponse;
import filemarlin.filemarlinclient.websocket.records.SignalMessage;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;

public class WebSocketSignaller {


    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RequestTracker requestTracker = new RequestTracker();

    private final WebSocket socket;

    public WebSocketSignaller(WebSocket webSocket) {
        socket = webSocket;
    }

    private void sendMessage(Object request) {
        socket.sendText(objectMapper.writeValueAsString(request), true);
    }

    public CompletableFuture<String[]> getClients() {
        var key_future = requestTracker.registerRequest();
        var request = new GetClientsRequest("get-clients", new GetClientsRequest.ClientData(key_future.getKey()));
        sendMessage(request);
        return key_future.getValue().thenApply(obj -> (String[]) obj);
    }

    public void receiveClients(JsonNode payload) {
        var response = objectMapper.treeToValue(payload, GetClientsResponse.class);
        requestTracker.completeRequest(response.clientData().requestId(), response.clients());
    }

    public void receiveError(JsonNode payload) {
        var response = objectMapper.treeToValue(payload, ErrorResponse.class);
        System.out.println(response.message());

        if (response.ClientData().has("requestId")) {
            var requestId = response.ClientData().get("requestId").asString();
            requestTracker.failRequest(requestId, new Exception(response.message()));
        }
    }

    public <T> void sendSignal(String id, String signalType, T signalPayload) {
        var signalPayloadJson = objectMapper.valueToTree(signalPayload);
        var clientData = new SignalMessage.ClientData(signalType, signalPayloadJson);
        var request = new SignalMessage("webrtc-signal", id, clientData);
        sendMessage(request);
    }

    public void receiveSignal(JsonNode payload) {
        var message = objectMapper.treeToValue(payload, SignalMessage.class);
        var type = message.clientData().signalType();
        
    }

}
