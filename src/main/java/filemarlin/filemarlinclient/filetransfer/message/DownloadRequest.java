package filemarlin.filemarlinclient.filetransfer.message;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DownloadRequest implements FileTransferMessageInterface {
    public long id;
    public String fileSource;
    public String fileDestination;

    public DownloadRequest(long id, String fileSource, String fileDestination) {
        this.id = id;
        this.fileSource = fileSource;
        this.fileDestination = fileDestination;
    }

    @Override
    public MessageType getType() {
        return MessageType.UPLOAD_REQUEST;
    }

    @Override
    public ByteBuffer encode() {

        var fileSourceBytes = fileSource.getBytes(StandardCharsets.UTF_8);
        var fileDestinationBytes = fileDestination.getBytes(StandardCharsets.UTF_8);

        var buffer = ByteBuffer.allocate(1 + Long.BYTES + Integer.BYTES + fileSourceBytes.length + Integer.BYTES + fileDestinationBytes.length);
        buffer.put(getType().getValue());
        buffer.putLong(id);

        buffer.putInt(fileSource.length());
        buffer.put(fileSourceBytes);

        buffer.putInt(fileDestination.length());
        buffer.put(fileDestinationBytes);

        buffer.flip();

        return buffer;
    }


    public static DownloadRequest decode(ByteBuffer buffer) throws Exception {

        var id = buffer.getLong();

        var fileSourceSize = buffer.getInt();
        if (fileSourceSize > 256 || fileSourceSize < 0) {
            throw new IllegalArgumentException("Filename to long: " + fileSourceSize);
        }

        var fileSourceBytes = new byte[fileSourceSize];
        buffer.get(fileSourceBytes);
        var fileSource =  new String(fileSourceBytes, StandardCharsets.UTF_8);

        var fileDestinationSize = buffer.getInt();
        if (fileDestinationSize > 256 || fileDestinationSize < 0) {
            throw new IllegalArgumentException("Filename to long: " + fileSourceSize);
        }

        var fileDestinationBytes = new byte[fileDestinationSize];
        buffer.get(fileDestinationBytes);
        var fileDestination =  new String(fileDestinationBytes, StandardCharsets.UTF_8);

        return new DownloadRequest(id, fileSource, fileDestination);
    }
}
