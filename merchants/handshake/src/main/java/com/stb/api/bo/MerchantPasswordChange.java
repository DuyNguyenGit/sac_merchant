package com.stb.api.bo;

import com.stb.api.model.STBResponse;

/**
 * Created by nguyenletruong on 8/13/17.
 */

public class MerchantPasswordChange extends STBResponse {
    //request
    public String LanguageID;
    public String DeviceID;
    public String OldPassword;
    public String NewPassword;
    public String UserName;
    public String RefNumber;
}
