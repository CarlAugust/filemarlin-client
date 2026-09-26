package filemarlin.filemarlinclient.filetransfer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMessageTest {

    @Test
    public void encodeDecodeConsistency() throws Exception {

        var str = "hello";
        var expected = new TestMessage(str.length(), str.getBytes());
        var encoded = expected.encode();
        encoded.get();

        var actual = TestMessage.decode(encoded);

        assertEquals(expected.getType(), actual.getType());
        assertArrayEquals(expected.getData(), actual.getData());
    }
}
