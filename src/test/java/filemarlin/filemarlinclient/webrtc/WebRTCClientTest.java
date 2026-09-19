package filemarlin.filemarlinclient.webrtc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WebRTCClientTest {

    @Test
    void twoClientsCanEstablishConnection() throws Exception {

        var signallerA = new FakeSignaller("A");
        var signallerB = new FakeSignaller("B");
        signallerA.others.put("B", signallerB);
        signallerB.others.put("A", signallerA);

        var clientA = new WebRTCClient(signallerA);
        var clientB = new WebRTCClient(signallerB);

        clientA.establishConnection("B", true);
        clientB.establishConnection("A", false);

        var connectionA = clientA.getConnection("B");
        var connectionB = clientB.getConnection("A");

        assertNotNull(connectionA);
        assertNotNull(connectionB);

        connectionA.getConnectionEstablished().join();
        connectionB.getConnectionEstablished().join();


        assertNotNull(connectionA.getPeerConnection(), "Connection on A exists");
        assertNotNull(connectionB.getPeerConnection(), "Connections on B exists");

        assertNotNull(connectionA.getDataChannel(), "Datachannel on A exists");
        assertNotNull(connectionB.getDataChannel(), "Datachannel on B exists");

    }
}