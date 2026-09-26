package filemarlin.filemarlinclient.filetransfer;

import java.nio.ByteBuffer;

public class ErrorMessage implements FileTransferMessageInterface {
    public long id;
    public ErrorCodes errorCode;
    public long messageSize;
    public String message;


    public ErrorMessage(long id, ErrorCodes errorCode, long messageSize, String message) {
        this.id = id;
        this.errorCode = errorCode;
        this.messageSize = messageSize;
        this.message = message;
    }

    @Override
    public MessageType getType() {
        return MessageType.ERROR;
    }


    @Override
    public ByteBuffer encode() {
        return null;
    }
}
