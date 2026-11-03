package filemarlin.filemarlinclient.filetransfer;

import filemarlin.filemarlinclient.filetransfer.message.*;
import filemarlin.filemarlinclient.general.ThrowingConsumer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FileService {

    private final Map<Long, FileUpload> fileUploadMap = new ConcurrentHashMap<>();
    private final Map<Long, FileDownload> fileDownloadMap = new ConcurrentHashMap<>();

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

        var path = FileTransferPath.normalizePathToRoot(uploadRequest.fileDestination);
        if (path.isEmpty()) {
            // TODO: Send Permission rejected Error
            return;
        }

        var acceptMessage = new AcceptTransfer(uploadRequest.id);
        try {
            send(acceptMessage.encode());
        } catch (Exception e) {
            // TODO: Should cancel if fails

        }

        var fileDownload = new FileDownload(uploadRequest.id, path.get(), uploadRequest.fileSize);
        fileDownloadMap.put(uploadRequest.id, fileDownload);
    }

    public void handleDownloadRequest(DownloadRequest downloadRequest) {


        var path =  FileTransferPath.normalizePathToRoot(downloadRequest.fileSource);
        if (path.isEmpty()) {
            // TODO: Send permission denied or file does not exist message
            return;
        }

        if (fileUploadMap.containsKey(downloadRequest.id)) {
            // TODO: Send non unique id error
            return;
        }

        try {
            var fileUpload = new FileUpload(downloadRequest.id, path.get());
            fileUploadMap.put(downloadRequest.id, fileUpload);

            var uploadRequest = new UploadRequest(downloadRequest.id, fileUpload.getTotalBytes(), downloadRequest.fileDestination);
            messageSender.accept(uploadRequest.encode());

        } catch (Exception e) {
            // TODO: What should i do when this happens? I might not need to handle anything because silence will be the error... maybe a bad decision
            System.out.println(e.toString());
            fileUploadMap.computeIfPresent(downloadRequest.id, (k, v) -> fileUploadMap.remove(k));
        }

    }

}
