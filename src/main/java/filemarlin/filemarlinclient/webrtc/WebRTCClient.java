package filemarlin.filemarlinclient.webrtc;

import dev.onvoid.webrtc.*;
import filemarlin.filemarlinclient.websocket.Signaller;
import filemarlin.filemarlinclient.websocket.records.SignalMessageResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebRTCClient {
    private final RTCConfiguration config = new RTCConfiguration();
    private final RTCIceServer iceServer = new RTCIceServer();
    private final PeerConnectionFactory factory = new PeerConnectionFactory();
    private final RTCOfferOptions offerOptions = new RTCOfferOptions();
    private final RTCAnswerOptions answerOptions = new RTCAnswerOptions();
    private final Map<String, CustomPeerConnection> peerConnectionsMap = new ConcurrentHashMap<>();

    private Signaller signaller;

    public WebRTCClient(Signaller signaller) {
        this.signaller = signaller;

        iceServer.urls.add("stun:stun.l.google.com:19302");
        config.iceServers.add(iceServer);

        signaller.setOnSignalEvent(this::handleSignal);
    }

    private void handleSignal(SignalMessageResponse message) {
        var connection = getConnection(message.senderId());
        connection.receiveSignal(message.senderId(), message.clientData().signalType(), message.clientData().signalPayload());
    }

    public void establishConnection(String id) {
        peerConnectionsMap.put(id, new CustomPeerConnection(id, config, factory, offerOptions, answerOptions, signaller));}

    public CustomPeerConnection getConnection(String id) {
        if (!peerConnectionsMap.containsKey(id)) {
            establishConnection(id);
        }
        return peerConnectionsMap.get(id);
    }
}
