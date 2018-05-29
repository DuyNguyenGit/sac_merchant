package com.sacombank.merchants.model.jsonobject;

/**
 * Created by DUY on 8/16/2017.
 */

public class QRCodeSharing {
    String email;
    String qrCodeStream;
    String note;

    public QRCodeSharing(String email, String qrCodeStream, String note) {
        this.email = email;
        this.qrCodeStream = qrCodeStream;
        this.note = note;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQrCodeStream() {
        return qrCodeStream;
    }

    public void setQrCodeStream(String qrCodeStream) {
        this.qrCodeStream = qrCodeStream;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
