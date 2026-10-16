package filemarlin.filemarlinclient.webrtc;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

public class FileTransferService {


    WebRTCDataChannelObserver transport;

    public void FileTransferService(WebRTCDataChannelObserver transport) {
        this.transport = transport;
        this.transport.setMessageListener(this::onMessage);
    }

    private void onMessage(ByteBuffer buffer) {
        // Do FileTransfer I guess
         
    }


}
