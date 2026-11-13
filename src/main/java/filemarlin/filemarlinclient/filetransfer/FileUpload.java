package filemarlin.filemarlinclient.filetransfer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

public class FileUpload {

    private long id;
    private TransferState transferState;

    private Path filePath;
    private RandomAccessFile writer = null;
    private long totalBytes = 0;
    private long offset = 0;
    private final int chunkSize = 16384;

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

    public Optional<byte[]> next() throws IOException {

        var chunk = new byte[chunkSize];
        var bytesRead = writer.read(chunk);
        if (bytesRead == -1) {
            return Optional.empty();
        }

        chunk = Arrays.copyOf(chunk, bytesRead);
        offset += bytesRead;
        return Optional.of(chunk);
    }

    public void complete() {
        transferState = TransferState.VERIFYING;
    }

    public RandomAccessFile getWriter() {
        return writer;
    }
    public synchronized TransferState getTransferState() {
        return transferState;
    }

    public synchronized long getTotalBytes() {
        return totalBytes;
    }

    public long getOffset() {
        return offset;
    }

    public int getChunkSize() {
        return chunkSize;
    }
}
