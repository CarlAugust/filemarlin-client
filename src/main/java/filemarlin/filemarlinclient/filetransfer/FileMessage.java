package filemarlin.filemarlinclient.filetransfer;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class FileMessage implements FileTransferMessageInterface {
    long id;
    long fileSize;
    int fileNameSize;
    String fileName;

    public FileMessage(long id, long fileSize, int fileNameSize, String fileName) {
        this.id = id;
        this.fileSize = fileSize;
        this.fileNameSize = fileNameSize;
        this.fileName = fileName;
    }

    @Override
    public MessageType getType() {
        return null;
    }

    @Override
    public ByteBuffer encode() {
        var buffer = ByteBuffer.allocate(1 + Long.BYTES + Integer.BYTES + fileNameSize);
        buffer.put(getType().getValue());
        buffer.putLong(fileSize);
        buffer.putInt(fileNameSize);
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
        var fileName =  new String(data, StandardCharsets.UTF_8);

        return new FileMessage(id, fileSize, fileNameSize, fileName);
    }
}
