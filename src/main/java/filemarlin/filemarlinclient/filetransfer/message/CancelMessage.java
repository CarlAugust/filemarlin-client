package filemarlin.filemarlinclient.filetransfer.message;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class CancelMessage implements FileTransferMessageInterface {
    public long id;
    public String reason;

    public CancelMessage(long id, String reason) {
        this.id = id;
        this.reason = reason;
    }

    @Override
    public MessageType getType() {
        return MessageType.CANCEL;
    }

    @Override
    public ByteBuffer encode() {
        var reasonData = reason.getBytes(StandardCharsets.UTF_8);

        var buffer = ByteBuffer.allocate(
                1 + Long.BYTES + Integer.BYTES + reasonData.length
        );

        buffer.put(getType().getValue());
        buffer.putLong(id);
        buffer.putInt(reasonData.length);
        buffer.put(reasonData);
        buffer.flip();

        return buffer;
    }

    public static CancelMessage decode(ByteBuffer buffer) throws Exception {
        var id = buffer.getLong();
        var reasonSize = buffer.getInt();

        if (reasonSize < 0 || reasonSize > 256) {
            throw new IllegalArgumentException(
                    "Invalid cancellation reason size: " + reasonSize
            );
        }

        var data = new byte[reasonSize];
        buffer.get(data);

        var reason = new String(data, StandardCharsets.UTF_8);

        return new CancelMessage(id, reason);
    }
}