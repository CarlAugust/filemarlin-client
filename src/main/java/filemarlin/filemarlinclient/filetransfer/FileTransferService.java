package filemarlin.filemarlinclient.filetransfer;


import java.nio.ByteBuffer;
import java.util.Optional;

/*
    The porpuse of this class is to expose an API any transport protocol can use to do file transfer I suppose.

*/
public class FileTransferService {

    // Current file being sent or received
    // State?
    // Encryption key
    // File permissions

    /*
        May change state ect
    */
    public void receive() {

    }

    /*
        Gives you the next payload to be sent
    */
    public Optional<ByteBuffer> nextPayload() {

       return null;
    }

    public void confirmPayload() {

    }


    // Get information ect...
}
