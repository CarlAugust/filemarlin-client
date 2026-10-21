package filemarlin.filemarlinclient.filetransfer;

import filemarlin.filemarlinclient.filetransfer.message.UploadFileMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileServiceTest {


    @Test
    public void createFileTransferUponUploadMessageReceive() {
        var service = new FileService();
        var expectedId = 22;

        var message = new UploadFileMessage(expectedId, 32, "potatotest");

        service.onMessage(message.encode());

        var actual = service.getFileTransfer(expectedId);
        assertNotNull(actual);

    }
}
