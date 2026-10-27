package filemarlin.filemarlinclient.filetransfer;

import filemarlin.filemarlinclient.filetransfer.message.*;
import filemarlin.filemarlinclient.general.ThrowingConsumer;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class FileService {

    private final Map<Long, FileTransfer> fileTransferMap = new ConcurrentHashMap<>();
    private ThrowingConsumer<ByteBuffer> messageSender;

    public void setMessageSender(ThrowingConsumer<ByteBuffer> sender) {
        messageSender = sender;
    }

    public void send(ByteBuffer buffer) throws Exception {
        if (messageSender == null) {
            return;
        }

        messageSender.accept(buffer);
    }

    public void onMessage(ByteBuffer data) {

        var decoded_data = FileTransferMessageDecoder.decode(data);

        switch (decoded_data.getType()) {
            case TEST -> {
                var typed_data = (TestMessage)decoded_data;
            }
            case UPLOAD -> {
                var uploadMessage = (UploadFileMessage)decoded_data;
                var path = FileTransferPath.normalizePathToRoot(uploadMessage.fileName);
                if (path.isEmpty()) {
                    // TODO: Send Permission rejected Error
                    break;
                }

                var acceptMessage = new AcceptMessage(uploadMessage.id);
                send(acceptMessage.encode());

                var fileTransfer = new FileTransfer(uploadMessage.id, uploadMessage.fileSize, path.get(), true);
                fileTransferMap.put(fileTransfer.getId(), fileTransfer);

            }
            case DOWNLOAD -> {
                var downloadMessage = (DownloadFileMessage)decoded_data;
                var path =  FileTransferPath.normalizePathToRoot(downloadMessage.fileName);
                if (path.isEmpty()) {
                    // TODO: Send permission denied or file does not exist message
                    break;
                }

                var fileTransfer = new FileTransfer(downloadMessage.id, 0, path.get(), false);
                fileTransferMap.put(fileTransfer.getId(), fileTransfer);

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
