package filemarlin.filemarlinclient.filetransfer.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UploadRequestTest {

    @Test
    public void encodeDecodeConsistency() throws Exception {
        var expectedFileName = "Hello guys";
        var expected = new UploadRequest(22, 83838383, expectedFileName);
        var encoded = expected.encode();

        encoded.get();
        var actual = UploadRequest.decode(encoded);

        assertEquals(expected.id, actual.id);
        assertEquals(expected.fileSize, actual.fileSize);
        assertEquals(expected.fileName, actual.fileName);
    }
}
