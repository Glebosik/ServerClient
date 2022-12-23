module com.example.serverclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports com.example.serverclient.server;
    opens com.example.serverclient.server to javafx.fxml;
    exports com.example.serverclient.server.exceptions;
    opens com.example.serverclient.server.exceptions to javafx.fxml;
    exports com.example.serverclient.models;
    opens com.example.serverclient.models to javafx.fxml;
    exports com.example.serverclient.client;
    opens com.example.serverclient.client to javafx.fxml;
}