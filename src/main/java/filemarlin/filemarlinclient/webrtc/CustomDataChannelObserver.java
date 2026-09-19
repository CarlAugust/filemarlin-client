package filemarlin.filemarlinclient.webrtc;

import dev.onvoid.webrtc.RTCDataChannel;
import dev.onvoid.webrtc.RTCDataChannelBuffer;
import dev.onvoid.webrtc.RTCDataChannelObserver;
import dev.onvoid.webrtc.RTCDataChannelState;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class CustomDataChannelObserver implements RTCDataChannelObserver {


    private final RTCDataChannel dataChannel;
    private final CompletableFuture<Void> connectionEstablished;

    public CustomDataChannelObserver(RTCDataChannel dataChannel, CompletableFuture<Void> connectionEstablished) {
        this.dataChannel = dataChannel;
        this.connectionEstablished = connectionEstablished;
    }

    @Override
    public void onBufferedAmountChange(long previousAmount) {

    }

    @Override
    public void onStateChange() {
        var state = dataChannel.getState();
        switch (state) {
            case OPEN -> {
                connectionEstablished.complete(null);
            }
        }
    }

    @Override
    public void onMessage(RTCDataChannelBuffer buffer) {
        System.out.println(buffer.toString());

        byte[] message = "ping".getBytes(StandardCharsets.UTF_8);

        try {
            dataChannel.send(
                    new RTCDataChannelBuffer(
                            ByteBuffer.wrap(message),
                            false
                    )
            );
        } catch (Exception e) {
            System.err.println("Error when sending message: " + e);
        }

    }
}
