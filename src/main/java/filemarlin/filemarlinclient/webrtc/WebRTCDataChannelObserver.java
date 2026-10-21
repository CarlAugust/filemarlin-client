package filemarlin.filemarlinclient.webrtc;

import dev.onvoid.webrtc.RTCDataChannel;
import dev.onvoid.webrtc.RTCDataChannelBuffer;
import dev.onvoid.webrtc.RTCDataChannelObserver;
import dev.onvoid.webrtc.RTCDataChannelState;
import filemarlin.filemarlinclient.filetransfer.FileTransfer;
import filemarlin.filemarlinclient.filetransfer.FileTransferMessageDecoder;
import filemarlin.filemarlinclient.filetransfer.FileTransferPath;
import filemarlin.filemarlinclient.filetransfer.message.*;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class WebRTCDataChannelObserver implements RTCDataChannelObserver {


    private final RTCDataChannel dataChannel;
    private final CompletableFuture<Void> hasOpened;

    private Consumer<ByteBuffer> messageListener;


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


        // TODO: This might have to be abstracted later as this DataChannel observer will handle
        // File transfer for now

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
