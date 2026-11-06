package filemarlin.filemarlinclient.filetransfer;

import filemarlin.filemarlinclient.filetransfer.message.UploadRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FileServiceTest {


    @Test
    public void createFileTransferUponUploadMessageReceive() {
        var service = new FileService();
        long expectedId = 22;

        var message = new UploadRequest(expectedId, 32, "potatotest");

        service.onMessage(message.encode());

        var downloads = service.getDownloads();
        assertEquals(downloads.size(), 1, "Has one download");
        assertTrue(downloads.containsKey(expectedId), "Check if download exists after a upload has been requested");
    }
}
