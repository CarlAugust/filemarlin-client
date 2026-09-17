package filemarlin.filemarlinclient.websocket;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;



public class WebSocketListener implements WebSocket.Listener {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WebSocketSignaller signaller;

    public WebSocketListener(WebSocketSignaller signaller) {
        this.signaller = signaller;
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {

        try {
            var payload = objectMapper.readTree(data.toString());
            var type = payload.get("type").asString();

            switch (type) {
                case "webrtc-signal" -> signaller.receiveSignal(payload);
                case "get-clients" -> signaller.receiveClients(payload);
                case "error" -> signaller.receiveError(payload);
            }

        }  catch (JacksonException e) {
            System.err.println("There was a problem parsing a response: " + e);
        } catch (Exception e) {
            System.err.println("Something went wrong\n");
        }

        return WebSocket.Listener.super.onText(webSocket, data, last);
    }
}
