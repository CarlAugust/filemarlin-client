package filemarlin.filemarlinclient.webrtc;

import filemarlin.filemarlinclient.websocket.Signaller;
import filemarlin.filemarlinclient.websocket.records.SignalMessageResponse;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class FakeSignaller implements Signaller {


    public final Map<String, FakeSignaller> others = new ConcurrentHashMap<>();
    private Consumer<SignalMessageResponse> onSignalEvent;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String ownerId;

    public FakeSignaller(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public void sendSignal(String id, String signalType, JsonNode signalPayload) {
        var response = new SignalMessageResponse("webrtc-signal", ownerId, new SignalMessageResponse.ClientData(signalType, signalPayload));
        others.get(id).receiveSignal(objectMapper.valueToTree(response));
    }

    @Override
    public void receiveSignal(JsonNode payload) {
        onSignalEvent.accept(objectMapper.treeToValue(payload, SignalMessageResponse.class));
    }

    @Override
    public void setOnSignalEvent(Consumer<SignalMessageResponse> handler) {
        this.onSignalEvent = handler;
    }
}
