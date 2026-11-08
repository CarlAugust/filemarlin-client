package filemarlin.filemarlinclient.filetransfer.message;

import java.nio.ByteBuffer;

public class ChunkMessage implements FileTransferMessageInterface {
    public long id;
    public long offset;
    public int size;
    public byte[] data;

    public ChunkMessage(long id, long offset, int size, byte[] data) {
        this.id = id;
        this.offset = offset;
        this.size = size;
        this.data = data;
    }

    @Override
    public MessageType getType() {
        return MessageType.CHUNK;
    }

    @Override
    public ByteBuffer encode() {
        var buffer = ByteBuffer.allocate(
                1 + Long.BYTES + Long.BYTES + Integer.BYTES + size
        );

        buffer.put(getType().getValue());
        buffer.putLong(id);
        buffer.putLong(offset);
        buffer.putInt(size);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    public static ChunkMessage decode(ByteBuffer buffer) throws Exception {
        var id = buffer.getLong();
        var offset = buffer.getLong();
        var size = buffer.getInt();

        if (size < 0) {
            throw new IllegalArgumentException("Invalid payload size: " + size);
        }

        var data = new byte[size];
        buffer.get(data);

        return new ChunkMessage(id, offset, size, data);
    }
}