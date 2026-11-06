package filemarlin.filemarlinclient.filetransfer.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AcceptUploadTest {

    @Test
    public void EncodeDecodeConsistency() throws Exception {
        var expected = new AcceptUpload(22);
        var encoded = expected.encode();

        encoded.get();
        var actual = AcceptUpload.decode(encoded);

        assertEquals(expected.id, actual.id);
    }
}
