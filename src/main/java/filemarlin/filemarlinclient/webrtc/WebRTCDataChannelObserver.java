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
    public void onBufferedAmountChange(long sentDataSize) {
        long currentAmount = dataChannel.getBufferedAmount();
        System.out.println("Buffered amount decreased by " + sentDataSize +
                " to " + currentAmount + " bytes");
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

    @Override
    public void onMessage(RTCDataChannelBuffer buffer) {


        // TODO: This might have to be abstracted later as this DataChannel observer will handle
        // File transfer for now

        var data = buffer.data;
        if (messageListener != null) {
            messageListener.accept(data);
        }

    }

    public void setMessageListener(Consumer<ByteBuffer> messageListener) {
        this.messageListener = messageListener;
    }


}
