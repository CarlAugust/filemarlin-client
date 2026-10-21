package filemarlin.filemarlinclient.filetransfer.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UploadFileMessageTest {

    @Test
    public void encodeDecodeConsistency() throws Exception {
        var expectedFileName = "Hello guys";
        var expected = new UploadFileMessage(22, 83838383, expectedFileName);
        var encoded = expected.encode();

        encoded.get();
        var actual = UploadFileMessage.decode(encoded);

        assertEquals(expected.id, actual.id);
        assertEquals(expected.fileSize, actual.fileSize);
        assertEquals(expected.fileName, actual.fileName);
    }
}
