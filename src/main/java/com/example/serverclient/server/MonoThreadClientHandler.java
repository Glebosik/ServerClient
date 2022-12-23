package com.example.serverclient.server;

import com.example.serverclient.models.*;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.serverclient.server.MultiThreadServer.db;

public class MonoThreadClientHandler implements Runnable {

    private static Socket clientDialog;
    private static int count = 0;
    public int id;
    private Logger log;
    public MonoThreadClientHandler(Socket client, Logger log) {
        this.log = log;
        clientDialog = client;
        this.id = count++;
    }

    @Override
    public void run() {

        try {
            DataOutputStream o = new DataOutputStream(clientDialog.getOutputStream());
            ObjectOutputStream out = new ObjectOutputStream(o);
            DataInputStream i = new DataInputStream(clientDialog.getInputStream());
            ObjectInputStream in = new ObjectInputStream(i);
            log.log(Level.INFO, "Client <" + this.id + "> has connected.");
            while (!clientDialog.isClosed()) {
                Message input = (Message) in.readObject();
                Message output;
                switch(input.type) {
                    case loginRequest -> {
                        String[] content = (String[]) input.message;
                        log.log(Level.INFO, "Client <" + this.id + "> sent loginRequest with login '"+content[0]+"' and password '"+content[1]+"'");
                        int userId = db.checkCredentials(content[0], content[1]);
                        if(userId != 0) {
                            Object [] contentOut = new Object[3];
                            contentOut[0] = userId;
                            int specificId = 0;
                            if((specificId = db.getDriverId(userId))!= 0){
                                contentOut[1] = UserType.Driver.toString();
                            }
                            else if((specificId = db.getManagerId(userId)) !=0){
                                contentOut[1] = UserType.Manager.toString();
                            }
                            else if((specificId = db.getBrigadierId(userId)) != 0){
                                contentOut[1] = UserType.Brigadier.toString();
                            }
                            contentOut[2] = specificId;
                            output = new Message(Requests.loginRequest, true, contentOut);
                            log.log(Level.INFO, "Server replied to Client <" + this.id + "> loginRequest with status=True userId="+contentOut[0]+" userType="+contentOut[1]+" specificId="+contentOut[2]);
                        } else {
                            output = new Message(Requests.loginRequest, false, null);
                            log.log(Level.INFO, "Server replied to Client <" + this.id + "> loginRequest with status=False");
                        }
                        out.writeObject(output);
                        out.flush();
                    }
                    case registerRequest -> {
                        String[] content = (String[]) input.message;
                        UserType type = UserType.valueOf(content[0]);
                        String login = content[1];
                        String password = content[2];
                        String name = content[3];
                        LocalDate birth = LocalDate.parse(content[4]);
                        log.log(Level.INFO, "Client <" + this.id + "> sent registerRequest with userType="+type+" login="+login+" password="+password+" name="+name+" birth="+birth.toString());
                        if(db.isLoginFree(login)){
                            switch (type) {
                                case Brigadier -> {
                                    db.insertBrigadier(name, birth, login, password);
                                }
                                case Manager -> {
                                    db.insertManager(name, birth, login, password);
                                }
                            }
                            output = new Message(Requests.registerRequest, true, null);
                            log.log(Level.INFO, "Server replied to Client <" + this.id + "> registerRequest with status=True");
                        } else {
                            output = new Message(Requests.registerRequest, false, null);
                            log.log(Level.INFO, "Server replied to Client <" + this.id + "> registerRequest with status=False");
                        }
                        out.writeObject(output);
                        out.flush();
                    }
                    case getAllDriversForBrigadier -> {
                        int idBrigadier = (int) input.message;
                        log.log(Level.INFO, "Client <" + this.id + "> sent getAllDriversForBrigadier with idBrigadier="+idBrigadier);
                        ArrayList<Driver> drivers = db.getAllDriversForBrigadier(idBrigadier);
                        log.log(Level.INFO, "Server replied to Client <" + this.id + "> getAllDriversForBrigadier with statue=true and list of drivers");
                        output = new Message(Requests.getAllDriversForBrigadier, true, drivers);
                        out.writeObject(output);
                        out.flush();
                    }
                    case registerDriverRequest -> {
                        String[] content = (String[]) input.message;
                        UserType type = UserType.valueOf(content[0]);
                        String login = content[1];
                        String password = content[2];
                        String name = content[3];
                        LocalDate birth = LocalDate.parse(content[4]);
                        int idBrigadier = Integer.valueOf((String) content[5]);
                        VehicleType vehicleType = VehicleType.valueOf((String) content[6]);
                        log.log(Level.INFO, "Client <" + this.id + "> sent registerRequest with userType="+type+" login="+login+" password="+password+" name="+name+" birth="+birth.toString()+" idBrigadier="+idBrigadier);
                        if(db.isLoginFree(login)){
                            switch (type) {
                                case Driver -> {
                                    db.insertDriver(name, birth, login, password, idBrigadier, vehicleType);
                                }
                            }
                            output = new Message(Requests.registerDriverRequest, true, null);
                            log.log(Level.INFO, "Server replied to Client <" + this.id + "> registerDriverRequest with status=True");
                        } else {
                            output = new Message(Requests.registerDriverRequest, false, null);
                            log.log(Level.INFO, "Server replied to Client <" + this.id + "> registerDriverRequest with status=False");
                        }
                        out.writeObject(output);
                        out.flush();
                    }
                    case getDriverDataRequest -> {
                        int idDriver = (Integer) input.message;
                        log.log(Level.INFO, "Client <" + this.id + "> sent getDriverDataRequest with idDriver="+idDriver);
                        ArrayList<Vehicle> vehicles = db.getDriverData(idDriver);
                        log.log(Level.INFO, "Server replied to Client <" + this.id + "> getDriverDataRequest with status=true and list of vehicles");
                        output = new Message(Requests.getDriverDataRequest, true, vehicles);
                        out.writeObject(output);
                        out.flush();
                    }
                    case getManagerDataRequest -> {
                        int idManager = (Integer) input.message;
                        log.log(Level.INFO, "Client <" + this.id + "> sent getManagerDataRequest with idManager="+idManager);
                        Object[] content = new Object[4];
                        content[0] = db.getAllManagers();
                        content[1] = db.getAllBrigadiers();
                        content[2] = db.getAllDrivers();
                        content[3] = db.getAllVehicles();
                        log.log(Level.INFO, "Server replied to Client <" + this.id + "> sent getManagerDataRequest with status=true and data for manager");
                        output = new Message(Requests.getManagerDataRequest, true, content);
                        out.writeObject(output);
                        out.flush();
                    }
                    case getBrigadierDataRequest -> {
                        int idBrigadier = (Integer) input.message;
                        log.log(Level.INFO, "Client <" + this.id + "> sent getBrigadierDataRequest with idBrigadier="+idBrigadier);
                        ArrayList<Vehicle> vehicles = db.getBrigadierVehiclesData(idBrigadier);
                        ArrayList<DriverForBrigadier> driversForBrigadier = db.getBrigadierDriversData(idBrigadier);
                        Object[] content = new Object[2];
                        content[0] = vehicles;
                        content[1] = driversForBrigadier;
                        log.log(Level.INFO, "Server replied to Client <" + this.id + "> getDriverDataRequest with status=true and lists of vehicles and drivers");
                        output = new Message(Requests.getBrigadierDataRequest, true, content);
                        out.writeObject(output);
                        out.flush();
                    }
                    case getAllBrigadiers -> {
                        log.log(Level.INFO, "Client <" + this.id + "> sent getAllBrigadiersRequest");
                        ArrayList<Brigadier> brigadiers = db.getAllBrigadiers();
                        output = new Message(Requests.getAllBrigadiers, true, brigadiers);
                        log.log(Level.INFO, "Server replied to Client <" + this.id + "> getAllBrigadiersRequest with status=true and lists of brigadiers");
                        out.writeObject(output);
                        out.flush();
                    }
                    case getAllBrigadiersForRegister -> {
                        log.log(Level.INFO, "Client <" + this.id + "> sent getAllBrigadiersForRegisterRequest");
                        ArrayList<Brigadier> brigadiers = db.getAllBrigadiers();
                        output = new Message(Requests.getAllBrigadiersForRegister, true, brigadiers);
                        log.log(Level.INFO, "Server replied to Client <" + this.id + "> getAllBrigadiersForRegisterRequest with status=true and lists of brigadiers");
                        out.writeObject(output);
                        out.flush();
                    }
                    case addVehicleRequest -> {
                        log.log(Level.INFO, "Client <" + this.id + "> sent addVehicleRequest");
                        VehicleType type = (VehicleType) input.message;
                        db.insertVehicle(type);
                        output = new Message(Requests.addVehicleRequest, true, null);
                        log.log(Level.INFO, "Server replied to Client <" + this.id + "> addVehicleRequest with status=true");
                        out.writeObject(output);
                        out.flush();
                    }
                    case takeVehicleRequest -> {
                        Object[] content = (Object[]) input.message;
                        int idVehicle = (int) content[0];
                        int idDriver = (int) content[1];
                        LocalDate date = LocalDate.parse((String) content[2]);
                        log.log(Level.INFO, "Client <" + this.id + "> sent takeVehicleRequest with idVehicle="+idVehicle+" idDriver="+idDriver+" date="+date.toString());
                        db.takeVehicle(idVehicle, idDriver, date);
                        output = new Message(Requests.takeVehicleRequest, true, null);
                        log.log(Level.INFO, "Server replied to Client <" + this.id + "> takeVehicleRequest with status=true");
                        out.writeObject(output);
                        out.flush();
                    }
                    case freeVehicleRequest -> {
                        int idVehicle = (int) input.message;
                        log.log(Level.INFO, "Client <" + this.id + "> sent freeVehicleRequest with idVehicle="+idVehicle);
                        db.freeVehicle(idVehicle);
                        output = new Message(Requests.freeVehicleRequest, true, null);
                        log.log(Level.INFO, "Server replied to Client <" + this.id + "> freeVehicleRequest with status=true");
                        out.writeObject(output);
                        out.flush();
                    }
                    case disconnect -> {
                        log.log(Level.INFO, "Client <" + this.id + "> has disconnected.");
                        System.out.println("Client disconnected");
                        System.out.println("Closing connections & channels.");
                        in.close();
                        out.close();
                        clientDialog.close();
                        System.out.println("Closing connections & channels - DONE.");
                    }
                }
            }
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}