package filemarlin.filemarlinclient;

import filemarlin.filemarlinclient.websocket.WebSocketManager;
import javafx.application.Application;

import java.io.IOException;

public class Launcher {
    public static void main(String[] args) {

        // Setup http client and websocket stuff
        WebSocketManager wsManager;
        try {
            wsManager = new WebSocketManager();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Setup WebRTC stuff


        Application.launch(HelloApplication.class, args);

        wsManager.close();

    }
}
