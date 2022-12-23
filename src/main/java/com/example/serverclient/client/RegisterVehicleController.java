package com.example.serverclient.client;

import com.example.serverclient.models.VehicleType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegisterVehicleController {
    @FXML
    protected ComboBox<String> typeComboBox;
    @FXML
    protected void onRegisterButtonClick() {
        if(typeComboBox.getSelectionModel().isEmpty())
            return;
        VehicleType type = VehicleType.Digging;
        switch(typeComboBox.getValue()) {
            case "Землеройная техника" -> {
                type = VehicleType.Digging;
            }
            case "Кран" -> {
                type = VehicleType.Crane;
            }
            case "Транспорт" -> {
                type = VehicleType.Transport;
            }
        }
        ClientApplication.connection.addVehicleRequest(type);
    }
    @FXML
    protected void onBackButtonClick() {
        ClientApplication.app.switchScene("login.fxml");
    }
    public void initialize(){
        Platform.runLater(() -> {
            typeComboBox.getItems().addAll("Землеройная техника", "Кран", "Транспорт");
        });
    }
}