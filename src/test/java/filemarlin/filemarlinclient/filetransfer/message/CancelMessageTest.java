package filemarlin.filemarlinclient.filetransfer.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CancelMessageTest {

    @Test
    public void EncodeDecodeConsistency() throws Exception {
        var expected = new CancelMessage(22, "Cause i felt like it");
        var encoded = expected.encode();

        encoded.get();
        var actual = CancelMessage.decode(encoded);

        assertEquals(expected.id, actual.id);
        assertEquals(expected.reason, actual.reason);
    }
}
