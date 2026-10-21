package filemarlin.filemarlinclient.filetransfer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;


public class FileTransfer implements AutoCloseable {

    private RandomAccessFile writer;
    private long id;
    private Path filePath;

    private Direction direction;
    private TransferState state;

    private long transferredBytes = 0;
    private long totalBytes;


    public FileTransfer(long id, long totalBytes, Path filePath) {
        this.id = id;
        this.totalBytes = totalBytes;
        this.filePath = filePath;

        state = TransferState.QUEUED;
    }

    public void start(boolean isReceiving) throws IllegalStateException, IOException {

        if (state != TransferState.QUEUED) {
            throw new IllegalStateException("Cannot start file transfer in state " + state);
        }
        direction = isReceiving ? Direction.RECEIVING : Direction.SENDING;

        if (direction == Direction.RECEIVING) {
            writer = new RandomAccessFile(filePath.toFile(), "rw");
        } else {
            writer = new RandomAccessFile(filePath.toFile(), "r");

            // Update total byte size as reader
            totalBytes = writer.length();
        }

        state = TransferState.TRANSFERRING;
    }

    private boolean isWithinBounds(long offset, long size) {
        return offset >= 0
                && size >= 0
                && offset <= totalBytes
                && size <= totalBytes - offset;
    }

    // Write PayloadMessage to file
    public void write(long offset, byte[] data) throws IOException, IllegalArgumentException {
        if (state != TransferState.TRANSFERRING) {
            throw new IllegalStateException("Cannot write when the state is not in Transferring");
        }

        if (!isWithinBounds(offset, data.length)) {
            throw new IllegalArgumentException("Data size overflows the total file size");
        }

        writer.seek(offset);
        writer.write(data);
    }

    // Read size bytes from offset of file
    public byte[] read(long offset, int size) throws IOException {
        if (state != TransferState.TRANSFERRING) {
            throw new IllegalStateException("Cannot read when the state is not in Transferring");
        }

        if (!isWithinBounds(offset, size)) {
            throw new IllegalArgumentException("Trying to read outside of file size");
        }

        writer.seek(offset);
        var data = new byte[size];
        writer.readFully(data);
        return data;
    }

    public void complete() throws IOException {
        if (state != TransferState.TRANSFERRING) {
            throw new IllegalStateException("Cannot complete a non transferring transfer");
        }


        writer.getFD().sync();

        state = TransferState.COMPLETE;
        closeFile();
    }

    public void pause() {
        state = TransferState.PAUSED;
    }

    public void unPause() {
        state = TransferState.TRANSFERRING;
    }

    // I want to do this later or something.
    public String calculateCheckSum() throws IllegalStateException {
        if (state != TransferState.COMPLETE) {
            throw new IllegalStateException("Cannot get checksum before file transfer is complete");
        }

        state = TransferState.VERIFYING;
        // Bla bla bal calculate checksum

        return "THIS CHECKSUM DOES NOT EXIST";
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

    public Path getFilePath() {
        return filePath;
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
