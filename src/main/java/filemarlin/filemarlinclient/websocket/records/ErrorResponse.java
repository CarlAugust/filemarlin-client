package filemarlin.filemarlinclient.websocket.records;

import tools.jackson.databind.JsonNode;

public record ErrorResponse(String type, String message, JsonNode ClientData) {
}
