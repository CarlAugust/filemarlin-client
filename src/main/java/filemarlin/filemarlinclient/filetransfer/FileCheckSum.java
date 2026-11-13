package filemarlin.filemarlinclient.filetransfer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.CRC32;

public class FileCheckSum {

    public static long generateCheckSum(RandomAccessFile writer) throws IOException {
        writer.seek(0);
        CRC32 crc = new CRC32();

        var buffer = new byte[8192];
        int bytesRead;

        while ((bytesRead = writer.read(buffer)) != -1) {
            crc.update(buffer, 0, bytesRead);
        }

        return crc.getValue();
    }
}
