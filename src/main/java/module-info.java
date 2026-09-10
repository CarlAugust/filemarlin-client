module filemarlin.filemarlinclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens filemarlin.filemarlinclient to javafx.fxml;
    exports filemarlin.filemarlinclient;
}