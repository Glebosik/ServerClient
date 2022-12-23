package com.example.serverclient.client;

import com.example.serverclient.models.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

public class ServerConnection implements Runnable{
    static Socket socket;
    static ObjectOutputStream out;
    static ObjectInputStream in;

    ServerConnection(){
        try {
            socket = new Socket("localhost", 5548);
            DataOutputStream o = new DataOutputStream(socket.getOutputStream());
            out = new ObjectOutputStream(o);
            DataInputStream i = new DataInputStream(socket.getInputStream());
            in = new ObjectInputStream(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loginRequest(String login, String password) {
        try {
            String[] content = new String[2];
            content[0] = login;
            content[1] = password;
            Message message = new Message(Requests.loginRequest, true, content);
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void registerRequest(UserType type, String login, String password, String name, LocalDate birth) {
        try {
            String[] content = new String[5];
            content[0] = type.toString();
            content[1] = login;
            content[2] = password;
            content[3] = name;
            content[4] = birth.toString();
            Message message = new Message(Requests.registerRequest, true, content);
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void registerDriverRequest(String login, String password, String name, LocalDate birth, int idBrigadier, VehicleType vehicleType){
        try {
            String[] content = new String[7];
            content[0] = UserType.Driver.toString();
            content[1] = login;
            content[2] = password;
            content[3] = name;
            content[4] = birth.toString();
            content[5] = Integer.toString(idBrigadier);
            content[6] = vehicleType.toString();
            Message message = new Message(Requests.registerDriverRequest, true, content);
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void getDriverDataRequest(int idDriver){
        try {
            Message message = new Message(Requests.getDriverDataRequest, true, idDriver);
            out.writeObject(message);
            out.flush();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public void getBrigadierDataRequest(int idBrigadier){
        try {
            Message message = new Message(Requests.getBrigadierDataRequest, true, idBrigadier);
            out.writeObject(message);
            out.flush();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public void getManagerDataRequest(int idManager){
        try {
            Message message = new Message(Requests.getManagerDataRequest, true, idManager);
            out.writeObject(message);
            out.flush();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public void takeVehicleRequest(int idVehicle, int idDriver, LocalDate date){
        try {
            Object [] content = new Object[3];
            content[0] = idVehicle;
            content[1] = idDriver;
            content[2] = date.toString();
            Message message = new Message(Requests.takeVehicleRequest, true, content);
            out.writeObject(message);
            out.flush();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public void freeVehicleRequest(int idVehicle){
        try {
            Message message = new Message(Requests.freeVehicleRequest, true, idVehicle);
            out.writeObject(message);
            out.flush();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public void addVehicleRequest(VehicleType type){
        try {
        Message message = new Message(Requests.addVehicleRequest, true, type);
        out.writeObject(message);
        out.flush();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public void getAllBrigadiers(){
        try {
            Message message = new Message(Requests.getAllBrigadiers, true, null);
            out.writeObject(message);
            out.flush();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public void getAllBrigadiersForRegister(){
        try {
            Message message = new Message(Requests.getAllBrigadiersForRegister, true, null);
            out.writeObject(message);
            out.flush();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public void diconnect(){
        try {
            Message message = new Message(Requests.disconnect, true, null);
            out.writeObject(message);
            out.flush();
            out.close();
            in.close();
            socket.close();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public void getAllDriversForBrigadier(int brigadierId) {
        try {
            Message message = new Message(Requests.getAllDriversForBrigadier, true, brigadierId);
            out.writeObject(message);
            out.flush();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    @Override
    public void run() {
        try {
            while(!socket.isClosed()) {
                Message entry = null;
                entry = (Message) in.readObject();
                Message finalEntry = entry;
                if(finalEntry != null) {
                    Platform.runLater(() -> {
                        switch (finalEntry.type) {
                            case loginRequest -> {
                                if (finalEntry.status) {
                                    Object[] content = (Object[]) finalEntry.message;
                                    int userId = (Integer) content[0];
                                    UserType userType = UserType.valueOf((String) content[1]);
                                    int specificId = (Integer) content[2];
                                    ClientApplication.session.setUserId(userId);
                                    ClientApplication.session.setUserType(userType);
                                    ClientApplication.session.setSpecificId(specificId);
                                    switch (userType) {
                                        case Driver -> {
                                            getDriverDataRequest(specificId);
                                        }
                                        case Brigadier -> {
                                            getBrigadierDataRequest(specificId);
                                        }
                                        case Manager -> {
                                            getManagerDataRequest(specificId);
                                        }
                                    }
                                }
                                else {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Ошибка авторизации");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Пользователь с таким набором данных не найден");
                                    alert.showAndWait();
                                }
                            }
                            case registerRequest, registerDriverRequest -> {
                                if(finalEntry.status) {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Пользователь зарегистрирован");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Пользователь был успешно зарегистрирован.");
                                    alert.showAndWait();
                                } else {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Ошибка регистрации");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Данный логин занят");
                                    alert.showAndWait();
                                }
                            }
                            case getDriverDataRequest -> {
                                if (finalEntry.status) {
                                    ArrayList<Vehicle> vehicles = (ArrayList<Vehicle>) finalEntry.message;
                                    if(vehicles != null)
                                        ClientApplication.stage.setUserData(vehicles);
                                    ClientApplication.app.switchScene("driverView.fxml");
                                }
                            }
                            case getManagerDataRequest -> {
                                if (finalEntry.status) {
                                    Object[] content = (Object[]) finalEntry.message;
                                    if(content != null)
                                        ClientApplication.stage.setUserData(content);
                                    ClientApplication.app.switchScene("managerView.fxml");
                                }
                            }
                            case getBrigadierDataRequest -> {
                                if (finalEntry.status) {
                                    Object[] content = (Object[]) finalEntry.message;
                                    if(content != null)
                                        ClientApplication.stage.setUserData(content);
                                    ClientApplication.app.switchScene("brigadierView.fxml");
                                }
                            }
                            case getAllBrigadiers -> {
                                if(finalEntry.status){
                                    ArrayList<Brigadier> content = (ArrayList<Brigadier>) finalEntry.message;
                                    if(content != null){
                                        ClientApplication.stage.setUserData(content);
                                        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("registerDriver.fxml"));
                                        Scene scene = null;
                                        Stage stage = new Stage();
                                        try {
                                            scene = new Scene(fxmlLoader.load(), 400, 400);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                        stage.setTitle("Регистрация водителя");
                                        stage.setScene(scene);
                                        stage.show();
                                    } else {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Ошибка регистрации");
                                        alert.setHeaderText(null);
                                        alert.setContentText("На предприятии нет бригадиров");
                                        alert.showAndWait();
                                    }
                                }
                            }
                            case getAllDriversForBrigadier -> {
                                if(finalEntry.status){
                                    ArrayList<Driver> content = (ArrayList<Driver>) finalEntry.message;
                                    if(content != null){
                                        Object[] data = new Object[2];
                                        int idVehicle = (int) ClientApplication.stage.getUserData();
                                        data[0] = idVehicle;
                                        data[1] = content;
                                        ClientApplication.stage.setUserData(data);
                                        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("takeVehicle.fxml"));
                                        Scene scene = null;
                                        Stage stage = new Stage();
                                        try {
                                            scene = new Scene(fxmlLoader.load(), 320, 240);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                        stage.setTitle("Выбор водителя");
                                        stage.setScene(scene);
                                        stage.show();
                                    } else {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Ошибка регистрации");
                                        alert.setHeaderText(null);
                                        alert.setContentText("На предприятии нет бригадиров");
                                        alert.showAndWait();
                                    }
                                }
                            }
                            case getAllBrigadiersForRegister -> {
                                if(finalEntry.status){
                                    ArrayList<Brigadier> content = (ArrayList<Brigadier>) finalEntry.message;
                                    if(content != null){
                                        ClientApplication.stage.setUserData(content);
                                        ClientApplication.app.switchScene("register.fxml");
                                    } else {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Ошибка регистрации");
                                        alert.setHeaderText(null);
                                        alert.setContentText("На предприятии нет бригадиров");
                                        alert.showAndWait();
                                    }
                                }
                            }
                            case takeVehicleRequest -> {
                                getBrigadierDataRequest(ClientApplication.session.getSpecificId());
                            }
                            case freeVehicleRequest -> {
                                getBrigadierDataRequest(ClientApplication.session.getSpecificId());
                            }
                            case addVehicleRequest -> {
                                if(finalEntry.status) {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Техника добавлена");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Техника была успешно добавлена.");
                                    alert.showAndWait();
                                }
                            }
                        }
                    });
                }
            }
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
        }
    }
}
