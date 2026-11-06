package filemarlin.filemarlinclient.filetransfer.message;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class CancelMessage implements FileTransferMessageInterface {
    public long id;

    public CancelMessage(long id) {
        this.id = id;
    }

    @Override
    public MessageType getType() {
        return MessageType.CANCEL_TRANSFER;
    }

    @Override
    public ByteBuffer encode() {

        var buffer = ByteBuffer.allocate(
                1 + Long.BYTES
        );

        buffer.put(getType().getValue());
        buffer.putLong(id);
        buffer.flip();

        return buffer;
    }

    public static CancelMessage decode(ByteBuffer buffer) throws Exception {
        var id = buffer.getLong();

        return new CancelMessage(id);
    }
}