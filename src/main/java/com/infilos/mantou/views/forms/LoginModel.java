package com.infilos.mantou.views.forms;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LoginModel {
    private final StringProperty username = new SimpleStringProperty("Username");
    private final StringProperty password = new SimpleStringProperty("Password");

    public String getUsername() {
        return username.get();
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }
}
