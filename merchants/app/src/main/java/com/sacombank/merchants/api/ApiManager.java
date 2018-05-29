package com.sacombank.merchants.api;

import android.util.Log;

import com.google.gson.Gson;
import com.stb.api.STBAppClient;
import com.stb.api.STBGlobal;
import com.stb.api.bo.AccountObject;
import com.stb.api.bo.AppSessionHandshake;
import com.stb.api.bo.MerchantAccountLogin;
import com.stb.api.bo.MerchantAccountInquiry;
import com.stb.api.bo.MerchantPasswordChange;
import com.stb.api.bo.MerchantPasswordRetrieval;
import com.stb.api.bo.MerchantQRGeneration;
import com.stb.api.bo.MerchantQRShare;
import com.stb.api.bo.MerchantTransactionInquiry;
import com.stb.api.bo.MerchantTransactionReversal;
import com.stb.api.crypto.TripleDESCrypto;
import com.stb.api.listeners.ApiResponse;

import java.util.UUID;

public class ApiManager {

    /*** Auth Type ***/
    public enum  TransactionStatus{
        SUCCESSFUl{//giao dịch thành công
            public String toString() {
                return "SU";
            }
        },
        REVERSAL{//giao dịch đã reverse
            public String toString() {
                return "RE";
            }
        },
        PENDING{//đang chờ xử lý
            public String toString() {
                return "PE";
            }
        }
    }

    private static final String TAG = ApiManager.class.getSimpleName();

    private static String mMID, mTID, mUserName, mPassword, mVisaID;
    private static String mRefNumber = UUID.randomUUID().toString().replaceAll("-", "").substring(12);
    private static String mLanguageID = "VI";
    private static String mDeviceID = "123455666";
    private static String mPlatform = "Android";
    private static String mOSVersion = "5.1.0";
    private static String mModel = "SamSung";
    public static final String SESSION_TIMEOUT = "440";
    public static final String NOT_NETWORIKING = "911";
    private static Boolean bIsLoginSuccessful = false;

    public static void setModel(String model)
    {
        mModel = model;
    }

    public static void setOSVersion(String OSVersion)
    {
        mOSVersion = OSVersion;
    }

    public static void setPlatform(String platform)
    {
        mPlatform = platform;
    }

    public static void setDeviceId(String deviceId)
    {
        mDeviceID = deviceId;
    }

    public static void setLanguageId(String languageId)
    {
        mLanguageID = languageId;
    }

    public static void requestHandshake(final ApiResponse<AppSessionHandshake> listener) {
        // Handshake with Sacombank's API
        STBAppClient.handshake(mDeviceID, mLanguageID, new ApiResponse<AppSessionHandshake>() {
            @Override
            public void onSuccess(AppSessionHandshake result) {
                System.out.println("AppSessionHandshake handshake = " + new Gson().toJson(result));
                System.out.println("AppSessionHandshake SessionKey = " + result.SessionKey);
                System.out.println("sessionHandshake.RespCode 2 = " + result.RespCode);
                if (result.RespCode != null && result.RespCode.equals("00")) {
                    STBGlobal.SessionTimeout = result.SessionTimeout;
                    listener.onSuccess(result);
                }
                else if(result.SessionKey == null) {
                    System.out.println("not networking");
                    result.RespCode = "911";
                    listener.onError(result);
                }
            }

            @Override
            public void onError(AppSessionHandshake error) {
                System.out.println("error handshake = " + new Gson().toJson(error));
                listener.onError(error);
            }
        });

    }

    public static void requestLogin(final MerchantAccountLogin object, final ApiResponse<MerchantAccountLogin> listener) {
        System.out.print("In::requestLogin");
        if (object != null) {
            object.DeviceID = mDeviceID;
            object.LanguageID = mLanguageID;
            object.Platform = mPlatform;
            object.OSVersion = mOSVersion;
            object.Model = mModel;
            object.RefNumber = mRefNumber;
            Log.d(TAG,"device token = " + object.FirebaseToken);
            STBAppClient.send(object, new ApiResponse<MerchantAccountLogin>() {
                @Override
                public void onSuccess(MerchantAccountLogin result) {
                    Log.e(TAG, "onSuccess: >>>" + new Gson().toJson(result));
                    if (result != null) {
                        mPassword = result.Password;
                    }
                    if (result.AccountObject != null) {
                        AccountObject account = result.AccountObject[0];
                        mMID = account.MID;
                        mTID = account.TID;
                    }
                    if (result.MerchantObject != null) {
                        mVisaID = result.MerchantObject.mVisaMID;
                        mUserName = result.MerchantObject.UserName;
                    }
                    listener.onSuccess(result);
                    bIsLoginSuccessful = true;
                }

                @Override
                public void onError(MerchantAccountLogin error) {
                    listener.onError(error);

                    bIsLoginSuccessful = false;
                }
            });
        }
        System.out.print("Out::requestLogin");
    }

    public static void requestChangePassword(final MerchantPasswordChange object, final ApiResponse<MerchantPasswordChange> listener) {
        if (object != null) {
            object.DeviceID = mDeviceID;
            object.LanguageID = mLanguageID;
            object.RefNumber = mRefNumber;
            object.UserName = mUserName;
            object.OldPassword = TripleDESCrypto.encrypt(object.OldPassword, STBAppClient.getSessionKey());
            object.NewPassword = TripleDESCrypto.encrypt(object.NewPassword, STBAppClient.getSessionKey());
            STBAppClient.send(object, listener);
        }
    }

    public static void requestForgotPassword(final MerchantPasswordRetrieval object, final ApiResponse<MerchantPasswordRetrieval> listener) {
        if (object != null) {
            object.DeviceID = mDeviceID;
            object.LanguageID = mLanguageID;
            object.RefNumber = mRefNumber;
            STBAppClient.send(object, listener);
        }
    }

    public static void requestManageAccount(final ApiResponse<MerchantAccountInquiry> listener) {
        MerchantAccountInquiry object = new MerchantAccountInquiry();
        object.UserName = mUserName;
        object.DeviceID = mDeviceID;
        object.LanguageID = mLanguageID;
        object.RefNumber = mRefNumber;
        STBAppClient.send(object, listener);
    }

    public static void requestQRCodeShare(final MerchantQRShare object, final  ApiResponse<MerchantQRShare> listener) {
        if(object!= null) {
            object.DeviceID = mDeviceID;
            object.LanguageID = mLanguageID;
            object.RefNumber = mRefNumber;
            STBAppClient.send(object, listener);
        }
    }

    public static void requestCreateQRCodeDynamic(final MerchantQRGeneration object, final ApiResponse<MerchantQRGeneration> listener) {
        if (object != null) {
            object.DeviceID = mDeviceID;
            object.LanguageID = mLanguageID;
            object.MID = mMID;
            object.TID = mTID;
            object.UserName = mUserName;
            object.mVisaMID = mVisaID;
            object.RefNumber = mRefNumber;
            Log.d(TAG,"Amount = " + object.Amount);
            Log.d(TAG,"Tip = " + object.Tips);
            Log.d(TAG,"Billnumber = " + object.BillNumber);
            Log.d(TAG,"Description = " + object.Description);
            STBAppClient.send(object, listener);
        }
    }

    public static void requestTransactionHistory(final MerchantTransactionInquiry object, final ApiResponse<MerchantTransactionInquiry> listener) {
        if (object != null) {
            object.DeviceID = mDeviceID;
            object.LanguageID = mLanguageID;
            object.TID = mTID;
            object.RefNumber = mRefNumber;
            STBAppClient.send(object, listener);
        }
    }

    public static void requestTransactionReversal(final MerchantTransactionReversal object, final ApiResponse<MerchantTransactionReversal> listener) {
        if (object != null) {
            object.DeviceID = mDeviceID;
            object.LanguageID = mLanguageID;
            object.RefNumber = mRefNumber;
            Log.d(TAG,"mUserName = " + mUserName);
            object.UserName = mUserName;
            object.AccountPassword = TripleDESCrypto.encrypt(object.AccountPassword, STBAppClient.getSessionKey());
            STBAppClient.send(object, listener);
        }
    }

    /*** unit test ***/
    static void testAPILogin() {
        MerchantAccountLogin object = new MerchantAccountLogin();
        object.UserName = "hello";
        object.Password = "924544";
        object.FirebaseToken = "dh2r929wut934utojfg93u4ut3ujtg";
        requestLogin(object, new ApiResponse<MerchantAccountLogin>() {
            @Override
            public void onSuccess(MerchantAccountLogin result) {

            }

            @Override
            public void onError(MerchantAccountLogin error) {

            }
        });
    }

    static void testAPIQRCodeDynamic(MerchantAccountLogin result)
    {
        System.out.print("In::testAPIQRCodeDynamic");
        MerchantQRGeneration object = new MerchantQRGeneration();
        object.Amount = 10;
        object.Tips = 1;
        object.BillNumber = "01234567890";
        requestCreateQRCodeDynamic(object, new ApiResponse<MerchantQRGeneration>() {
            @Override
            public void onSuccess(MerchantQRGeneration result) {
                //testAPIQRCodeShare(result);
            }

            @Override
            public void onError(MerchantQRGeneration error) {

            }
        });
        System.out.print("Out::testAPIQRCodeDynamic\n");
    }

    static void testAPIQRCodeShare(MerchantAccountLogin result) {
        System.out.print("In::testAPIQRCodeShare");
        MerchantQRShare object = new MerchantQRShare();
        object.QRStream = result.StaticQRData;
        object.ToEmail = "tuong.nguyen@akadigital.vn";
        object.Note = "Test Data";
        requestQRCodeShare(object, new ApiResponse<MerchantQRShare>() {
            @Override
            public void onSuccess(MerchantQRShare result) {
                //testAPIMerchantTransactionInquiry();
            }

            @Override
            public void onError(MerchantQRShare error) {
                //testAPIMerchantTransactionInquiry();
            }
        });
        System.out.print("Out::testAPIQRCodeShare");
    }

    static void testAPIMerchantTransactionInquiry()
    {
        System.out.print("In::testAPIMerchantTransactionInquiry");
        MerchantTransactionInquiry object = new MerchantTransactionInquiry();
        object.SearchDate = "";//optional: (yyyyMMdd)
        object.LastDigitCardNo = "";//optional
        object.SelectIndex = 1;//optional
        object.SelectSize = 20;//optional
        requestTransactionHistory(object, new ApiResponse<MerchantTransactionInquiry>() {
            @Override
            public void onSuccess(MerchantTransactionInquiry result) {
                testAPIMerchantAccountInquiry();
            }

            @Override
            public void onError(MerchantTransactionInquiry error) {

            }
        });
        System.out.print("Out::testAPIMerchantTransactionInquiry");
    }

    static void testMerchantPasswordChange()
    {
        System.out.print("In::testMerchantPasswordChange");
        MerchantPasswordChange object = new MerchantPasswordChange();
        object.OldPassword = "981797";
        object.NewPassword = "123456";
        requestChangePassword(object, new ApiResponse<MerchantPasswordChange>() {
            @Override
            public void onSuccess(MerchantPasswordChange result) {

            }

            @Override
            public void onError(MerchantPasswordChange error) {

            }
        });
        System.out.print("Out::testMerchantPasswordChange");
    }
    // test API forgetPass
    static void testMerchantPasswordRetrieval()
    {
        System.out.print("In::testMerchantPasswordRetrieval");
        MerchantPasswordRetrieval object = new MerchantPasswordRetrieval();
        object.UserName = "hello";
        requestForgotPassword(object, new ApiResponse<MerchantPasswordRetrieval>() {
            @Override
            public void onSuccess(MerchantPasswordRetrieval result) {

            }

            @Override
            public void onError(MerchantPasswordRetrieval error) {

            }
        });
        System.out.print("Out::testMerchantPasswordRetrieval");
    }
    // test Api Manage Account
    static void testAPIMerchantAccountInquiry()
    {
        requestManageAccount(new ApiResponse<MerchantAccountInquiry>() {
            @Override
            public void onSuccess(MerchantAccountInquiry result) {

            }

            @Override
            public void onError(MerchantAccountInquiry error) {

            }
        });
    }
    // test Api MerchantTransactionReversal
    static  void testAPIMerchantTransactionReversal() {
        System.out.print("In:testAPIMerchantTransactionReversal");
        MerchantTransactionReversal object = new MerchantTransactionReversal();
        object.ReferenceNo = "7845123269";
        object.BatchNo = "56";
        object.ApprovalCode = "562314";
        object.Amount = "1234000";
        object.MaskCardNumber = "4577412511";
        object.CardCode = "";
        requestTransactionReversal(object, new ApiResponse<MerchantTransactionReversal>() {
            @Override
            public void onSuccess(MerchantTransactionReversal result) {

            }

            @Override
            public void onError(MerchantTransactionReversal error) {

            }
        });
        System.out.print("out:testAPIMerchantTransactionReversal");
    }
}