package filemarlin.filemarlinclient.webrtc;

import dev.onvoid.webrtc.*;
import filemarlin.filemarlinclient.websocket.Signaller;
import filemarlin.filemarlinclient.websocket.records.SignalMessageResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class WebRTCClient {
    private final RTCIceServer iceServer = new RTCIceServer();
    private final WebRTCContext context = new WebRTCContext();
    private final Map<String, WebRTCPeerConnection> peerConnectionsMap = new ConcurrentHashMap<>();
    private Signaller signaller;

    public WebRTCClient(Signaller signaller) {
        this.signaller = signaller;

        iceServer.urls.add("stun:stun.l.google.com:19302");
        context.getConfig().iceServers.add(iceServer);

        signaller.setOnSignalEvent(this::handleSignal);
    }

    private void handleSignal(SignalMessageResponse message) {
        establishIfConnectionDoesntExist(message.senderId());
        var connection = getConnection(message.senderId());
        connection.receiveSignal(message.senderId(), message.clientData().signalType(), message.clientData().signalPayload());
    }

    public CompletableFuture<Void> establishConnection(String id, boolean shouldCreateOffer) {
        CompletableFuture<Void> connectionEstablished = new CompletableFuture<Void>()
                .orTimeout(10, TimeUnit.SECONDS);

        var connection = new WebRTCPeerConnection(id, context, signaller, connectionEstablished);
        peerConnectionsMap.put(id, connection);

        if (shouldCreateOffer) {
            connection.createOffer(id);
        }

        return connectionEstablished;
    }

    public void establishIfConnectionDoesntExist(String id) {
        if (peerConnectionsMap.get(id) == null) {
            establishConnection(id, false);
        }
    }

    public WebRTCPeerConnection getConnection(String id) {
        var connection = peerConnectionsMap.get(id);

        if (connection == null) {
            throw new IllegalStateException(
                    "No connection exists for " + id
            );
        }

        return connection;
    }

    public void close() {
        for (var connection : peerConnectionsMap.values()) {
            connection.close();
        }

        peerConnectionsMap.clear();
        context.getFactory().dispose();
    }
}
