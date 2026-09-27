package filemarlin.filemarlinclient.filetransfer.message;

import java.nio.ByteBuffer;

public class AcceptMessage implements FileTransferMessageInterface {
    public long id;

    public AcceptMessage(long id) {
        this.id = id;
    }

    @Override
    public MessageType getType() {
        return MessageType.ACCEPT;
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

    public static AcceptMessage decode(ByteBuffer buffer) throws Exception {
        var id = buffer.getLong();

        return new AcceptMessage(id);
    }
}