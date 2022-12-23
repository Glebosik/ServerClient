package com.example.serverclient.models;

public class Session {
    private int userId = 0;
    private int specificId = 0;
    private UserType userType;
    private String name;

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getSpecificId() {
        return specificId;
    }
    public void setSpecificId(int specificId) {
        this.specificId = specificId;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
