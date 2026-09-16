package filemarlin.filemarlinclient.webrtc;

import dev.onvoid.webrtc.*;
import filemarlin.filemarlinclient.websocket.records.SignalMessageRequest;
import filemarlin.filemarlinclient.websocket.records.SignalMessageResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static filemarlin.filemarlinclient.websocket.GlobalWebSocketSignallerAccessor.Signal;

public class WebRTCClient {
    private final RTCConfiguration config = new RTCConfiguration();
    private final RTCIceServer iceServer = new RTCIceServer();
    private final PeerConnectionFactory factory = new PeerConnectionFactory();
    private final RTCOfferOptions options = new RTCOfferOptions();
    private final Map<String, CustomPeerConnection> peerConnectionsMap = new ConcurrentHashMap<>();

    public WebRTCClient() {
        iceServer.urls.add("stun:stun.l.google.com:19302");
        config.iceServers.add(iceServer);

        Signal().setOnSignalEvent(this::handleSignal);
    }

    private void handleSignal(SignalMessageResponse message) {
        System.out.println("A signal was sent!!!");

        var connection = getConnection(message.senderId());

    }

    public void establishConnection(String id) {
        peerConnectionsMap.put(id, new CustomPeerConnection(id, config, factory, options));
    }

    public CustomPeerConnection getConnection(String id) {
        if (!peerConnectionsMap.containsKey(id)) {
            establishConnection(id);
        }
        return peerConnectionsMap.get(id);
    }
}
