package filemarlin.filemarlinclient.filetransfer;

import filemarlin.filemarlinclient.filetransfer.message.FileMessage;
import filemarlin.filemarlinclient.filetransfer.message.PayloadMessage;

import java.io.IOException;
import java.io.RandomAccessFile;


public class FileTransfer implements AutoCloseable {

    private RandomAccessFile writer;
    private String fileName;
    private long id;

    private Direction direction;
    private TransferState state;

    private long transferredBytes = 0;
    private long totalBytes;

    public void FileTransfer(FileMessage message) {
        this.fileName = message.fileName;
        this.totalBytes = message.fileSize;
        this.id = message.id;

        state = TransferState.QUEUED;
    }

    public void start(boolean isReceiving) {
        state = TransferState.TRANSFERRING;
        direction = isReceiving ? Direction.RECEIVING : Direction.SENDING;
    }

    // Write PayloadMessage to file
    public void write(PayloadMessage payload) {

    }

    // Read size bytes from offset of file
    public Byte[] read(long offset, long size) {

        return null;
    }

    public void complete() {
        state = TransferState.COMPLETE;
    }

    public void pause() {
        state = TransferState.PAUSED;
    }

    public void unPause() {
        state = TransferState.TRANSFERRING;
    }

    public void calculateCheckSum() throws IllegalStateException {
        if (state != TransferState.COMPLETE) {
            throw new IllegalStateException("Cannot get checksum before file transfer is complete");
        }

        state = TransferState.VERIFYING;
        // Bla bla bal calculate checksum
    }

    private void closeFile() throws IOException {
        if (writer != null) {
            writer.close();
            writer = null;
        }
    }

    @Override
    public synchronized void close() throws IOException {
        closeFile();
    }

    public String getFileName() {
        return fileName;
    }

    public long getId() {
        return id;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public synchronized long getTransferredBytes() {
        return transferredBytes;
    }

    public synchronized Direction getDirection() {
        return direction;
    }

    public synchronized TransferState getState() {
        return state;
    }

}
