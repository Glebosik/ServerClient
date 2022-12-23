package com.example.serverclient.client;

import com.example.serverclient.models.Vehicle;
import com.example.serverclient.models.VehicleType;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.util.ArrayList;

public class DriverController {

    @FXML
    Label header;
    @FXML
    TableView table;
    @FXML
    Button exit;
    @FXML
    protected void onBackButtonClick() {
        ClientApplication.app.switchScene("login.fxml");
    }

    @FXML
    protected  void onUpdateButtonClicked(){
        ClientApplication.connection.getDriverDataRequest(ClientApplication.session.getSpecificId());
    }

    public void initialize(){
        Platform.runLater(() -> {
            table.setPlaceholder(new Label("У вас нет доступных машин"));
            ArrayList<Vehicle> vehicles = (ArrayList<Vehicle>) ClientApplication.stage.getUserData();
            ArrayList<tableData> data = new ArrayList<>();
            for(int i = 0; i < vehicles.size(); ++i) {
                Vehicle vehicle = vehicles.get(i);
                data.add(new tableData(vehicle.getIdVehicle(), vehicle.getIdVehicle(), vehicle.getType(), vehicle.getDate()));
            }
            ObservableList<TableColumn> list = table.getColumns();
            list.get(0).setCellValueFactory(new PropertyValueFactory<>("idVehicle"));
            list.get(1).setCellValueFactory(new PropertyValueFactory<>("idDriver"));
            list.get(2).setCellValueFactory(new PropertyValueFactory<>("type"));
            list.get(3).setCellValueFactory(new PropertyValueFactory<>("date"));
            table.setItems(FXCollections.observableArrayList(data));
        });
    }

    public class tableData {
        private SimpleIntegerProperty idVehicle;
        private SimpleIntegerProperty idDriver;
        private SimpleStringProperty type;
        private SimpleStringProperty date;

        tableData(int idVehicle, int idDriver, VehicleType type, Date date) {
            this.idVehicle = new SimpleIntegerProperty(idVehicle);
            this.idDriver = new SimpleIntegerProperty(idDriver);
            switch(type){
                case Crane -> this.type = new SimpleStringProperty("Кран");
                case Digging -> this.type = new SimpleStringProperty("Землеройная техника");
                case Transport -> this.type = new SimpleStringProperty("Транспорт");
            }
            this.date = new SimpleStringProperty(date.toString());
        }

        public int getIdVehicle() {
            return idVehicle.get();
        }

        public SimpleIntegerProperty idVehicleProperty() {
            return idVehicle;
        }

        public void setIdVehicle(int idVehicle) {
            this.idVehicle.set(idVehicle);
        }

        public int getIdDriver() {
            return idDriver.get();
        }

        public SimpleIntegerProperty idDriverProperty() {
            return idDriver;
        }

        public void setIdDriver(int idDriver) {
            this.idDriver.set(idDriver);
        }

        public String getType() {
            return type.get();
        }

        public SimpleStringProperty typeProperty() {
            return type;
        }

        public void setType(String type) {
            this.type.set(type);
        }

        public String getDate() {
            return date.get();
        }

        public SimpleStringProperty dateProperty() {
            return date;
        }

        public void setDate(String date) {
            this.date.set(date);
        }
    }
}