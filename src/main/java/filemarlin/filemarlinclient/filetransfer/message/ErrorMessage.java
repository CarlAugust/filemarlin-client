package filemarlin.filemarlinclient.filetransfer.message;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ErrorMessage implements FileTransferMessageInterface {
    public long id;
    public ErrorCodes code;
    public String message;


    public ErrorMessage(long id, ErrorCodes errorCode, String message) {
        this.id = id;
        this.code = errorCode;
        this.message = message;
    }

    @Override
    public MessageType getType() {
        return MessageType.ERROR;
    }

    @Override
    public ByteBuffer encode() {
        var messageData = message.getBytes(StandardCharsets.UTF_8);

        var buffer = ByteBuffer.allocate(
                1 + Long.BYTES + 1 + Integer.BYTES + messageData.length
        );

        buffer.put(getType().getValue());
        buffer.putLong(id);
        buffer.put(code.getValue());
        buffer.putInt(messageData.length);
        buffer.put(messageData);
        buffer.flip();

        return buffer;
    }

    public static ErrorMessage decode(ByteBuffer buffer) throws Exception {
        var id = buffer.getLong();
        var codeValue = buffer.get();
        var messageSize = buffer.getInt();

        if (messageSize < 0 || messageSize > 256) {
            throw new IllegalArgumentException(
                    "Invalid error message size: " + messageSize
            );
        }

        var data = new byte[messageSize];
        buffer.get(data);

        var message = new String(data, StandardCharsets.UTF_8);

        var code = ErrorCodes.fromValue(codeValue);

        return new ErrorMessage(id, code, message);
    }
}
