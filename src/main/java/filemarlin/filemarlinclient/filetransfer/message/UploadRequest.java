package filemarlin.filemarlinclient.filetransfer.message;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class UploadRequest implements FileTransferMessageInterface {
    public long id;
    public long fileSize;
    public String fileDestination;

    public UploadRequest(long id, long fileSize, String fileDestination) {
        this.id = id;
        this.fileSize = fileSize;
        this.fileDestination = fileDestination;
    }

    @Override
    public MessageType getType() {
        return MessageType.UPLOAD_REQUEST;
    }

    @Override
    public ByteBuffer encode() {
        var fileDestinationBytes = fileDestination.getBytes(StandardCharsets.UTF_8);
        var buffer = ByteBuffer.allocate(1 + Long.BYTES + Long.BYTES + Integer.BYTES + fileDestinationBytes.length);

        buffer.put(getType().getValue());
        buffer.putLong(id);
        buffer.putLong(fileSize);
        buffer.putInt(fileDestinationBytes.length);
        buffer.put(fileDestinationBytes);
        buffer.flip();

        return buffer;
    }


    public static UploadRequest decode(ByteBuffer buffer) throws Exception {

        var id = buffer.getLong();
        var fileSize = buffer.getLong();

        var fileDestinationSize = buffer.getInt();
        if (fileDestinationSize > 256 || fileDestinationSize < 0) {
            throw new IllegalArgumentException("Filename to long: " + fileDestinationSize);
        }

        var fileDestinationBytes = new byte[fileDestinationSize];
        buffer.get(fileDestinationBytes);
        var fileDestination =  new String(fileDestinationBytes, StandardCharsets.UTF_8);

        return new UploadRequest(id, fileSize, fileDestination);
    }
}
