package com.example.serverclient.models;

import java.io.Serializable;
import java.sql.Date;

public class Driver extends User implements Serializable {
    private int idDriver;
    private int idBrigadier;

    public Driver(int idDriver, int idBrigadier, int idUser, String name, Date birth){
        super(idUser, name, birth);
        this.idDriver = idDriver;
        this.idBrigadier = idBrigadier;
    }
    public Driver(int idDriver, int idBrigadier, User user){
        super(user.idUser, user.name, user.birth);
        this.idDriver = idDriver;
        this.idBrigadier = idBrigadier;
    }


    public int getIdDriver() {
        return idDriver;
    }
}
