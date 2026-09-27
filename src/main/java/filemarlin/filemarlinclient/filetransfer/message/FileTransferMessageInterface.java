package filemarlin.filemarlinclient.filetransfer.message;

import java.nio.ByteBuffer;

public interface FileTransferMessageInterface {
    MessageType getType();
    public ByteBuffer encode();
}
