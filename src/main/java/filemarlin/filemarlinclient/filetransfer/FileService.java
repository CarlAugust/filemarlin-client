package filemarlin.filemarlinclient.filetransfer;

import filemarlin.filemarlinclient.filetransfer.message.*;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class FileService {

    private final Map<Long, FileTransfer> fileTransferMap = new ConcurrentHashMap<>();
    private Consumer<ByteBuffer> messageSender;

    public void setMessageSender(Consumer<ByteBuffer> sender) {
        messageSender = sender;
    }

    public void send(ByteBuffer buffer) {
        if (messageSender != null) {
            messageSender.accept(buffer);
        }
    }

    public void onMessage(ByteBuffer data) {

        var decoded_data = FileTransferMessageDecoder.decode(data);

        switch (decoded_data.getType()) {
            case TEST -> {
                var typed_data = (TestMessage)decoded_data;
            }
            case UPLOAD -> {
                var typed_data = (UploadFileMessage)decoded_data;
                var path = FileTransferPath.normalizePathToRoot(typed_data.fileName);
                if (path.isEmpty()) {
                    // Send Permission rejected Error
                    break;
                }

                var fileTransfer = new FileTransfer(typed_data.id, typed_data.fileSize, path.get());

                fileTransferMap.put(fileTransfer.getId(), fileTransfer);
            }
            case DOWNLOAD -> {
                var typed_data = (DownloadFileMessage)decoded_data;
            }
            case ACCEPT -> {
                var typed_data = (AcceptMessage)decoded_data;
            }
            case PAYLOAD -> {
                var typed_data = (PayloadMessage)decoded_data;
            }
            case RECEIVED -> {
                var typed_data = (ReceivedMessage)decoded_data;
            }
            case COMPLETE -> {
                var typed_data = (CompleteMessage)decoded_data;
            }
            case CANCEL -> {
                var typed_data = (CancelMessage)decoded_data;
            }
            case ERROR -> {
                var typed_data = (ErrorMessage)decoded_data;
            }
        }
    }

    public FileTransfer getFileTransfer(long id) {
        if (!fileTransferMap.containsKey(id)) {
            return null;
        }

        return fileTransferMap.get(id);
    }
}
