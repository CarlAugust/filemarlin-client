package filemarlin.filemarlinclient.webrtc;

import dev.onvoid.webrtc.RTCDataChannelBuffer;
import filemarlin.filemarlinclient.filetransfer.TestMessage;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientCommunicationTests {

    public FakeSignaller signallerA;
    public FakeSignaller signallerB;

    public WebRTCClient clientA;
    public WebRTCClient clientB;

    public CustomPeerConnection connectionA;
    public CustomPeerConnection connectionB;

    @BeforeAll
    public void setUp() {
        signallerA = new FakeSignaller("A");
        signallerB = new FakeSignaller("B");
        signallerA.others.put("B", signallerB);
        signallerB.others.put("A", signallerA);

        clientA = new WebRTCClient(signallerA);
        clientB = new WebRTCClient(signallerB);

        clientA.establishConnection("B", true);
        connectionA = clientA.getConnection("B");
        connectionA.getConnectionEstablished().join();

        connectionB = clientB.getConnection("A");
        connectionB.getConnectionEstablished().join();
    }

    @AfterAll
    public void cleanUp() {
        clientA.close();
        clientB.close();
    }

    @Test
    public void SendFileTransferMessageTest() throws Exception {
        var expected = "hello";
        var message = new TestMessage(expected.length(), expected.getBytes());

        connectionA.getDataChannel().send(new RTCDataChannelBuffer(message.encode(), true));
        Thread.sleep(1000);

        var actual = connectionB.getLastMessage();
        assertEquals(expected, actual);
    }

}
