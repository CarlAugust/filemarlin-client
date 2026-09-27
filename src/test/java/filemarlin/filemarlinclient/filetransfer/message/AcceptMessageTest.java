package filemarlin.filemarlinclient.filetransfer.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AcceptMessageTest {

    @Test
    public void EncodeDecodeConsistency() throws Exception {
        var expected = new AcceptMessage(22);
        var encoded = expected.encode();

        encoded.get();
        var actual = AcceptMessage.decode(encoded);

        assertEquals(expected.id, actual.id);
    }
}
