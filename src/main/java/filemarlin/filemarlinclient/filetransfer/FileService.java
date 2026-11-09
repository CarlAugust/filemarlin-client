package filemarlin.filemarlinclient.filetransfer;

import filemarlin.filemarlinclient.filetransfer.message.*;
import filemarlin.filemarlinclient.general.ThrowingConsumer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class FileService {

    private final Map<Long, FileUpload> fileUploadMap = new ConcurrentHashMap<>();
    private final BlockingQueue<Long> fileUploadQueue = new LinkedBlockingQueue<>();

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

    // -------------------------------------------------------------------------
    // Async running functions?

    /*
        NOTE FOR THIS FUNCTION startTransfer
        There might be a race condition on sending a start message and starting a transfer, if the file service
        has an async task that automatically sends from current transferring uploads. I think the simple fix is to
        allow this async to start the transfer, forcing it to send a start transfer prior to sending chunks.
        Because send is blocking for a thread.
    */
    public void startTransfer(long id) {
        if (!fileUploadMap.containsKey(id)) {
            System.out.println("Cannot start transfer for unexisting id");
        }

        var upload = fileUploadMap.get(id);
        try {
            upload.start();
        } catch (IOException e) {
            System.out.println("Error starting transfer: " + e);
        }

        var startTransfer = new StartTransfer(id);
        send(startTransfer.encode());
    }

    public void transferLoop() {
        long id = 0;
        boolean validId = false;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (!validId) {
                    id = fileUploadQueue.take();
                    validId = true;
                    startTransfer(id);
                }

                var upload = fileUploadMap.get(id);

                Optional<byte[]> chunk;
                while ((chunk = upload.next()).isPresent()) {
                    var chunkMessage = new ChunkMessage(id, upload.getOffset(), upload.getChunkSize(), chunk.get());
                    send(chunkMessage.encode());
                }

                id = 0;
                validId = false;
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                // TODO: Fail upload and send cancel message
            }

        }
    }

    // -------------------------------------------------------------------------------------------
    // Message handling

    public void onMessage(ByteBuffer buffer) {

        var type = buffer.get();
        var messageType = MessageType.fromByte(type);

        try {
            switch (messageType) {
                case TEST -> {
                    var testMessage = TestMessage.decode(buffer);
                    System.out.println(Arrays.toString(testMessage.getData()));
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
                case START_TRANSFER -> {
                    var id = StartTransfer.decode(buffer).id;

                }
                case CHUNK -> {
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

    private void handleUploadRequest(UploadRequest uploadRequest) {

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

    private void handleDownloadRequest(DownloadRequest downloadRequest) {

        var path =  FileTransferPath.verifyLegalAndNormalizePathToRoot(downloadRequest.fileSource);
        if (path.isEmpty()) {
            // TODO: Send permission denied or file does not exist message
            return;
        }

        if (fileUploadMap.containsKey(downloadRequest.id)) {
            // TODO: Send non unique id error
            return;
        }

        var fileUpload = new FileUpload(downloadRequest.id, path.get());
        fileUploadMap.put(downloadRequest.id, fileUpload);

        var uploadRequest = new UploadRequest(downloadRequest.id, fileUpload.getTotalBytes(), downloadRequest.fileDestination);
        send(uploadRequest.encode());
    }

    private void handleAcceptUpload(AcceptUpload acceptUpload) {
        if (!fileUploadMap.containsKey(acceptUpload.id)) {
            // TODO: WTF??? Why are you accepting unexistent things
            return;
        }

        fileUploadMap.get(acceptUpload.id).approve();
        fileUploadQueue.add(acceptUpload.id);
    }

    public Map<Long, FileUpload> getUploads() {
        return Collections.unmodifiableMap(fileUploadMap);
    }

    public Map<Long, FileDownload> getDownloads() {
        return Collections.unmodifiableMap(fileDownloadMap);
    }

    private void handleStartTransfer(long id) {
        if (!fileDownloadMap.containsKey(id)) {
           // TODO: BRO YOU NEVER REQUESTED THIS
           return;
        }

        var download = fileDownloadMap.get(id);
        try {
            download.start();
        } catch (IOException e) {
            var cancel = new CancelMessage(id);
            send(cancel.encode());
        }
    }

    private void handleIncomingChunk(ChunkMessage chunkMessage) {
        if (!fileDownloadMap.containsKey(chunkMessage.id)) {
            // TODO: BRO WTHAT ARE YOU EVEN TRANSFERING TOO????
            return;
        }

        var download = fileDownloadMap.get(chunkMessage.id);
        try {
            download.write(chunkMessage.offset, chunkMessage.data);

            var receivedChunk = new ReceivedChunk(chunkMessage.id, chunkMessage.offset, chunkMessage.size);
            send(receivedChunk.encode());
        } catch (IOException e) {
            // TODO: WHAT SHOULD HAPPEN HERE
            return;
        }
    }
}
