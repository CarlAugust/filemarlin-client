package filemarlin.filemarlinclient.webrtc;

import dev.onvoid.webrtc.RTCDataChannel;
import dev.onvoid.webrtc.RTCDataChannelBuffer;
import dev.onvoid.webrtc.RTCDataChannelObserver;
import dev.onvoid.webrtc.RTCDataChannelState;
import filemarlin.filemarlinclient.filetransfer.FileTransferMessageDecoder;
import filemarlin.filemarlinclient.filetransfer.message.TestMessage;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class WebRTCDataChannelObserver implements RTCDataChannelObserver {


    private final RTCDataChannel dataChannel;
    private Consumer<ByteBuffer> messageListener;
    private final CompletableFuture<Void> hasOpened;


    public WebRTCDataChannelObserver(RTCDataChannel dataChannel,
                                     CompletableFuture<Void> hasOpened
    ) {
        this.dataChannel = dataChannel;
        this.hasOpened = hasOpened;
    }

    @Override
    public void onBufferedAmountChange(long previousAmount) {
        System.out.println("Welp something happened in the buffer " + previousAmount);
    }

    @Override
    public void onStateChange() {
        var state = dataChannel.getState();
        switch (state) {
            case OPEN -> {
                hasOpened.complete(null);
            }
        }
    }


    @Override
    public void onMessage(RTCDataChannelBuffer buffer) {

        var data = buffer.data;
        if (messageListener != null) {
            messageListener.accept(data);
        }

    }

    public void send(ByteBuffer byteBuffer) throws Exception {
        if (dataChannel.getState() != RTCDataChannelState.OPEN) {
            throw new IllegalStateException("Data channel is not open");
        }

        dataChannel.send(new RTCDataChannelBuffer(byteBuffer, true));
    }

    public void setMessageListener(Consumer<ByteBuffer> messageListener) {
        this.messageListener = messageListener;
    }


}
