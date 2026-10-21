package filemarlin.filemarlinclient.filetransfer;

import filemarlin.filemarlinclient.filetransfer.message.*;

import java.nio.ByteBuffer;

public class FileTransferMessageDecoder {

    private static FileTransferMessageInterface decodeError() {
        String errorMessage = "Could not decode received data";
        return new ErrorMessage(0, ErrorCodes.DECODE_ERROR_LOCAL, errorMessage);
    }

    public static FileTransferMessageInterface decode(ByteBuffer buffer) {
        var type = buffer.get();
        var messageType = MessageType.fromByte(type);


        try {
            switch (messageType) {
                case TEST -> {
                    return TestMessage.decode(buffer);
                }
                case UPLOAD -> {
                    return UploadFileMessage.decode(buffer);
                }
                case DOWNLOAD -> {
                    return DownloadFileMessage.decode(buffer);
                }
                case ACCEPT -> {
                    return AcceptMessage.decode(buffer);
                }
                case PAYLOAD -> {
                    return PayloadMessage.decode(buffer);
                }
                case RECEIVED -> {
                    return ReceivedMessage.decode(buffer);
                }
                case COMPLETE -> {
                    return CompleteMessage.decode(buffer);
                }
                case CANCEL -> {
                    return CancelMessage.decode(buffer);
                }
                case ERROR -> {
                    return ErrorMessage.decode(buffer);
                }
                default -> {
                    return decodeError();
                }
            }
        } catch (Exception e) {
            return decodeError();
        }

    }
}
