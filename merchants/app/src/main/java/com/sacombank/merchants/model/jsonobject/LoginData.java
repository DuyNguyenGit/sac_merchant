package com.sacombank.merchants.model.jsonobject;

/**
 * Created by DUY on 7/15/2017.
 */

public class LoginData {
    String username;
    String password;
    String firebaseToken;

    public LoginData() {
    }

    public LoginData(String username, String password, String firebaseToken) {
        this.username = username;
        this.password = password;
        this.firebaseToken = firebaseToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }
}
