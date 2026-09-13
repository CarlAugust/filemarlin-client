package filemarlin.filemarlinclient.webrtc;

import dev.onvoid.webrtc.*;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebRTCManager {
    private final RTCConfiguration config = new RTCConfiguration();
    private final RTCIceServer iceServer = new RTCIceServer();
    private final PeerConnectionFactory factory = new PeerConnectionFactory();
    private final RTCOfferOptions options = new RTCOfferOptions();
    private final Map<String, CustomPeerConnection> peerConnectionsMap = new ConcurrentHashMap<>();

    public WebRTCManager() {
        iceServer.urls.add("stun:stun.l.google.com:19302");
        config.iceServers.add(iceServer);
    }

    public void establishConnection(String id) {
        peerConnectionsMap.put(id, new CustomPeerConnection(id, config, factory, options));
    }
}
