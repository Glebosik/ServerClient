package com.example.serverclient.models;

import java.io.Serializable;
import java.sql.Date;

public class DriverTable extends User implements Serializable {
    private int idDriver;
    private int idBrigadier;
    private int idVehicle;

    public DriverTable(int idDriver, int idBrigadier, int idVehicle, int idUser, String name, Date birth){
        super(idUser, name, birth);
        this.idDriver = idDriver;
        this.idBrigadier = idBrigadier;
        this.idVehicle = idVehicle;
    }
    public DriverTable(int idDriver, int idBrigadier, int idVehicle, User user){
        super(user.idUser, user.name, user.birth);
        this.idDriver = idDriver;
        this.idBrigadier = idBrigadier;
        this.idVehicle = idVehicle;
    }


    public int getIdDriver() {
        return idDriver;
    }

    public int getIdBrigadier() {
        return idBrigadier;
    }

    public int getIdVehicle() {
        return idVehicle;
    }
}
