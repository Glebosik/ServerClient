package com.example.serverclient.client;

import com.example.serverclient.models.UserType;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterManagerController {
    @FXML
    private TextField loginField;
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private DatePicker datePicker;

    @FXML
    protected void onRegisterButtonClick() {
        if(loginField.getText().isEmpty() || passwordField.getText().isEmpty() || datePicker.getValue()==null || nameField.getText().isEmpty())
            return;
        ClientApplication.connection.registerRequest(UserType.Manager, loginField.getText(), passwordField.getText(), nameField.getText(), datePicker.getValue());
    }
    @FXML
    protected void onBackButtonClick() {
        ClientApplication.app.switchScene("login.fxml");
    }
}