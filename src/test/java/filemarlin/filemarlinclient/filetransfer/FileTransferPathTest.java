package filemarlin.filemarlinclient.filetransfer;

import org.junit.jupiter.api.Test;

import static filemarlin.filemarlinclient.filetransfer.FileTransferPath.verifyLegalAndNormalizePathToRoot;

public class FileTransferPathTest {

    @Test
    public void normalizingIllegalPathReturnsNull() {
        assert(verifyLegalAndNormalizePathToRoot("/hi/boo/../../../../../../../hi/hello").isEmpty());
        assert(verifyLegalAndNormalizePathToRoot("potato").isPresent());
    }
}
