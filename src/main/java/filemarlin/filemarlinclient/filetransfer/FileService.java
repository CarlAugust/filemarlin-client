package filemarlin.filemarlinclient.filetransfer;

import filemarlin.filemarlinclient.filetransfer.message.*;
import filemarlin.filemarlinclient.general.ThrowingConsumer;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    public void onMessage(ByteBuffer buffer) {

        var type = buffer.get();
        var messageType = MessageType.fromByte(type);
        try {
            switch (messageType) {
                case TEST -> {
                    var typed_data = TestMessage.decode(buffer);
                }
                case UPLOAD_REQUEST -> {
                    var uploadRequest = UploadRequest.decode(buffer);
                    handleUploadRequest(uploadRequest);
                }
                case DOWNLOAD_REQUEST -> {
                    var downloadMessage = DownloadRequest.decode(buffer);

                }
                case ACCEPT_TRANSFER -> {
                }
                case PAYLOAD -> {
                }
                case RECEIVED -> {
                }
                case COMPLETE_TRANSFER -> {
                }
                case CANCEL_TRANSFER -> {
                }
                case ERROR -> {
                }
            }
        } catch (Exception e) {
            // TODO: Reject message
        }
    }

    public void handleUploadRequest(UploadRequest uploadRequest) {

        var path = FileTransferPath.normalizePathToRoot(uploadRequest.fileName);
        if (path.isEmpty()) {
            // TODO: Send Permission rejected Error
        }

        var acceptMessage = new AcceptTransfer(uploadRequest.id);
        try {
            send(acceptMessage.encode());
        } catch (Exception e) {
            // TODO: Should cancel if fails

        }

        var fileTransfer = new FileTransfer(uploadRequest.id, uploadRequest.fileSize, path.get(), true);
        fileTransferMap.put(fileTransfer.getId(), fileTransfer);
    }

    public void handleDownloadRequest(DownloadRequest downloadRequest) {


        var path =  FileTransferPath.normalizePathToRoot(downloadRequest.fileName);
        if (path.isEmpty()) {
            // TODO: Send permission denied or file does not exist message
        }

        var fileTransfer = new FileTransfer(downloadRequest.id, 0, path.get(), false);
        fileTransferMap.put(fileTransfer.getId(), fileTransfer);

    }

    public FileTransfer getFileTransfer(long id) {
        if (!fileTransferMap.containsKey(id)) {
            return null;
        }

        return fileTransferMap.get(id);
    }

}
