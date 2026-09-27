package filemarlin.filemarlinclient.filetransfer.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompleteMessageTest {

    @Test
    public void EncodeDecodeConsistency() throws Exception {
        var expected = new CompleteMessage(22);
        var encoded = expected.encode();

        encoded.get();
        var actual = CompleteMessage.decode(encoded);

        assertEquals(expected.id, actual.id);
    }
}
