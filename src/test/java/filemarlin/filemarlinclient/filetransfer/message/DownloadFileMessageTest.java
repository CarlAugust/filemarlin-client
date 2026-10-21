package filemarlin.filemarlinclient.filetransfer.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DownloadFileMessageTest {

    @Test
    public void encodeDecodeConsistency() throws Exception {
        var expectedFileName = "Hello guys";
        var expected = new DownloadFileMessage(22, expectedFileName);
        var encoded = expected.encode();

        encoded.get();
        var actual = DownloadFileMessage.decode(encoded);

        assertEquals(expected.id, actual.id);
        assertEquals(expected.fileName, actual.fileName);
    }
}
