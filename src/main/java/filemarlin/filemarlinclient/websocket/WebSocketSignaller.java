package filemarlin.filemarlinclient.websocket;

import filemarlin.filemarlinclient.websocket.records.*;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class WebSocketSignaller {


    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RequestTracker requestTracker = new RequestTracker();
    private WebSocket socket;

    private Consumer<SignalMessageResponse> onSignalEvent;

    public WebSocketSignaller() {}

    public void setSocket(WebSocket socket) {
        this.socket = socket;
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

    public void receiveClients(JsonNode payload) throws JacksonException {
        var response = objectMapper.treeToValue(payload, GetClientsResponse.class);
        requestTracker.completeRequest(response.clientData().requestId(), response.clients());
    }

    public void receiveError(JsonNode payload) throws JacksonException {
        var response = objectMapper.treeToValue(payload, ErrorResponse.class);
        System.out.println(response.message());

        if (response.ClientData().has("requestId")) {
            var requestId = response.ClientData().get("requestId").asString();
            requestTracker.failRequest(requestId, new Exception(response.message()));
        }
    }

    public <T> void sendSignal(String id, String signalType, T signalPayload) throws JacksonException {
        var signalPayloadJson = objectMapper.valueToTree(signalPayload);
        var clientData = new SignalMessageRequest.ClientData(signalType, signalPayloadJson);
        var request = new SignalMessageRequest("webrtc-signal", id, clientData);
        sendMessage(request);
    }

    public void receiveSignal(JsonNode payload) throws JacksonException {

        var message = objectMapper.treeToValue(payload, SignalMessageResponse.class);

        if (onSignalEvent != null) {
            onSignalEvent.accept(message);
        }

    }

    public void setOnSignalEvent(Consumer<SignalMessageResponse> onSignalEvent) {
        this.onSignalEvent = onSignalEvent;
    }
}
