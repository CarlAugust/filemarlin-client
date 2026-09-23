package filemarlin.filemarlinclient.filetransfer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


/*
    This definitly needs more robust error handling.
*/
public class FileTransfer {

    private RandomAccessFile writer;
    private long id;
    private String fileName;
    private Path filePath;
    private Path tempFilePath;

    /*
        This needs to be updated to handle proper folder permissions and stuff?
    */
    public boolean openFile(String fileName, long id) {
        try {
            this.filePath = Path.of(fileName);
            this.tempFilePath = Path.of(fileName + ".tmp");

            Files.deleteIfExists(tempFilePath);

            this.writer = new RandomAccessFile(tempFilePath.toString(), "rw");

            this.id = id;
            this.fileName = fileName;
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public boolean writeFile(byte[] data, long offset, long id) {
        if (id != this.id) {
            return false;
        }

        try {
            writer.seek(offset);
            writer.write(data);

        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public void cancel() {
        try {
            if (writer != null) {
                writer.close();
            }

            Files.deleteIfExists(tempFilePath);
        } catch (IOException e) {
            // WELPP... THATS TERRIBLE? TODO!!!!
        }
    }

    public boolean complete() {
        try {
            writer.getFD().sync();
            writer.close();

            Files.move(tempFilePath, filePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            return false;
        }
        return true;
    }



}
