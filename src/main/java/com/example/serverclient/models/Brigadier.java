package com.example.serverclient.models;

import java.io.Serializable;
import java.sql.Date;

public class Brigadier extends User implements Serializable {
    private int idBrigadier;

    public Brigadier(int idBrigadier, int idUser, String name, Date birth){
        super(idUser,name,birth);
        this.idBrigadier = idBrigadier;
    }

    public Brigadier(int idBrigadier, User user){
        super(user.idUser, user.name, user.birth);
        this.idBrigadier = idBrigadier;
    }

    public int getIdBrigadier() {
        return idBrigadier;
    }
}
