package filemarlin.filemarlinclient.webrtc;

import dev.onvoid.webrtc.RTCDataChannelBuffer;
import filemarlin.filemarlinclient.filetransfer.FileTransferMessageDecoder;
import filemarlin.filemarlinclient.filetransfer.FileTransferMessageInterface;
import filemarlin.filemarlinclient.filetransfer.TestMessage;
import filemarlin.filemarlinclient.filetransfer.MessageType;

import java.nio.charset.StandardCharsets;

public class DataChannelMessageHandler {

    private String lastMessage = "";

    public void onMessage(RTCDataChannelBuffer buffer) {

        var data = buffer.data;

        try {
            var message = FileTransferMessageDecoder.decode(data);
            var type = message.getType();
            switch (type) {
                case TEST -> {
                    var testMessage = (TestMessage) message;
                    lastMessage = new String(testMessage.getData(), StandardCharsets.UTF_8);
                }
                case FILE -> {

                }
                case ACCEPT -> {

                }
                case PAYLOAD -> {

                }
                case RECEIVED -> {

                }
                case COMPLETE -> {

                }
                case CANCEL -> {

                }
                case ERROR -> {

                }
            }
        } catch (Exception e) {
            System.err.println("Error when sending message: " + e);
        }
    }

    public String getLastMessage() {
        return lastMessage;
    }
}
