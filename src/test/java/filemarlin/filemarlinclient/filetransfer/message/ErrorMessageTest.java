package filemarlin.filemarlinclient.filetransfer.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorMessageTest {


    @Test
    public void EncodeDecodeConsistency() throws Exception {
        var errorMessage = "Hello world";
        var expected = new ErrorMessage(22, ErrorCodes.DECODE_ERROR_LOCAL, errorMessage);
        var encoded = expected.encode();

        encoded.get();
        var actual = ErrorMessage.decode(encoded);

        assertEquals(expected.id, actual.id);
        assertEquals(expected.code, actual.code);
        assertEquals(expected.message, actual.message);
    }

}
