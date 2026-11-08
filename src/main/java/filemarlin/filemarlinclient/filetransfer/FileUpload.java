package filemarlin.filemarlinclient.filetransfer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

public class FileUpload {

    private long id;
    private TransferState transferState;

    private Path filePath;
    private RandomAccessFile writer = null;
    private long totalBytes = 0;
    private long offset = 0;

    public FileUpload(long id, Path filePath) {
        this.id = id;
        this.filePath = filePath;

        totalBytes = filePath.toFile().length();
        transferState = TransferState.PENDING;
    }

    public void approve() {
        transferState = TransferState.QUEUED;
    }

    public void start() throws IOException {
        writer = new RandomAccessFile(filePath.toFile(), "r");
        transferState = TransferState.TRANSFERRING;
    }

    public synchronized TransferState getTransferState() {
        return transferState;
    }

    public synchronized long getTotalBytes() {
        return this.totalBytes;
    }

}
