package filemarlin.filemarlinclient.filetransfer;

public enum TransferState {
    QUEUED,
    TRANSFERRING,
    PAUSED,
    COMPLETE,
    VERIFYING,
    FAILED
}
