package filemarlin.filemarlinclient.filetransfer.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChunkMessageTest {

    @Test
    public void EncodeDecodeConsistency() throws Exception {
        var data = new byte[2];
        data[0] = 1;
        data[1] = 2;
        var expected = new ChunkMessage(22, 389238932, data.length, data);
        var encoded = expected.encode();

        encoded.get();
        var actual = ChunkMessage.decode(encoded);

        assertEquals(expected.id, actual.id);
        assertEquals(expected.offset, actual.offset);
        assertEquals(expected.size, actual.size);
        assertArrayEquals(expected.data, actual.data);
    }
}
