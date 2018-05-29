package com.sacombank.merchants.model.jsonobject;

/**
 * Created by DUY on 8/16/2017.
 */

public class PasswordData {
    String passOld;
    String passNew;

    public PasswordData(String passOld, String passNew) {
        this.passOld = passOld;
        this.passNew = passNew;
    }

    public String getPassOld() {
        return passOld;
    }

    public void setPassOld(String passOld) {
        this.passOld = passOld;
    }

    public String getPassNew() {
        return passNew;
    }

    public void setPassNew(String passNew) {
        this.passNew = passNew;
    }
}
