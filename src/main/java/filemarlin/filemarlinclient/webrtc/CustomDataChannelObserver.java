package filemarlin.filemarlinclient.webrtc;

import dev.onvoid.webrtc.RTCDataChannel;
import dev.onvoid.webrtc.RTCDataChannelBuffer;
import dev.onvoid.webrtc.RTCDataChannelObserver;
import dev.onvoid.webrtc.RTCDataChannelState;
import filemarlin.filemarlinclient.filetransfer.FileTransferMessage;
import filemarlin.filemarlinclient.filetransfer.MessageType;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class CustomDataChannelObserver implements RTCDataChannelObserver {


    private final RTCDataChannel dataChannel;
    private final CompletableFuture<Void> connectionEstablished;
    private final DataChannelMessageHandler messageHandler;

    public CustomDataChannelObserver(RTCDataChannel dataChannel,
                                     CompletableFuture<Void> connectionEstablished,
                                     DataChannelMessageHandler messageHandler) {
        this.dataChannel = dataChannel;
        this.connectionEstablished = connectionEstablished;
        this.messageHandler = messageHandler;
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
        messageHandler.onMessage(buffer);
    }
}
