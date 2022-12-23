package com.example.serverclient.models;

import java.io.Serializable;
import java.sql.Date;

public class Manager extends User implements Serializable {
    private int idManager;

    public Manager(int idManager, int idUser, String name, Date birth){
        super(idUser, name, birth);
        this.idManager = idManager;
    }

    public Manager(int idManager, User user){
        super(user.idUser, user.name, user.birth);
        this.idManager = idManager;
    }

    public int getIdManager() {
        return idManager;
    }
}
