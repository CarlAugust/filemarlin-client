package filemarlin.filemarlinclient.filetransfer.message;

import java.nio.ByteBuffer;

public class AcceptTransfer implements FileTransferMessageInterface {
    public long id;

    public AcceptTransfer(long id) {
        this.id = id;
    }

    @Override
    public MessageType getType() {
        return MessageType.ACCEPT_TRANSFER;
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

    public static AcceptTransfer decode(ByteBuffer buffer) throws Exception {
        var id = buffer.getLong();

        return new AcceptTransfer(id);
    }
}