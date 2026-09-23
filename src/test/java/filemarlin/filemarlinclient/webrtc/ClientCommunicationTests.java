package filemarlin.filemarlinclient.webrtc;

import dev.onvoid.webrtc.RTCDataChannelBuffer;
import filemarlin.filemarlinclient.filetransfer.FileTransferMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClientCommunicationTests {

    public FakeSignaller signallerA;
    public FakeSignaller signallerB;

    public WebRTCClient clientA;
    public WebRTCClient clientB;

    public CustomPeerConnection connectionA;
    public CustomPeerConnection connectionB;

    @BeforeEach
    public void setUp() {
        signallerA = new FakeSignaller("A");
        signallerB = new FakeSignaller("B");
        signallerA.others.put("B", signallerB);
        signallerB.others.put("A", signallerA);

        clientA = new WebRTCClient(signallerA);
        clientB = new WebRTCClient(signallerB);

        clientA.establishConnection("B", true);
        clientB.establishConnection("A", false);

        connectionA = clientA.getConnection("B");
        connectionB = clientB.getConnection("A");

        connectionA.getConnectionEstablished().join();
        connectionB.getConnectionEstablished().join();
    }

    @Test
    public void SendFileTransferMessageTest() throws Exception {
        var expected = "hello";
        var message = new FileTransferMessage(expected.getBytes());

        connectionA.getDataChannel().send(new RTCDataChannelBuffer(message.encode(), true));
        Thread.sleep(1000);

        var actual = connectionB.getLastMessage();
        assertEquals(expected, actual);
    }


}
