package filemarlin.filemarlinclient.webrtc;

import dev.onvoid.webrtc.RTCDataChannel;
import dev.onvoid.webrtc.RTCDataChannelBuffer;
import dev.onvoid.webrtc.RTCDataChannelObserver;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class CustomDataChannelObserver implements RTCDataChannelObserver {


    private RTCDataChannel dataChannel;

    public CustomDataChannelObserver(RTCDataChannel dataChannel) {
        this.dataChannel = dataChannel;
    }

    @Override
    public void onBufferedAmountChange(long previousAmount) {

    }

    @Override
    public void onStateChange() {
        System.out.println(
                "Data channel state: " + dataChannel.getState()
        );
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
