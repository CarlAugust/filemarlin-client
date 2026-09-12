package filemarlin.filemarlinclient.webrtc;

import dev.onvoid.webrtc.*;

public class WebRTCManager {
    private final RTCConfiguration config = new RTCConfiguration();
    private final RTCIceServer iceServer = new RTCIceServer();
    private final PeerConnectionFactory factory = new PeerConnectionFactory();

    public WebRTCManager() {
        iceServer.urls.add("stun:stun.l.google.com:19302");

        // Lets just test first of all

        RTCPeerConnection peerConnection = factory.createPeerConnection(config, new PeerConnectionObserver());

    }
}
