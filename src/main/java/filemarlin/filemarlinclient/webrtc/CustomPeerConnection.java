package filemarlin.filemarlinclient.webrtc;


import dev.onvoid.webrtc.*;
import filemarlin.filemarlinclient.webrtc.dto.IceCandidateDto;
import filemarlin.filemarlinclient.webrtc.dto.SessionDescriptionDto;
import filemarlin.filemarlinclient.websocket.Signaller;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class CustomPeerConnection {

    private RTCPeerConnection peerConnection = null;
    private RTCDataChannel dataChannel = null;

    private final RTCConfiguration config;
    private final PeerConnectionFactory factory;
    private final RTCOfferOptions offerOptions;
    private final RTCAnswerOptions answerOptions;
    private final String peerId;

    private final List<RTCIceCandidate> iceCandidateList = new ArrayList<>();
    private boolean remoteDescriptionSet = false;

    private final Signaller signaller;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public CustomPeerConnection(
            String id,
            RTCConfiguration config,
            PeerConnectionFactory factory,
            RTCOfferOptions offerOptions,
            RTCAnswerOptions answerOptions,
            Signaller signaller) {

        this.peerId = id;
        this.config = config;
        this.factory = factory;
        this.offerOptions = offerOptions;
        this.answerOptions = answerOptions;
        this.signaller = signaller;

        createPeerConnection(id);
    }

    private void log(String message) {
        System.out.println("[WebRTC][" + peerId + "] " + message);
    }

    private void log_error(String message, Error e) {
        System.err.println("[WebRTC][" + peerId + "] " + message + " " + e);
    }

    public void createPeerConnection(String id) {

        peerConnection = factory.createPeerConnection(config, new PeerConnectionObserver() {

            @Override
            public void onIceCandidate(RTCIceCandidate candidate) {
                signaller.sendSignal(id, "ice", objectMapper.valueToTree(candidate));
            }

            @Override
            public void onDataChannel(RTCDataChannel channel) {
                log("Set remote dataChannel");
                dataChannel = channel;
                dataChannel.registerObserver(new CustomDataChannelObserver(dataChannel));
            }
        });

    }

    public void createOffer(String id) throws NullPointerException {
        log("Creating offer.");
        dataChannel = peerConnection.createDataChannel("Message", new RTCDataChannelInit());
        dataChannel.registerObserver(new CustomDataChannelObserver(dataChannel));

        peerConnection.createOffer(offerOptions, new CreateSessionDescriptionObserver() {
            @Override
            public void onSuccess(RTCSessionDescription description) {
                peerConnection.setLocalDescription(description, new SetSessionDescriptionObserver() {
                    @Override
                    public void onSuccess() {
                        log("Offer set. ");
                        signaller.sendSignal(id, "offer", objectMapper.valueToTree(description));
                    }

                    @Override
                    public void onFailure(String error) {
                        log_error("Failed to set offer: ", new Error(error));
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                log_error("Failed to create offer", new Error(error));
            }
        });
    }

    public void createAnswer(String id) {
        log("creating answer");
        peerConnection.createAnswer(answerOptions, new CreateSessionDescriptionObserver() {
            @Override
            public void onSuccess(RTCSessionDescription description) {
                peerConnection.setLocalDescription(description, new SetSessionDescriptionObserver() {
                    @Override
                    public void onSuccess() {
                        log("Set created answer");
                        signaller.sendSignal(id, "answer", objectMapper.valueToTree(description));
                    }

                    @Override
                    public void onFailure(String error) {
                        log_error("Failed to set created answer", new Error(error));
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                log_error("Failed to create answer", new Error(error));
            }
        });
    }

    public void receiveSignal(String id, String signalType, JsonNode signalPayload) {
        switch(signalType) {
            case "offer" -> receiveOffer(id, signalPayload);
            case "answer" -> receiveAnswer(signalPayload);
            case "ice" -> receiveIce(signalPayload);
        }
    }

    private void receiveOffer(String id, JsonNode signalPayload) {
        log("Receiving offer");
        RTCSessionDescription description;
        try {
            description = SessionDescriptionDto.toRTCSesssionDescription(objectMapper, signalPayload);
        } catch (JacksonException e) {
            System.err.println("Error parsing answer: " + e);
            return;
        }

        peerConnection.setRemoteDescription(description, new SetSessionDescriptionObserver() {
            @Override
            public void onSuccess() {
                log("Set received offer");
                flushIce();
                createAnswer(id);
            }

            @Override
            public void onFailure(String error) {
                log_error("Failed to set received offer", new Error(error));
            }
        });
    }

    private void receiveAnswer(JsonNode signalPayload) {
        log("Receiving answer.");
        RTCSessionDescription description;
        try {
            description = SessionDescriptionDto.toRTCSesssionDescription(objectMapper, signalPayload);
        } catch (JacksonException e) {
            System.err.println("Error parsing answer: " + e);
            return;
        }

        peerConnection.setRemoteDescription(description, new SetSessionDescriptionObserver() {
            @Override
            public void onSuccess() {
                log("Set received answer");
                flushIce();
            }

            @Override
            public void onFailure(String error) {
                log_error("Failed to set received answer", new Error(error));
            }
        });
    }

    private void receiveIce(JsonNode signalPayload) {
        IceCandidateDto candidateDto;
        try {
            candidateDto = objectMapper.treeToValue( signalPayload, IceCandidateDto.class );
        } catch (Exception e) {
            System.err.println("Failed to parse ICE candidate: " + e.getMessage());
            return;
        }

        var candidate = new RTCIceCandidate(candidateDto.sdpMid, candidateDto.sdpMLineIndex, candidateDto.sdp);

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

    // Only used for testing
    public RTCDataChannel getDataChannel() {
        return dataChannel;
    }

    public RTCPeerConnection getPeerConnection() {
        return peerConnection;
    }
}
