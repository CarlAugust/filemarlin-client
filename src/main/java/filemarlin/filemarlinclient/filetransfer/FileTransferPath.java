package filemarlin.filemarlinclient.filetransfer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;


/*
    This class should handle permission of files ect...
    I suppose in the future it should also be able to act upon paths that are permitted.
*/
public class FileTransferPath {

    // https://stackoverflow.com/questions/53126527/safe-way-to-resolve-java-path
    public static Optional<Path> normalizePathToRoot(String stringPath) {

        Path root = Paths.get("").toAbsolutePath().normalize();
        Path path = root.normalize().resolve(stringPath).normalize();

        if(path.startsWith(root) && !Files.isSymbolicLink(path))
            return Optional.of(path);

        return Optional.empty();
    }
}
