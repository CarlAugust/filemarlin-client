package filemarlin.filemarlinclient.filetransfer;

import java.io.RandomAccessFile;
import java.nio.file.Path;

public class FileUpload {

    private RandomAccessFile writer = null;
    private long id;
    private Path filePath;


    private long offset = 0;
    private long totalBytes = 0;
    private TransferState transferState;

    public FileUpload(long id, Path filePath) {
        this.id = id;
        this.filePath = filePath;

        this.transferState = TransferState.QUEUED;
    }

    public void start() {

    }


}
