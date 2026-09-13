package filemarlin.filemarlinclient;

import filemarlin.filemarlinclient.websocket.Signaller;
import filemarlin.filemarlinclient.websocket.WebSocketManager;
import javafx.application.Application;

import java.io.IOException;
import java.util.Arrays;

public class Launcher {
    public static void main(String[] args) {

        // Setup http client and websocket stuff
        var wsManager = WebSocketManager.getInstance();
        System.out.println(Arrays.toString(Signaller.messenger.getClients().join()));

        // Setup WebRTC stuff


        Application.launch(HelloApplication.class, args);


        // Cleanup for graceful disconnect
        wsManager.close();

    }
}
