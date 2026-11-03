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

    public FileUpload(long id, Path filePath) throws IOException {
        this.id = id;
        this.filePath = filePath;

        this.transferState = TransferState.PENDING;

        // Assuming all Uploads will pass because uploads will happen via a client interface, though security should of course be added

        writer = new RandomAccessFile(filePath.toFile(), "r");
        totalBytes = writer.length();
    }

    public void start() {

    }

    public synchronized long getTotalBytes() {
        return this.totalBytes;
    }

}
