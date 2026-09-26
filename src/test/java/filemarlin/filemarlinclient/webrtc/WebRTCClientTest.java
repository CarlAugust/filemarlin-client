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


        var connectionA = clientA.getConnection("B");

        assertNotNull(connectionA);
        connectionA.getConnectionEstablished().join();

        var connectionB = clientB.getConnection("A");

        assertNotNull(connectionB);
        connectionB.getConnectionEstablished().join();


        assertNotNull(connectionA.getPeerConnection(), "Connection on A exists");
        assertNotNull(connectionB.getPeerConnection(), "Connections on B exists");

        assertNotNull(connectionA.getDataChannel(), "Datachannel on A exists");
        assertNotNull(connectionB.getDataChannel(), "Datachannel on B exists");


        clientA.close();
        clientB.close();
    }

    @Test
    void multipleClientCanEstablishConnection() throws Exception {
        System.out.println("");

        var signallerA = new FakeSignaller("A");
        var signallerB = new FakeSignaller("B");
        signallerA.others.put("B", signallerB);
        signallerB.others.put("A", signallerA);

        var clientA = new WebRTCClient(signallerA);
        var clientB = new WebRTCClient(signallerB);

        clientA.establishConnection("B", true);

        var connectionA = clientA.getConnection("B");

        assertNotNull(connectionA);
        connectionA.getConnectionEstablished().join();

        var connectionB = clientB.getConnection("A");

        assertNotNull(connectionB);
        connectionB.getConnectionEstablished().join();


        assertNotNull(connectionA.getPeerConnection(), "Connection on A exists");
        assertNotNull(connectionB.getPeerConnection(), "Connections on B exists");

        assertNotNull(connectionA.getDataChannel(), "Datachannel on A exists");
        assertNotNull(connectionB.getDataChannel(), "Datachannel on B exists");

        var signallerC = new FakeSignaller("C");
        signallerA.others.put("C", signallerC);
        signallerC.others.put("A", signallerA);

        var clientC = new WebRTCClient(signallerC);

        clientA.establishConnection("C", true);

        connectionA = clientA.getConnection("C");

        assertNotNull(connectionA);
        connectionA.getConnectionEstablished().join();

        var connectionC = clientC.getConnection("A");
        assertNotNull(connectionC);

        connectionC.getConnectionEstablished().join();


        assertNotNull(connectionC.getPeerConnection(), "Connection on A exists");
        assertNotNull(connectionC.getPeerConnection(), "Connections on C exists");

        assertNotNull(connectionC.getDataChannel(), "Datachannel on A exists");
        assertNotNull(connectionC.getDataChannel(), "Datachannel on C exists");


        clientA.close();
        clientB.close();
        clientC.close();
    }
}