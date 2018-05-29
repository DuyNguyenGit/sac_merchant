package com.stb.api.bo;

import com.stb.api.model.STBResponse;

public class MerchantAccountLogin extends STBResponse {
    //request
    public String Platform;
    public String OSVersion;
    public String Model;
    public String LanguageID;
    public String UserName;
    public String Password;
    public String FirebaseToken;
    public String RefNumber;
    public  String DeviceID;
    //response
    public String StaticQRData;
    public MerchantObject MerchantObject;
    public String UserID;
    public String MerchantName;//lenght:25
    public String City;//13
    public String CountryCode;//2
    public String MCC;//4
    public String Phone;
    public String Address;
    public AccountObject[]AccountObject;
    public String CurrencyCode;//3
    public String ModuleList;
    public String ModuleCode;
    public String ModuleName;
    public Boolean IsForceUpdatePassword;
}
