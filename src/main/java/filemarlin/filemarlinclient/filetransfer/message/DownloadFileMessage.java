package filemarlin.filemarlinclient.filetransfer.message;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DownloadFileMessage implements FileTransferMessageInterface {
    public long id;
    public String fileName;

    public DownloadFileMessage(long id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    @Override
    public MessageType getType() {
        return MessageType.UPLOAD;
    }

    @Override
    public ByteBuffer encode() {
        var buffer = ByteBuffer.allocate(1 + Long.BYTES + Integer.BYTES + fileName.length());
        buffer.put(getType().getValue());
        buffer.putLong(id);
        buffer.putInt(fileName.length());
        buffer.put(fileName.getBytes());
        buffer.flip();

        return buffer;
    }


    public static DownloadFileMessage decode(ByteBuffer buffer) throws Exception {

        var id = buffer.getLong();
        var fileNameSize = buffer.getInt();

        if (fileNameSize > 256 || fileNameSize < 0) {
            throw new IllegalArgumentException("Filename to long: " + fileNameSize);
        }

        var data = new byte[fileNameSize];
        buffer.get(data);
        var fileName =  new String(data, StandardCharsets.UTF_8);

        return new DownloadFileMessage(id, fileName);
    }
}
