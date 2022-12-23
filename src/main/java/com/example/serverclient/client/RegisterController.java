package com.example.serverclient.client;

import com.example.serverclient.models.Brigadier;
import com.example.serverclient.models.VehicleType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.util.ArrayList;

public class RegisterController {
    @FXML
    private TextField loginField;
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<Brigadier> brigadierComboBox;
    @FXML
    private ComboBox<String> vehicleTypeComboBox;

    @FXML
    protected void onRegisterButtonClick() {
        if(loginField.getText().isEmpty() || passwordField.getText().isEmpty() || vehicleTypeComboBox.getSelectionModel().isEmpty()  || brigadierComboBox.getSelectionModel().isEmpty() || datePicker.getValue()==null || nameField.getText().isEmpty())
            return;
        VehicleType type = VehicleType.Digging;
        switch(vehicleTypeComboBox.getValue()) {
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
        ClientApplication.connection.registerDriverRequest(loginField.getText(), passwordField.getText(), nameField.getText(), datePicker.getValue(), brigadierComboBox.getValue().getIdBrigadier(), type);
    }
    @FXML
    protected void onBackButtonClick() {
        ClientApplication.app.switchScene("login.fxml");
    }

    public void initialize(){
        Platform.runLater(() -> {
            ArrayList<Brigadier> brigadiers = (ArrayList<Brigadier>) ClientApplication.stage.getUserData();
            brigadierComboBox.setItems(FXCollections.observableList(brigadiers));
            brigadierComboBox.setConverter(new RegisterDriverController.CategoryChooserConverter<>());
            vehicleTypeComboBox.getItems().addAll("Землеройная техника", "Кран", "Транспорт");
        });
    }

    public static class CategoryChooserConverter<T> extends StringConverter<Brigadier> {
        @Override
        public String toString(Brigadier brigadier) {
            if(brigadier == null) {
                return null;
            }
            return brigadier.getName();
        }

        @Override
        public Brigadier fromString(String s) {
            return null;
        }
    }
}