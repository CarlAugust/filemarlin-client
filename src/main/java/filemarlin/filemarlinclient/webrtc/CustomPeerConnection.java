package filemarlin.filemarlinclient.webrtc;


import dev.onvoid.webrtc.*;
import tools.jackson.databind.ObjectMapper;

public class CustomPeerConnection {

    private WebRTCClient manager;
    private RTCPeerConnection peerConnection;
    private RTCDataChannel dataChannel;

    private final RTCConfiguration config;
    private final PeerConnectionFactory factory;
    private final RTCOfferOptions options;


    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomPeerConnection(
            String id,
            RTCConfiguration config,
            PeerConnectionFactory factory,
            RTCOfferOptions options) {

        this.config = config;
        this.factory = factory;
        this.options = options;

        createPeerConnection(id);
    }

    public void createPeerConnection(String id) {

        var peerConnection = factory.createPeerConnection(config, new PeerConnectionObserver() {

            @Override
            public void onIceCandidate(RTCIceCandidate candidate) {
                var signalPayload = objectMapper.valueToTree(candidate);
                // Signaller.sendSignal(id, candidate);
            }

            @Override
            public void onDataChannel(RTCDataChannel dataChannel) {
                PeerConnectionObserver.super.onDataChannel(dataChannel);
            }
        });
        var dataChannel = peerConnection.createDataChannel("Data", new RTCDataChannelInit());
    }

    public void createOffer(String id) throws NullPointerException {
        peerConnection.createOffer(options, new CreateSessionDescriptionObserver() {
            @Override
            public void onSuccess(RTCSessionDescription description) {
                // Signaller.sendSignal(id, description);
            }

            @Override
            public void onFailure(String error) {
                System.err.println("Failed to set descriptor: " + error);
            }
        });
    }

    public void recieveMessage(String signalType, String signalPayload) {
        System.out.println(signalType);
        System.out.println(signalPayload);
    }

    public void close() {
        dataChannel.unregisterObserver();
        dataChannel.close();
        dataChannel.dispose();

        peerConnection.close();
    }


}
