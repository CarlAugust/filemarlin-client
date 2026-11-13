package filemarlin.filemarlinclient.filetransfer.message;

import java.nio.ByteBuffer;

public class VerifyTransfer implements FileTransferMessageInterface {
    public long id;

    public VerifyTransfer(long id) {
        this.id = id;
    }

    @Override
    public MessageType getType() {
        return MessageType.VERIFY_TRANSFER;
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

    public static VerifyTransfer decode(ByteBuffer buffer) throws Exception {
        var id = buffer.getLong();

        return new VerifyTransfer(id);
    }
}