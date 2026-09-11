package filemarlin.filemarlinclient.websocket.records;

public record GetClientsRequest(String type, ClientData clientData) {
    public record ClientData(String requestId) {}
}
