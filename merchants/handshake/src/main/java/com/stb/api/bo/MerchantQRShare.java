package com.stb.api.bo;

import com.stb.api.model.STBResponse;

/**
 * Created by nguyenletruong on 8/13/17.
 */

public class MerchantQRShare extends STBResponse {
    //request
    public String LanguageID;
    public String DeviceID;
    public String QRStream;
    public String ToEmail;
    public String Note;
    public String RefNumber;
}
