package com.stb.api.bo;

import com.stb.api.model.STBResponse;

/**
 * Created by nguyenletruong on 8/13/17.
 */

public class MerchantAccountInquiry extends STBResponse {
    public String DeviceID;
    public String LanguageID;
    public String UserName;
    public String RefNumber;
    //response
    public String StaticQRData;
    public MerchantObject MerchantObject;
    public AccountObject[] AccountObject;
    public ModuleList[] ModuleList;
}
