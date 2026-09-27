package filemarlin.filemarlinclient.filetransfer.message;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class TestMessage implements FileTransferMessageInterface {

    private int size;
    private final byte[] data;

    public TestMessage(int size, byte[] data) {
        this.size = size;
        this.data = data;
    }

    @Override
    public MessageType getType() {
        return MessageType.TEST;
    }

    @Override
    public ByteBuffer encode() {
        var buffer = ByteBuffer.allocate(1 + Integer.BYTES + data.length);
        buffer.put(getType().getValue());
        buffer.putInt(data.length);
        buffer.put(data);

        buffer.flip();

        return buffer;
    }

    public static TestMessage decode(ByteBuffer buffer) throws BufferUnderflowException {
        var length = buffer.getInt();

        if (length > 256 || length < 0) {
            throw new IllegalArgumentException("Invalid message size, max 256, actual: " + length);
        }

        var data = new byte[length];
        buffer.get(data);

        return new TestMessage(length, data);
    }

    public byte[] getData() {
        return data;
    }
}
