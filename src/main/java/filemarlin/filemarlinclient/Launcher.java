package filemarlin.filemarlinclient;

import filemarlin.filemarlinclient.websocket.WebSocketManager;
import javafx.application.Application;

import java.io.IOException;
import java.util.Arrays;

public class Launcher {
    public static void main(String[] args) {

        // Setup http client and websocket stuff
        WebSocketManager wsManager;
        try {
            wsManager = WebSocketManager.getInstance();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(Arrays.toString(wsManager.getMessenger().getClients().join()));

        // Setup WebRTC stuff


        Application.launch(HelloApplication.class, args);

        wsManager.close();

    }
}
