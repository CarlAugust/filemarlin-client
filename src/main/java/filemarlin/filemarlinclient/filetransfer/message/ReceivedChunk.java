package filemarlin.filemarlinclient.filetransfer.message;

import java.nio.ByteBuffer;

public class ReceivedChunk implements FileTransferMessageInterface {
    public long id;
    public long offset;
    public int size;

    public ReceivedChunk(long id, long offset, int size) {
        this.id = id;
        this.offset = offset;
        this.size = size;
    }

    @Override
    public MessageType getType() {
        return MessageType.RECEIVED;
    }

    @Override
    public ByteBuffer encode() {
        var buffer = ByteBuffer.allocate(
                1 + Long.BYTES + Long.BYTES + Integer.BYTES
        );

        buffer.put(getType().getValue());
        buffer.putLong(id);
        buffer.putLong(offset);
        buffer.putInt(size);
        buffer.flip();

        return buffer;
    }

    public static ReceivedChunk decode(ByteBuffer buffer) throws Exception {
        var id = buffer.getLong();
        var offset = buffer.getLong();
        var size = buffer.getInt();

        if (size < 0) {
            throw new IllegalArgumentException("Invalid received size: " + size);
        }

        return new ReceivedChunk(id, offset, size);
    }
}