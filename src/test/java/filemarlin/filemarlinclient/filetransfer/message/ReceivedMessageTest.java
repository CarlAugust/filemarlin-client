package filemarlin.filemarlinclient.filetransfer.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReceivedMessageTest {

    @Test
    public void EncodeDecodeConsistency() throws Exception {
        var expected = new ReceivedMessage(22, 389238932, 16248);
        var encoded = expected.encode();

        encoded.get();
        var actual = ReceivedMessage.decode(encoded);

        assertEquals(expected.id, actual.id);
        assertEquals(expected.offset, actual.offset);
        assertEquals(expected.size, actual.size);
    }
}
