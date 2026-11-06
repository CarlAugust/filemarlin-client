package filemarlin.filemarlinclient.filetransfer;

import filemarlin.filemarlinclient.filetransfer.message.*;
import filemarlin.filemarlinclient.general.ThrowingConsumer;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FileService {

    private final Map<Long, FileUpload> fileUploadMap = new ConcurrentHashMap<>();
    private final Map<Long, FileDownload> fileDownloadMap = new ConcurrentHashMap<>();

    private ThrowingConsumer<ByteBuffer> messageSender;

    public void setMessageSender(ThrowingConsumer<ByteBuffer> sender) {
        messageSender = sender;
    }

    public void send(ByteBuffer buffer) {
        try {
            if (messageSender != null) {
                messageSender.accept(buffer);
            }
        } catch (Exception e) {
            // TODO: What if message sent fails?
            System.out.println(e.toString());
        }
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
                    handleDownloadRequest(downloadMessage);
                }
                case ACCEPT_UPLOAD -> {
                    var acceptUpload = AcceptUpload.decode(buffer);
                    handleAcceptUpload(acceptUpload);
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

        var path = FileTransferPath.verifyLegalAndNormalizePathToRoot(uploadRequest.fileDestination);
        if (path.isEmpty()) {
            // TODO: Send Permission rejected Error
            return;
        }

        var fileDownload = new FileDownload(uploadRequest.id, path.get(), uploadRequest.fileSize);
        fileDownloadMap.put(uploadRequest.id, fileDownload);

        var acceptMessage = new AcceptUpload(uploadRequest.id);
        send(acceptMessage.encode());
    }

    public void handleDownloadRequest(DownloadRequest downloadRequest) {


        var path =  FileTransferPath.verifyLegalAndNormalizePathToRoot(downloadRequest.fileSource);
        if (path.isEmpty()) {
            // TODO: Send permission denied or file does not exist message
            return;
        }

        if (!fileUploadMap.containsKey(downloadRequest.id)) {
            // TODO: Send non unique id error
            return;
        }

        var fileUpload = new FileUpload(downloadRequest.id, path.get());
        fileUploadMap.put(downloadRequest.id, fileUpload);

        var uploadRequest = new UploadRequest(downloadRequest.id, fileUpload.getTotalBytes(), downloadRequest.fileDestination);
        send(uploadRequest.encode());
    }

    public void handleAcceptUpload(AcceptUpload acceptUpload) {
        if (!fileUploadMap.containsKey(acceptUpload.id)) {
            // TODO: WTF??? Why are you accepting unexistent things
            return;
        }

        fileUploadMap.get(acceptUpload.id).approve();
    }

    public Map<Long, FileUpload> getUploads() {
        return Collections.unmodifiableMap(fileUploadMap);
    }

    public Map<Long, FileDownload> getDownloads() {
        return Collections.unmodifiableMap(fileDownloadMap);
    }
}
