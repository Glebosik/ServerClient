package com.example.serverclient.client;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;

    @FXML
    protected void onLoginButtonClick() {
        if(loginField.getText().isEmpty() || passwordField.getText().isEmpty())
            return;
        ClientApplication.connection.loginRequest(loginField.getText(), passwordField.getText());
    }
    @FXML
    protected void onRegisterButtonClick() {
        ClientApplication.connection.getAllBrigadiersForRegister();
    }
}