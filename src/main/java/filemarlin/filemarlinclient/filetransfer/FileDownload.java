package filemarlin.filemarlinclient.filetransfer;

import java.io.RandomAccessFile;
import java.nio.file.Path;

public class FileDownload {

    private RandomAccessFile writer;
    private long id;
    private Path filePath;
    private long totalBytes;

    private TransferState transferState;

    public FileDownload(long id, Path filepath, long totalBytes) {
        this.id = id;
        this.filePath = filepath;
        this.totalBytes = totalBytes;

        transferState = TransferState.QUEUED;
    }



}
