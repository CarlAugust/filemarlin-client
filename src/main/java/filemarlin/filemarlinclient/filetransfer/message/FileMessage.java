package filemarlin.filemarlinclient.filetransfer.message;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class FileMessage implements FileTransferMessageInterface {
    public long id;
    public long fileSize;
    public String fileName;

    public FileMessage(long id, long fileSize, String fileName) {
        this.fileSize = fileSize;
        this.fileName = fileName;
    }

    @Override
    public MessageType getType() {
        return MessageType.FILE;
    }

    @Override
    public ByteBuffer encode() {
        var buffer = ByteBuffer.allocate(1 + Long.BYTES + Long.BYTES + Integer.BYTES + fileName.length());
        buffer.put(getType().getValue());
        buffer.putLong(id);
        buffer.putLong(fileSize);
        buffer.putInt(fileName.length());
        buffer.put(fileName.getBytes());
        buffer.flip();

        return buffer;
    }


    public static FileMessage decode(ByteBuffer buffer) throws Exception {

        var id = buffer.getLong();
        var fileSize = buffer.getLong();
        var fileNameSize = buffer.getInt();

        if (fileNameSize > 256 || fileNameSize < 0) {
            throw new IllegalArgumentException("Filename to long: " + fileNameSize);
        }

        var data = new byte[fileNameSize];
        buffer.get(data);
        var fileName =  new String(data, StandardCharsets.UTF_8);

        return new FileMessage(id, fileSize, fileName);
    }
}
