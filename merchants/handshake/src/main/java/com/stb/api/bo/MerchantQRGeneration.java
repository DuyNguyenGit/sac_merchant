package com.stb.api.bo;

import com.stb.api.model.STBResponse;

/**
 * Created by nguyenletruong on 8/12/17.
 */

public class MerchantQRGeneration extends STBResponse {
    //request
    public String LanguageID;
    public String DeviceID;
    public String MID;
    public String TID;
    public Integer Amount;
    public Integer Tips;
    public String UserName;
    public String mVisaMID;
    public String BillNumber;
    public String RefNumber;
    //response
    public String Fee;
    public String QRStream;
}
