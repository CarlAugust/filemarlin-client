module filemarlin.filemarlinclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires tools.jackson.databind;
    requires webrtc.java;
    requires jdk.unsupported;

    requires org.junit.jupiter.api;

    opens filemarlin.filemarlinclient to javafx.fxml, org.junit.platform.commons;
    opens filemarlin.filemarlinclient.webrtc to org.junit.platform.commons;
    opens filemarlin.filemarlinclient.websocket to org.junit.platform.commons;

    exports filemarlin.filemarlinclient.websocket.records;
    exports filemarlin.filemarlinclient;
    exports filemarlin.filemarlinclient.webrtc;
    exports filemarlin.filemarlinclient.websocket;
    exports filemarlin.filemarlinclient.webrtc.dto;

}