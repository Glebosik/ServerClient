package com.example.serverclient.models;

import java.io.Serializable;
import java.sql.Date;


public class Vehicle implements Serializable {
    private int idVehicle;
    private int idDriver;
    private VehicleType type;
    private Date date;

    public Vehicle(int idVehicle, int idDriver, VehicleType type, Date date){
        this.idVehicle = idVehicle;
        this.idDriver = idDriver;
        this.type = type;
        this.date = date;
    }
    public Vehicle(int idVehicle, int idDriver, int type, Date date){
        this.idVehicle = idVehicle;
        this.idDriver = idDriver;
        this.type = VehicleType.values()[type];
        this.date = date;
    }

    public int getIdVehicle() {
        return idVehicle;
    }

    public int getIdDriver() {
        return idDriver;
    }

    public VehicleType getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
