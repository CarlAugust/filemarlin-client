package filemarlin.filemarlinclient.websocket.records;

import tools.jackson.databind.JsonNode;

public record SignalMessageRequest(String type, String targetId, ClientData clientData) {
    public record ClientData(String signalType, JsonNode signalPayload) {};
}
