package filemarlin.filemarlinclient.filetransfer;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static filemarlin.filemarlinclient.filetransfer.FileTransferPath.normalizePathToRoot;

public class FileTransferPathTest {

    @Test
    public void normalizingIllegalPathReturnsNull() {
        assert(normalizePathToRoot("/hi/boo/../../../../../../../hi/hello").isEmpty());
        assert(normalizePathToRoot("potato").isPresent());
    }
}
