package filemarlin.filemarlinclient;

import filemarlin.filemarlinclient.websocket.WebSocketClient;
import javafx.application.Application;

import java.util.Arrays;

import static filemarlin.filemarlinclient.websocket.GlobalWebSocketSignallerAccessor.Signal;

public class Launcher {
    public static void main(String[] args) {

        // Setup http client and websocket stuff
        var wsManager = WebSocketClient.getInstance();
        System.out.println(Arrays.toString(Signal().getClients().join()));

        // Setup WebRTC stuff


        Application.launch(HelloApplication.class, args);


        // Cleanup for graceful disconnect
        wsManager.close();

    }
}
