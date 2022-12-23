package com.example.serverclient.client;

import com.example.serverclient.models.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

public class ManagerController {

    @FXML
    TableView managerTable;
    @FXML
    TableView brigadierTable;
    @FXML
    TableView driverTable;
    @FXML
    TableView vehicleTable;

    @FXML
    protected void onBackButtonClick() {
        ClientApplication.app.switchScene("login.fxml");
    }

    @FXML
    protected void onManagerAddClick(){
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("registerManager.fxml"));
        Scene scene = null;
        Stage stage = new Stage();
        try {
            scene = new Scene(fxmlLoader.load(), 400, 400);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Регистрация управляющего");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void onBrigadierAddClick(){
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("registerBrigadier.fxml"));
        Scene scene = null;
        Stage stage = new Stage();
        try {
            scene = new Scene(fxmlLoader.load(), 400, 400);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Регистрация бригадира");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void onDriverAddClick(){
        ClientApplication.connection.getAllBrigadiers();
    }
    @FXML
    protected void onVehicleAddClick(){
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("registerVehicle.fxml"));
        Scene scene = null;
        Stage stage = new Stage();
        try {
            scene = new Scene(fxmlLoader.load(), 200, 200);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Добавление машины");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected  void onUpdateButtonClicked(){
        ClientApplication.connection.getManagerDataRequest(ClientApplication.session.getSpecificId());
    }

    public void initialize(){
        Platform.runLater(() -> {
            Object[] content = (Object[]) ClientApplication.stage.getUserData();
            ArrayList<Manager> managers = (ArrayList<Manager>) content[0];
            ArrayList<Brigadier> brigadiers = (ArrayList<Brigadier>) content[1];
            ArrayList<Driver> drivers = (ArrayList<Driver>) content[2];
            ArrayList<Vehicle> vehicles = (ArrayList<Vehicle>) content[3];
            ArrayList<tableUserData> managerData = new ArrayList<>();
            ArrayList<tableUserData> brigadierData = new ArrayList<>();
            ArrayList<tableUserData> driverData = new ArrayList<>();
            ArrayList<tableVehicleData> vehicleData = new ArrayList<>();
            for(int i=0; i<managers.size();++i){
                managerData.add(new tableUserData(managers.get(i).getIdUser(), managers.get(i).getIdManager(), managers.get(i).getName(), managers.get(i).getBirth()));
            }
            for(int i=0; i<brigadiers.size();++i){
                brigadierData.add(new tableUserData(brigadiers.get(i).getIdUser(), brigadiers.get(i).getIdBrigadier(), brigadiers.get(i).getName(), brigadiers.get(i).getBirth()));
            }
            for(int i=0; i<drivers.size();++i){
                driverData.add(new tableUserData(drivers.get(i).getIdUser(), drivers.get(i).getIdDriver(), drivers.get(i).getName(), drivers.get(i).getBirth()));
            }
            for(int i=0; i<vehicles.size();++i){
                vehicleData.add(new tableVehicleData(vehicles.get(i).getIdVehicle(), vehicles.get(i).getIdDriver(), vehicles.get(i).getType(), vehicles.get(i).getDate()));
            }
            ObservableList<TableColumn> list = managerTable.getColumns();
            list.get(0).setCellValueFactory(new PropertyValueFactory<>("idUser"));
            list.get(1).setCellValueFactory(new PropertyValueFactory<>("idSpecific"));
            list.get(2).setCellValueFactory(new PropertyValueFactory<>("name"));
            list.get(3).setCellValueFactory(new PropertyValueFactory<>("birth"));
            list = brigadierTable.getColumns();
            list.get(0).setCellValueFactory(new PropertyValueFactory<>("idUser"));
            list.get(1).setCellValueFactory(new PropertyValueFactory<>("idSpecific"));
            list.get(2).setCellValueFactory(new PropertyValueFactory<>("name"));
            list.get(3).setCellValueFactory(new PropertyValueFactory<>("birth"));
            list = driverTable.getColumns();
            list.get(0).setCellValueFactory(new PropertyValueFactory<>("idUser"));
            list.get(1).setCellValueFactory(new PropertyValueFactory<>("idSpecific"));
            list.get(2).setCellValueFactory(new PropertyValueFactory<>("name"));
            list.get(3).setCellValueFactory(new PropertyValueFactory<>("birth"));
            list = vehicleTable.getColumns();
            list.get(0).setCellValueFactory(new PropertyValueFactory<>("idVehicle"));
            list.get(1).setCellValueFactory(new PropertyValueFactory<>("idDriver"));
            list.get(2).setCellValueFactory(new PropertyValueFactory<>("type"));
            list.get(3).setCellValueFactory(new PropertyValueFactory<>("date"));
            managerTable.setItems(FXCollections.observableArrayList(managerData));
            brigadierTable.setItems(FXCollections.observableArrayList(brigadierData));
            driverTable.setItems(FXCollections.observableArrayList(driverData));
            vehicleTable.setItems(FXCollections.observableList(vehicleData));

        });
    }

    public class tableUserData {
        SimpleIntegerProperty idUser;
        SimpleIntegerProperty idSpecific;
        SimpleStringProperty name;
        SimpleStringProperty birth;

        tableUserData(int idUser, int idDoctor, String name, Date birth) {
            this.idUser = new SimpleIntegerProperty(idUser);
            this.idSpecific = new SimpleIntegerProperty(idDoctor);
            this.name = new SimpleStringProperty(name);
            this.birth = new SimpleStringProperty(birth.toString());
        }

        public int getIdUser() {
            return idUser.get();
        }

        public SimpleIntegerProperty idUserProperty() {
            return idUser;
        }

        public void setIdUser(int idUser) {
            this.idUser.set(idUser);
        }

        public int getIdSpecific() {
            return idSpecific.get();
        }

        public SimpleIntegerProperty idSpecificProperty() {
            return idSpecific;
        }

        public void setIdSpecific(int idSpecific) {
            this.idSpecific.set(idSpecific);
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getBirth() {
            return birth.get();
        }

        public SimpleStringProperty birthProperty() {
            return birth;
        }

        public void setBirth(String birth) {
            this.birth.set(birth);
        }
    }
    public class tableVehicleData {
        SimpleIntegerProperty idVehicle;
        SimpleIntegerProperty idDriver;
        SimpleStringProperty type;
        SimpleStringProperty date;

        tableVehicleData(int idVehicle, int idDriver, VehicleType type, Date date) {
            this.idVehicle = new SimpleIntegerProperty(idVehicle);
            this.idDriver = new SimpleIntegerProperty(idDriver);
            switch (type){
                case Transport -> this.type = new SimpleStringProperty("Транспорт");
                case Digging -> this.type = new SimpleStringProperty("Землеройная техника");
                case Crane -> this.type = new SimpleStringProperty("Кран");
            }
            if(date == null){
                this.date = new SimpleStringProperty("Техника свободна");
            } else {
                this.date = new SimpleStringProperty(date.toString());
            }
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