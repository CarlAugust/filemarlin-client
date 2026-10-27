package filemarlin.filemarlinclient.filetransfer.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AcceptTransferTest {

    @Test
    public void EncodeDecodeConsistency() throws Exception {
        var expected = new AcceptTransfer(22);
        var encoded = expected.encode();

        encoded.get();
        var actual = AcceptTransfer.decode(encoded);

        assertEquals(expected.id, actual.id);
    }
}
