package filemarlin.filemarlinclient.filetransfer.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DownloadRequestTest {

    @Test
    public void encodeDecodeConsistency() throws Exception {
        var expectedFileName = "Hello guys";
        var expected = new DownloadRequest(22, expectedFileName);
        var encoded = expected.encode();

        encoded.get();
        var actual = DownloadRequest.decode(encoded);

        assertEquals(expected.id, actual.id);
        assertEquals(expected.fileName, actual.fileName);
    }
}
