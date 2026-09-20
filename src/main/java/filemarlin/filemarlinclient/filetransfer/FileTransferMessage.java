package filemarlin.filemarlinclient.filetransfer;

import java.nio.ByteBuffer;

public class FileTransferMessage implements FileTransferInterface{

    private final byte[] data;

    public FileTransferMessage(byte[] data) {
        this.data = data;
    }

    @Override
    public MessageType getType() {
        return MessageType.MESSAGE;
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

    public static FileTransferMessage decode(ByteBuffer buffer) {
        var length = buffer.getInt();

        if (length > 256 || length < 0) {
            throw new IllegalArgumentException("Invalid message size, max 256, actual: " + length);
        }

        var data = new byte[length];
        buffer.get(data);

        return new FileTransferMessage(data);
    }
}
