package filemarlin.filemarlinclient.websocket;

import filemarlin.filemarlinclient.websocket.records.SignalMessageResponse;
import tools.jackson.databind.JsonNode;

import java.util.function.Consumer;

public interface Signaller {

    void sendSignal(
            String id,
            String signalType,
            JsonNode signalPayload
    );

    void receiveSignal (
            JsonNode payload
    );

    void setOnSignalEvent(
            Consumer<SignalMessageResponse> handler
    );
}
