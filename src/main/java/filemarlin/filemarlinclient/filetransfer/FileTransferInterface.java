package filemarlin.filemarlinclient.filetransfer;

import java.nio.ByteBuffer;
import filemarlin.filemarlinclient.filetransfer.MessageType;

public interface FileTransferInterface {
    MessageType getType();
    public ByteBuffer encode();
}
