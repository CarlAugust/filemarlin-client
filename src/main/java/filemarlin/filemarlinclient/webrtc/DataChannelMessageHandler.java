package filemarlin.filemarlinclient.webrtc;

import dev.onvoid.webrtc.RTCDataChannelBuffer;
import filemarlin.filemarlinclient.filetransfer.FileTestMessage;
import filemarlin.filemarlinclient.filetransfer.MessageType;

import java.nio.charset.StandardCharsets;

public class DataChannelMessageHandler {

    private String lastMessage = "";

    public void onMessage(RTCDataChannelBuffer buffer) {

        var data = buffer.data;

        try {
            var type = MessageType.fromByte(data.get());

            switch (type) {
                case MESSAGE -> {
                    var message = FileTestMessage.decode(data);
                    lastMessage = new String(message.getData(), StandardCharsets.UTF_8);
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
