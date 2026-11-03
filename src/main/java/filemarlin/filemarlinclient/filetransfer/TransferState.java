package filemarlin.filemarlinclient.filetransfer;

public enum TransferState {
    PENDING,
    QUEUED,
    TRANSFERRING,
    PAUSED,
    COMPLETE,
    VERIFYING,
    SUCCESS,
    FAILED
}
