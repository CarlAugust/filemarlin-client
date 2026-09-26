package filemarlin.filemarlinclient.filetransfer;

import java.nio.ByteBuffer;

public interface FileTransferMessageInterface {



    MessageType getType();
    public ByteBuffer encode();
}
