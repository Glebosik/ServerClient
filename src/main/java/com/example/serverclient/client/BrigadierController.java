package com.example.serverclient.client;

import com.example.serverclient.models.*;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.util.ArrayList;

public class BrigadierController {

    @FXML
    TableView firstTable;
    @FXML
    TableView secondTable;

    @FXML
    protected void onBackButtonClick() {
        ClientApplication.app.switchScene("login.fxml");
    }
    @FXML
    protected void onUpdateButtonClick() {
        ClientApplication.connection.getBrigadierDataRequest(ClientApplication.session.getSpecificId());
    }

    public void initialize(){
        Platform.runLater(() -> {
            Object[] arrays = (Object[]) ClientApplication.stage.getUserData();
            ArrayList<Vehicle> vehicles = (ArrayList<Vehicle> ) arrays[0];
            ArrayList<DriverForBrigadier> drivers = (ArrayList<DriverForBrigadier>) arrays[1];
            ArrayList<firstTableData> firstTableArray = new ArrayList<>();
            ArrayList<secondTableData> secondTableArray = new ArrayList<>();
            for(int i=0; i<vehicles.size();++i){
                Vehicle vehicle = vehicles.get(i);
                firstTableArray.add(new firstTableData(vehicle.getIdVehicle(), vehicle.getIdDriver(), vehicle.getType(), vehicle.getDate()));
            }
            for(int i=0; i<drivers.size();++i){
                DriverForBrigadier driver = drivers.get(i);
                secondTableArray.add(new secondTableData(driver.getIdUser(), driver.getIdDriver(), driver.getName(), driver.getBirth(), driver.getIdVehicle(), driver.getDate()));
            }
            ObservableList<TableColumn> list = firstTable.getColumns();
            list.get(0).setCellValueFactory(new PropertyValueFactory<>("idVehicle"));
            list.get(1).setCellValueFactory(new PropertyValueFactory<>("idDriver"));
            list.get(2).setCellValueFactory(new PropertyValueFactory<>("type"));
            list.get(3).setCellValueFactory(new PropertyValueFactory<>("date"));
            list.get(4).setCellValueFactory(new PropertyValueFactory<>("button"));
            TableColumn<firstTableData, firstTableData> firstTableColumn = list.get(4);
            firstTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
            list.get(4).setCellFactory(param->new TableCell<firstTableData, firstTableData>() {
                private final Button takeButton  = new Button("Назначить");
                @Override
                protected void updateItem(firstTableData data, boolean empty){
                    super.updateItem(data, empty);
                    if(data == null){
                        setGraphic(null);
                        return;
                    }
                    setGraphic(takeButton);
                    takeButton.setOnAction(event -> {
                        ClientApplication.stage.setUserData(data.idVehicle.getValue());
                        ClientApplication.connection.getAllDriversForBrigadier(ClientApplication.session.getSpecificId());
                    });
                }
            });
            list = secondTable.getColumns();
            list.get(0).setCellValueFactory(new PropertyValueFactory<>("idDriver"));
            list.get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
            list.get(2).setCellValueFactory(new PropertyValueFactory<>("birth"));
            list.get(3).setCellValueFactory(new PropertyValueFactory<>("idVehicle"));
            list.get(4).setCellValueFactory(new PropertyValueFactory<>("date"));
            list.get(5).setCellValueFactory(new PropertyValueFactory<>("button"));
            TableColumn<secondTableData, secondTableData> secondTableColumn = list.get(5);
            secondTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
            list.get(5).setCellFactory(param->new TableCell<secondTableData, secondTableData>() {
                private final Button removeButton  = new Button("Снять");
                @Override
                protected void updateItem(secondTableData data, boolean empty){
                    super.updateItem(data, empty);
                    if(data == null){
                        setGraphic(null);
                        return;
                    }
                    setGraphic(removeButton);
                    removeButton.setOnAction(event -> {
                        ClientApplication.connection.freeVehicleRequest(data.idVehicle.getValue());
                        secondTableArray.remove(data);
                    });
                }
            });
            firstTable.setItems(FXCollections.observableList(firstTableArray));
            secondTable.setItems(FXCollections.observableList(secondTableArray));
        });
    }

    public class firstTableData {
        private SimpleIntegerProperty idVehicle;
        private SimpleIntegerProperty idDriver;
        private SimpleStringProperty type;
        private SimpleStringProperty date;

        firstTableData(int idVehicle, int idDriver, VehicleType type, Date date) {
            this.idVehicle = new SimpleIntegerProperty(idVehicle);
            this.idDriver = new SimpleIntegerProperty(idDriver);
            switch(type){
                case Crane -> this.type = new SimpleStringProperty("Кран");
                case Digging -> this.type = new SimpleStringProperty("Землеройная машина");
                case Transport -> this.type = new SimpleStringProperty("Транспорт");
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
    public class secondTableData {
        private SimpleIntegerProperty idUser;
        private SimpleIntegerProperty idDriver;
        private SimpleStringProperty name;
        private SimpleStringProperty birth;
        private SimpleIntegerProperty idVehicle;
        private SimpleStringProperty date;
        secondTableData(int idUser, int idDriver, String name, Date birth, int idVehicle, Date date) {
            this.idUser = new SimpleIntegerProperty(idUser);
            this.idDriver = new SimpleIntegerProperty(idDriver);
            this.name = new SimpleStringProperty(name);
            this.birth = new SimpleStringProperty(birth.toString());
            this.idVehicle = new SimpleIntegerProperty(idVehicle);
            if(date == null){
                this.date = new SimpleStringProperty("Техника свободна");
            } else {
                this.date = new SimpleStringProperty(date.toString());
            }
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

        public int getIdDriver() {
            return idDriver.get();
        }

        public SimpleIntegerProperty idDriverProperty() {
            return idDriver;
        }

        public void setIdDriver(int idDriver) {
            this.idDriver.set(idDriver);
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

        public int getIdVehicle() {
            return idVehicle.get();
        }

        public SimpleIntegerProperty idVehicleProperty() {
            return idVehicle;
        }

        public void setIdVehicle(int idVehicle) {
            this.idVehicle.set(idVehicle);
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

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }
    }
}