package filemarlin.filemarlinclient.filetransfer;

import java.nio.ByteBuffer;

public interface FileTransferMessageInterface {

    // A function should also have a decode. Its important to take into account that the type
    // will be extracted from the buffer before the decode function is run in CustomDataChannelObserver

    MessageType getType();
    public ByteBuffer encode();
}
