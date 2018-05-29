package com.stb.api.bo;

import com.stb.api.model.STBResponse;

/**
 * Created by nguyenletruong on 8/13/17.
 */

public class MerchantTransactionInquiry extends STBResponse {
    // request
    public String LanguageID;
    public String DeviceID;
    public String TID;
    public String SearchDate;//Optional, yyyyMMdd
    public String LastDigitCardNo;
    public Integer SelectIndex;
    public Integer SelectSize;
    public String RefNumber;
    // response
    public TransactionObject []TransactionObject;
}
