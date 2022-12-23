package com.example.serverclient.server;

import com.example.serverclient.models.Driver;
import com.example.serverclient.server.exceptions.IdNotFoundException;
import com.example.serverclient.models.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Database {
    private static final String url = "jdbc:mysql://localhost:3306/building";
    private static final String user = "root";
    private static final String password = "fde207bdb1d";

    private static Connection con;
    private static ResultSet rs;

    Database() {
        try {
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    ArrayList<Vehicle> getDriverData(int idDriver) {
        try {
            int idVehicle;
            VehicleType type;
            Date date;
            PreparedStatement preparedStatement = con.prepareStatement("select * from vehicle where idDriver = ?");
            preparedStatement.setInt(1, idDriver);
            ResultSet multirs = preparedStatement.executeQuery();
            ArrayList<Vehicle> vehicles = new ArrayList<>();
            while(multirs.next()){
                idVehicle = multirs.getInt("idVehicle");
                type = VehicleType.values()[multirs.getInt("type")-1];
                date = multirs.getDate("date");
                vehicles.add(new Vehicle(idVehicle, idDriver, type, date));
            }
            return vehicles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    ArrayList<Vehicle> getBrigadierVehiclesData(int idBrigadier) {
        try {
            int idVehicle;
            VehicleType type;
            Date date;
            PreparedStatement preparedStatement = con.prepareStatement("select * from vehicle where idDriver = 0");
            ResultSet multirs = preparedStatement.executeQuery();
            ArrayList<Vehicle> vehicles = new ArrayList<>();
            while(multirs.next()){
                idVehicle = multirs.getInt("idVehicle");
                type = VehicleType.values()[multirs.getInt("type")-1];
                date = multirs.getDate("date");
                vehicles.add(new Vehicle(idVehicle, 0, type, date));
            }
            return vehicles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    ArrayList<Driver> getAllDriversForBrigadier(int idBrigadier){
        try {
            int idUser;
            int idDriver;
            PreparedStatement preparedStatement = con.prepareStatement("select * from driver where driver.idBrigadier = ?");
            preparedStatement.setInt(1, idBrigadier);
            ResultSet multirs = preparedStatement.executeQuery();
            ArrayList<Driver> drivers = new ArrayList<>();
            while(multirs.next()){
                idUser = multirs.getInt("idUser");
                idDriver = multirs.getInt("idDriver");
                if(idUser == 0){
                    continue;
                }
                try {
                    drivers.add(new Driver(idDriver, idBrigadier, getUser(idUser)));
                } catch (IdNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            return drivers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    ArrayList<DriverForBrigadier> getBrigadierDriversData(int idBrigadier){
        try {
            int idUser;
            int idDriver;
            String name;
            Date birth;
            int idVehicle;
            Date date;
            PreparedStatement preparedStatement = con.prepareStatement("select driver.idUser, driver.idDriver, user.name, user.birth, vehicle.idVehicle, vehicle.date from driver join vehicle on driver.idDriver = vehicle.idDriver join user on driver.idUser = user.idUser where driver.idBrigadier = ?");
            preparedStatement.setInt(1, idBrigadier);
            ResultSet multirs = preparedStatement.executeQuery();
            ArrayList<DriverForBrigadier> drivers = new ArrayList<>();
            while(multirs.next()){
                idDriver = multirs.getInt("idDriver");
                idVehicle = multirs.getInt("idVehicle");
                date = multirs.getDate("date");
                idUser = multirs.getInt("idUser");
                name = multirs.getString("name");
                birth = multirs.getDate("birth");
                drivers.add(new DriverForBrigadier(idDriver, idVehicle, date, idUser, name, birth));
            }
            return drivers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    void takeVehicle(int idVehicle, int idDriver, LocalDate date) {
        try {
            PreparedStatement  preparedStatement = con.prepareStatement("update vehicle set idDriver = ?, date = ? where idVehicle= ?");
            preparedStatement.setInt(1, idDriver);
            preparedStatement.setDate(2, Date.valueOf(date));
            preparedStatement.setInt(3, idVehicle);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    void freeVehicle(int idVehicle) {
        try {
            PreparedStatement  preparedStatement = con.prepareStatement("update vehicle set idDriver = 0, date = null where idVehicle= ?");
            preparedStatement.setInt(1, idVehicle);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    void insertDriver(String name, LocalDate birth, String login, String password, int idBrigadier, VehicleType type){
        try {
            int userId = getUserAvailableId();
            int driverId = getDriverAvailableId();
            PreparedStatement preparedStatement = con.prepareStatement("insert into user (idUser, name, birth) values (?, ?, ?)");
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, name);
            preparedStatement.setDate(3, Date.valueOf(birth));
            preparedStatement.executeUpdate();
            preparedStatement = con.prepareStatement("insert into driver (idDriver, idUser, idBrigadier, vehicleType) values (?, ?, ?, ?)");
            preparedStatement.setInt(1, driverId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, idBrigadier);
            preparedStatement.setInt(4, type.ordinal()+1);
            preparedStatement.executeUpdate();
            preparedStatement = con.prepareStatement("insert into credentials (idUser, login, password) values (?, ?, ?)");
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, login);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
   ArrayList<Driver> getAllDrivers() {
        try {
            int idUser;
            int idDriver;
            int idBrigadier;
            PreparedStatement preparedStatement = con.prepareStatement("select * from driver");
            ResultSet multirs = preparedStatement.executeQuery();
            ArrayList<Driver> drivers = new ArrayList<>();
            while(multirs.next()){
                idUser = multirs.getInt("idUser");
                idDriver = multirs.getInt("idDriver");
                idBrigadier = multirs.getInt("idBrigadier");
                if(idUser == 0){
                    continue;
                }
                try {
                    drivers.add(new Driver(idDriver, idBrigadier, getUser(idUser)));
                } catch (IdNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            return drivers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    void insertManager(String name, LocalDate birth, String login, String password){
        try {
            int userId = getUserAvailableId();
            int managerId = getBrigadierAvailableId();
            PreparedStatement preparedStatement = con.prepareStatement("insert into user (idUser, name, birth) values (?, ?, ?)");
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, name);
            preparedStatement.setDate(3, Date.valueOf(birth));
            preparedStatement.executeUpdate();
            preparedStatement = con.prepareStatement("insert into manager (idManager, idUser) values (?, ?)");
            preparedStatement.setInt(1, managerId);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
            preparedStatement = con.prepareStatement("insert into credentials (idUser, login, password) values (?, ?, ?)");
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, login);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    void insertVehicle(VehicleType type){
        try {
            int vehicleId = getVehicleAvailableId();
            PreparedStatement preparedStatement = con.prepareStatement("insert into vehicle (idVehicle, idDriver, type, date) values (?, ?, ?, ?)");
            preparedStatement.setInt(1, vehicleId);
            preparedStatement.setInt(2, 0);
            preparedStatement.setInt(3, type.ordinal()+1);
            preparedStatement.setDate(4, null);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    ArrayList<Brigadier> getAllBrigadiers() {
        try {
            int idUser;
            int idBrigadier;
            PreparedStatement preparedStatement = con.prepareStatement("select * from brigadier");
            ResultSet multirs = preparedStatement.executeQuery();
            ArrayList<Brigadier> brigadiers = new ArrayList<>();
            while(multirs.next()){
                idUser = multirs.getInt("idUser");
                idBrigadier = multirs.getInt("idBrigadier");
                if(idUser == 0){
                    continue;
                }
                try {
                    brigadiers.add(new Brigadier(idBrigadier, getUser(idUser)));
                } catch (IdNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            return brigadiers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    void insertBrigadier(String name, LocalDate birth, String login, String password){
        try {
            int userId = getUserAvailableId();
            int brigadierId = getManagerAvailableId();
            PreparedStatement preparedStatement = con.prepareStatement("insert into user (idUser, name, birth) values (?, ?, ?)");
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, name);
            preparedStatement.setDate(3, Date.valueOf(birth));
            preparedStatement.executeUpdate();
            preparedStatement = con.prepareStatement("insert into brigadier (idBrigadier, idUser) values (?, ?)");
            preparedStatement.setInt(1, brigadierId);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
            preparedStatement = con.prepareStatement("insert into credentials (idUser, login, password) values (?, ?, ?)");
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, login);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    ArrayList<Manager> getAllManagers() {
        try {
            int idUser;
            int idManager;
            PreparedStatement preparedStatement = con.prepareStatement("select * from manager");
            ResultSet multirs = preparedStatement.executeQuery();
            ArrayList<Manager> managers = new ArrayList<>();
            while(multirs.next()){
                idUser = multirs.getInt("idUser");
                idManager = multirs.getInt("idManager");
                if(idUser == 0){
                    continue;
                }
                try {
                    managers.add(new Manager(idManager, getUser(idUser)));
                } catch (IdNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            return managers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    ArrayList<Vehicle> getAllVehicles() {
        try {
            int idVehicle;
            int idDriver;
            VehicleType type;
            Date date;
            PreparedStatement preparedStatement = con.prepareStatement("select * from vehicle");
            ResultSet multirs = preparedStatement.executeQuery();
            ArrayList<Vehicle> vehicles = new ArrayList<>();
            while(multirs.next()){
                idVehicle = multirs.getInt("idVehicle");
                idDriver = multirs.getInt("idDriver");
                type = VehicleType.values()[multirs.getInt("type")-1];
                date = multirs.getDate("date");
                vehicles.add(new Vehicle(idVehicle, idDriver, type, date));
            }
            return vehicles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    User getUser(int id) throws IdNotFoundException{
        try {
            PreparedStatement preparedStatement = con.prepareStatement("select * from user where idUser = ?");
            preparedStatement.setInt(1, id);
            rs = preparedStatement.executeQuery();
            if(rs.next()){
                Date birth = rs.getDate("birth");
                String name = rs.getString("name");
                User user = new User(id, name, birth);
                return user;
            }
            else
                throw new IdNotFoundException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    int getUserAvailableId(){
        try {
            PreparedStatement preparedStatement = con.prepareStatement("select max(idUser) from user");
            rs = preparedStatement.executeQuery();
            int availableId = 0;
            if(rs.next()){
                availableId = rs.getInt(1);
            }
            return availableId + 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    int getManagerAvailableId(){
        try {
            PreparedStatement preparedStatement = con.prepareStatement("select max(idManager) from manager");
            rs = preparedStatement.executeQuery();
            int availableId = 0;
            if(rs.next()){
                availableId = rs.getInt(1);
            }
            return availableId + 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    int getBrigadierAvailableId(){
        try {
            PreparedStatement preparedStatement = con.prepareStatement("select max(idBrigadier) from brigadier");
            rs = preparedStatement.executeQuery();
            int availableId = 0;
            if(rs.next()){
                availableId = rs.getInt(1);
            }
            return availableId + 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    int getDriverAvailableId(){
        try {
            PreparedStatement preparedStatement = con.prepareStatement("select max(idDriver) from driver");
            rs = preparedStatement.executeQuery();
            int availableId = 0;
            if(rs.next()){
                availableId = rs.getInt(1);
            }
            return availableId + 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    int getVehicleAvailableId(){
        try {
            PreparedStatement preparedStatement = con.prepareStatement("select max(idVehicle) from vehicle");
            rs = preparedStatement.executeQuery();
            int availableId = 0;
            if(rs.next()){
                availableId = rs.getInt(1);
            }
            return availableId + 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    int checkCredentials(String login, String password){
        try {
            PreparedStatement preparedStatement = con.prepareStatement("select idUser from credentials where login = ? and password = ?");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            rs = preparedStatement.executeQuery();
            int idUser = 0;
            if(rs.next()){
                idUser = rs.getInt("idUser");
            }
            return idUser;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    int getDriverId(int userId){
        try {
            PreparedStatement preparedStatement = con.prepareStatement("select * from driver where driver.idUser = ?");
            preparedStatement.setInt(1, userId);
            rs = preparedStatement.executeQuery();
            int driverId = 0;
            if(rs.next()){
                driverId = rs.getInt(1);
            }
            return driverId;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    int getManagerId(int userId){
        try {
            PreparedStatement preparedStatement = con.prepareStatement("select * from manager where manager.idUser = ?");
            preparedStatement.setInt(1, userId);
            rs = preparedStatement.executeQuery();
            int managerId = 0;
            if(rs.next()){
                managerId = rs.getInt(1);
            }
            return managerId;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    int getBrigadierId(int userId){
        try {
            PreparedStatement preparedStatement = con.prepareStatement("select * from brigadier where brigadier.idUser = ?");
            preparedStatement.setInt(1, userId);
            rs = preparedStatement.executeQuery();
            int brigadierId = 0;
            if(rs.next()){
                brigadierId = rs.getInt(1);
            }
            return brigadierId;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    boolean isLoginFree(String login) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("select * from credentials where credentials.login = ?");
            preparedStatement.setString(1, login);
            rs = preparedStatement.executeQuery();
            if(rs.next()){
                return false;
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
