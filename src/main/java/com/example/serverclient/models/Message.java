package com.example.serverclient.models;

import java.io.Serializable;

public class Message implements Serializable {
    public Requests type;
    public boolean status;
    public Object message;

    public Message(Requests type, boolean status, Object message){
        this.type = type;
        this.status = status;
        this.message = message;
    }
}
