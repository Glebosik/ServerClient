package com.example.serverclient.models;

import java.io.Serializable;
import java.sql.Date;

public class DriverForBrigadier extends User implements Serializable {
    private int idDriver;
    private int idVehicle;
    private Date date;

    public DriverForBrigadier(int idDriver, int idVehicle, Date date, int idUser, String name, Date birth){
        super(idUser, name, birth);
        this.idDriver = idDriver;
        this.idVehicle = idVehicle;
        this.date = date;
    }
    public DriverForBrigadier(int idDriver, int idVehicle, Date date, User user){
        super(user.idUser, user.name, user.birth);
        this.idDriver = idDriver;
        this.idVehicle = idVehicle;
        this.date = date;
    }


    public int getIdDriver() {
        return idDriver;
    }

    public int getIdVehicle() {
        return idVehicle;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
