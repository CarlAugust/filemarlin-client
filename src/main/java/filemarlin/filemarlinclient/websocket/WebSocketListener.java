package filemarlin.filemarlinclient.websocket;

import tools.jackson.databind.ObjectMapper;

import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

import static filemarlin.filemarlinclient.websocket.GlobalWebSocketSignallerAccessor.Signal;


public class WebSocketListener implements WebSocket.Listener {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {

        try {
            var payload = objectMapper.readTree(data.toString());
            var type = payload.get("type").asString();

            switch (type) {
                case "webrtc-signal" -> Signal().receiveSignal(payload);
                case "get-clients" -> Signal().receiveClients(payload);
                case "error" -> Signal().receiveError(payload);
            }

        } catch (Exception e) {
            System.out.println("Something went wrong\n");
        }

        return WebSocket.Listener.super.onText(webSocket, data, last);
    }
}
