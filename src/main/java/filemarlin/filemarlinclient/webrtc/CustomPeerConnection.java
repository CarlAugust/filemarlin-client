package filemarlin.filemarlinclient.webrtc;


import dev.onvoid.webrtc.*;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static filemarlin.filemarlinclient.websocket.GlobalWebSocketSignallerAccessor.Signal;

public class CustomPeerConnection {

    private RTCPeerConnection peerConnection;
    private RTCDataChannel dataChannel;

    private final RTCConfiguration config;
    private final PeerConnectionFactory factory;
    private final RTCOfferOptions offerOptions;
    private final RTCAnswerOptions answerOptions;

    private final List<RTCIceCandidate> iceCandidateList = new ArrayList<>();
    private boolean remoteDescriptionSet = false;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomPeerConnection(
            String id,
            RTCConfiguration config,
            PeerConnectionFactory factory,
            RTCOfferOptions offerOptions,
            RTCAnswerOptions answerOptions) {

        this.config = config;
        this.factory = factory;
        this.offerOptions = offerOptions;
        this.answerOptions = answerOptions;

        createPeerConnection(id);
    }

    public void createPeerConnection(String id) {

        peerConnection = factory.createPeerConnection(config, new PeerConnectionObserver() {

            @Override
            public void onIceCandidate(RTCIceCandidate candidate) {
                Signal().sendSignal(id, "ice", objectMapper.valueToTree(candidate));
            }

            @Override
            public void onDataChannel(RTCDataChannel channel) {
                dataChannel = channel;
                dataChannel.registerObserver(new RTCDataChannelObserver() {
                    @Override
                    public void onBufferedAmountChange(long previousAmount) {

                    }

                    @Override
                    public void onStateChange() {

                    }

                    @Override
                    public void onMessage(RTCDataChannelBuffer buffer) {

                    }
                });
            }
        });

    }

    public void createOffer(String id) throws NullPointerException {
        dataChannel = peerConnection.createDataChannel("Message", new RTCDataChannelInit());
        dataChannel.registerObserver(new RTCDataChannelObserver() {
            @Override
            public void onBufferedAmountChange(long previousAmount) {

            }

            @Override
            public void onStateChange() {

            }

            @Override
            public void onMessage(RTCDataChannelBuffer buffer) {

            }
        });
        
        peerConnection.createOffer(offerOptions, new CreateSessionDescriptionObserver() {
            @Override
            public void onSuccess(RTCSessionDescription description) {
                peerConnection.setLocalDescription(description, new SetSessionDescriptionObserver() {
                    @Override
                    public void onSuccess() {
                        Signal().sendSignal(id, "offer", objectMapper.valueToTree(description));
                    }

                    @Override
                    public void onFailure(String error) {}
                });
            }

            @Override
            public void onFailure(String error) {
                System.err.println("Failed to set descriptor: " + error);
            }
        });
    }

    public void createAnswer(String id) {
        peerConnection.createAnswer(answerOptions, new CreateSessionDescriptionObserver() {
            @Override
            public void onSuccess(RTCSessionDescription description) {
                peerConnection.setLocalDescription(description, new SetSessionDescriptionObserver() {
                    @Override
                    public void onSuccess() {
                        Signal().sendSignal(id, "answer", objectMapper.valueToTree(description));
                    }

                    @Override
                    public void onFailure(String error) {
                        System.err.println("Offer fail: " + error);
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                System.err.println("Create answer fail: " + error);
            }
        });
    }

    public void recieveSignal(String id, String signalType, JsonNode signalPayload) {
        switch(signalType) {
            case "offer" -> recieveOffer(id, signalPayload);
            case "answer" -> recieveAnswer(signalPayload);
            case "ice" -> recieveIce(signalPayload);
        }
    }

    private void recieveOffer(String id, JsonNode signalPayload) {
        var remoteDescription = objectMapper.treeToValue(signalPayload, RTCSessionDescription.class);
        peerConnection.setRemoteDescription(remoteDescription, new SetSessionDescriptionObserver() {
            @Override
            public void onSuccess() {
                System.out.println("Offer success");
                flushIce();
                createAnswer(id);
            }

            @Override
            public void onFailure(String error) {
                System.err.println("Offer fail: " + error);
            }
        });
    }

    private void recieveAnswer(JsonNode signalPayload) {
        var remoteDescription = objectMapper.treeToValue(signalPayload, RTCSessionDescription.class);
        peerConnection.setRemoteDescription(remoteDescription, new SetSessionDescriptionObserver() {
            @Override
            public void onSuccess() {
                System.out.println("Answer success");
                flushIce();
            }

            @Override
            public void onFailure(String error) {
                System.err.println("Answer fail: " + error);
            }
        });
    }

    private void recieveIce(JsonNode signalPayload) {
        var candidate = objectMapper.treeToValue(signalPayload, RTCIceCandidate.class);

        if (remoteDescriptionSet) {
            peerConnection.addIceCandidate(candidate);
            return;
        }

        iceCandidateList.add(candidate);
    }

    private void flushIce() {
        remoteDescriptionSet = true;
        for (var candidate : iceCandidateList) {
            peerConnection.addIceCandidate(candidate);
        }

        iceCandidateList.clear();
    }

    public void close() {
        dataChannel.unregisterObserver();
        dataChannel.close();
        dataChannel.dispose();

        peerConnection.close();
    }


}
