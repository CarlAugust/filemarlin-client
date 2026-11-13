package filemarlin.filemarlinclient.webrtc;

import dev.onvoid.webrtc.RTCDataChannel;
import dev.onvoid.webrtc.RTCDataChannelBuffer;
import dev.onvoid.webrtc.RTCDataChannelObserver;
import dev.onvoid.webrtc.RTCDataChannelState;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class WebRTCDataChannelObserver implements RTCDataChannelObserver {


    private final RTCDataChannel dataChannel;
    private final CompletableFuture<Void> hasOpened;
    private final Consumer<ByteBuffer> messageListener;
    private final Object bufferLock;


    public WebRTCDataChannelObserver(RTCDataChannel dataChannel,
                                     CompletableFuture<Void> hasOpened,
                                     Consumer<ByteBuffer> messageListener,
                                     Object bufferLock
    ) {
        this.dataChannel = dataChannel;
        this.hasOpened = hasOpened;
        this.messageListener = messageListener;
        this.bufferLock = bufferLock;
    }

    @Override
    public void onBufferedAmountChange(long sentDataSize) {
        long currentAmount = dataChannel.getBufferedAmount();
        System.out.println("Buffered amount decreased by " + sentDataSize +
                " to " + currentAmount + " bytes");

        synchronized (bufferLock) {
            bufferLock.notifyAll();
        }
    }

    @Override
    public void onStateChange() {
        // Called when the data channel state changes
        RTCDataChannelState state = dataChannel.getState();
        System.out.println("Data channel state changed to: " + state);

        // Handle different states
        switch (state) {
            case CONNECTING:
                System.out.println("Data channel is being established");
                break;
            case OPEN:
                System.out.println("Data channel is open and ready to use");
                hasOpened.complete(null);

                break;
            case CLOSING:
                System.out.println("Data channel is being closed");
                break;
            case CLOSED:
                System.out.println("Data channel is closed");

        }
    }

    @Override
    public void onMessage(RTCDataChannelBuffer buffer) {

        var data = buffer.data;
        if (messageListener != null) {
            messageListener.accept(data);
        }

    }


}
