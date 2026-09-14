package filemarlin.filemarlinclient.websocket;


/*
This honestly feels like a bad thing to do but it makes things a little nicer to write, like an easy interface to use over
all the implementation in WebSocket So whatever.
Long ass name i know whatever it explainatory i guess?
 */
public class GlobalWebSocketSignallerAccessor {
    public static WebSocketSignaller Signal() {
        return WebSocketManager.getInstance().getMessenger();
    }
}
