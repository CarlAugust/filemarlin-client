package filemarlin.filemarlinclient.websocket.records;

public record GetClientsResponse(String type, String[] clients, ClientData clientData) {
    public record ClientData(String requestId) {}
}
