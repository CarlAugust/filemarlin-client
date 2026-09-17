package filemarlin.filemarlinclient;

import filemarlin.filemarlinclient.websocket.WebSocketClient;
import javafx.application.Application;

import java.io.IOException;
import java.util.Arrays;


public class Launcher {
    public static void main(String[] args) {

        // Setup http client and websocket stuff
        WebSocketClient wsClient = null;
        try {
            wsClient = new WebSocketClient();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(Arrays.toString(wsClient.getSignaller().getClients().join()));

        // Setup WebRTC stuff


        Application.launch(HelloApplication.class, args);


        // Cleanup for graceful disconnect
        wsClient.close();

    }
}
