package filemarlin.filemarlinclient.websocket.records;

import tools.jackson.databind.JsonNode;

public record SignalMessageResponse(String type, String senderId, ClientData clientData) {
    public record ClientData(String signalType, JsonNode signalPayload) {};
}
