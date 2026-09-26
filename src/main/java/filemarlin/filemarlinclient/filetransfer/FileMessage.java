package filemarlin.filemarlinclient.filetransfer;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class FileMessage implements FileTransferMessageInterface {
    public long id;
    public long fileSize;
    public int fileNameSize;
    public String fileName;

    public FileMessage(long id, long fileSize, int fileNameSize, String fileName) {
        this.fileSize = fileSize;
        this.fileNameSize = fileNameSize;
        this.fileName = fileName;
    }

    @Override
    public MessageType getType() {
        return MessageType.FILE;
    }

    @Override
    public ByteBuffer encode() {
        var buffer = ByteBuffer.allocate(1 + Long.BYTES + Long.BYTES + Integer.BYTES + fileNameSize);
        buffer.put(getType().getValue());
        buffer.putLong(id);
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
        buffer.get(data);
        var fileName =  new String(data, StandardCharsets.UTF_8);

        return new FileMessage(id, fileSize, fileNameSize, fileName);
    }
}
