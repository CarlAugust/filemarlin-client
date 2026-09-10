module filemarlin.filemarlinclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;


    opens filemarlin.filemarlinclient to javafx.fxml;
    exports filemarlin.filemarlinclient;
}