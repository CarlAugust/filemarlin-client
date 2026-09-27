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
                case FILE -> {
                    return FileMessage.decode(buffer);
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
