module filemarlin.filemarlinclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires tools.jackson.databind;
    requires webrtc.java;


    opens filemarlin.filemarlinclient to javafx.fxml;
    exports filemarlin.filemarlinclient.websocket.records;
    exports filemarlin.filemarlinclient;
}