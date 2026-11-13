package filemarlin.filemarlinclient.filetransfer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

public class FileDownload {

    private RandomAccessFile writer;
    private long id;
    private Path filePath;
    private long totalBytes = 0;

    private TransferState transferState;

    public FileDownload(long id, Path filepath, long totalBytes) {
        this.id = id;
        this.filePath = filepath;
        this.totalBytes = totalBytes;

        transferState = TransferState.QUEUED;
    }

    public void start() throws IOException {
        writer = new RandomAccessFile(filePath.toString() + ".tmp", "rw");
        transferState = TransferState.TRANSFERRING;
    }

    public void write(long offset, byte[] data) throws IOException {
        writer.seek(offset);
        writer.write(data);
    }

    public void complete() {
        transferState = TransferState.VERIFYING;
    }

    public RandomAccessFile getWriter() {
        return writer;
    }
}
