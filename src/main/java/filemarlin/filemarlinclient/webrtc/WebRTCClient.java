package filemarlin.filemarlinclient.webrtc;

import dev.onvoid.webrtc.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebRTCClient {
    private final RTCConfiguration config = new RTCConfiguration();
    private final RTCIceServer iceServer = new RTCIceServer();
    private final PeerConnectionFactory factory = new PeerConnectionFactory();
    private final RTCOfferOptions options = new RTCOfferOptions();
    private final Map<String, CustomPeerConnection> peerConnectionsMap = new ConcurrentHashMap<>();

    public WebRTCClient() {
        iceServer.urls.add("stun:stun.l.google.com:19302");
        config.iceServers.add(iceServer);
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
