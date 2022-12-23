package com.example.serverclient.client;

import com.example.serverclient.models.Driver;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.util.ArrayList;

public class TakeVehicleController {
    @FXML
    private ComboBox<Driver> driverComboBox;
    @FXML
    private DatePicker datePicker;

    int idVehicle;

    @FXML
    protected void onTakeButtonClick() {
        if(driverComboBox.getSelectionModel().isEmpty() || datePicker.getValue()==null)
            return;
        ClientApplication.connection.takeVehicleRequest(idVehicle, driverComboBox.getValue().getIdDriver(), datePicker.getValue());
    }
    @FXML
    protected void onBackButtonClick() {
        ClientApplication.app.switchScene("login.fxml");
    }

    public void initialize(){
        Platform.runLater(() -> {
            Object[] content = (Object[]) ClientApplication.stage.getUserData();
            idVehicle = (int) content[0];
            ArrayList<Driver> drivers = (ArrayList<Driver>) content[1];
            driverComboBox.setItems(FXCollections.observableList(drivers));
            driverComboBox.setConverter(new CategoryChooserConverter<>());
        });
    }

    public static class CategoryChooserConverter<T> extends StringConverter<Driver> {
        @Override
        public String toString(Driver driver) {
            if(driver == null) {
                return null;
            }
            return driver.getName();
        }

        @Override
        public Driver fromString(String s) {
            return null;
        }
    }
}
