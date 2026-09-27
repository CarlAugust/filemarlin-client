package filemarlin.filemarlinclient.filetransfer.message;

import java.nio.ByteBuffer;

public class CompleteMessage implements FileTransferMessageInterface {
    public long id;

    public CompleteMessage(long id) {
        this.id = id;
    }

    @Override
    public MessageType getType() {
        return MessageType.COMPLETE;
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

    public static CompleteMessage decode(ByteBuffer buffer) throws Exception {
        var id = buffer.getLong();

        return new CompleteMessage(id);
    }
}