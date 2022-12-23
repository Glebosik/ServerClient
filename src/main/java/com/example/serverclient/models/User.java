package com.example.serverclient.models;

import java.io.Serializable;
import java.sql.Date;

public class User implements Serializable {
    protected int idUser;
    protected String name;
    protected Date birth;

    public User(int idUser, String name, Date birth) {
        this.idUser = idUser;
        this.name = name;
        this.birth = birth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }
}
