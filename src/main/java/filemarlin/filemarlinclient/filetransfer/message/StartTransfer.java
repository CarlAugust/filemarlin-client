package filemarlin.filemarlinclient.filetransfer.message;

import java.nio.ByteBuffer;

public class StartTransfer implements  FileTransferMessageInterface {
    public long id;

    public StartTransfer(long id) {
        this.id = id;
    }

    @Override
    public MessageType getType() {
        return MessageType.START_TRANSFER;
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

    public static StartTransfer decode(ByteBuffer buffer) throws Exception {
        var id = buffer.getLong();

        return new StartTransfer(id);
    }
}
