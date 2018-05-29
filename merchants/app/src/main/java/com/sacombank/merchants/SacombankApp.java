package com.sacombank.merchants;

import android.app.Application;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.AppModule;
import com.sacombank.merchants.injection.DaggerAppComponent;
import com.sacombank.merchants.util.constants.ShareReference;
import com.sacombank.merchants.util.preference.IntegerPrefs;
import com.sacombank.merchants.util.preference.StringPrefs;
import com.sacombank.merchants.api.ApiManager;
import io.fabric.sdk.android.Fabric;

public final class SacombankApp extends Application {
    private AppComponent mAppComponent;
    private String TAG = SacombankApp.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        saveCommonSystemInfo();
    }

    private void saveCommonSystemInfo() {
        String deviceID = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        ApiManager.setDeviceId(deviceID);
        Log.e(TAG, "saveCommonSystemInfo: >>>" + deviceID);
    }

    @NonNull
    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public final StringPrefs userAccountPref = new StringPrefs(this, ShareReference.USER, "");
    public final StringPrefs firebaseTokenPref = new StringPrefs(this, ShareReference.FIREBASE_TOKEN, "");
    public final StringPrefs qrCodeDynamicPref = new StringPrefs(this, ShareReference.QRCODE_DYNAMIC, "");
    public final StringPrefs historyTransaction = new StringPrefs(this, ShareReference.HISTORY_TRANSACTION, "");
    public final StringPrefs oldPassword = new StringPrefs(this, ShareReference.OLD_PASSWORD, "");
    public final IntegerPrefs flagForgotPass = new IntegerPrefs(this, ShareReference.FLAG_FORGOT_PASS, 0);
    public final IntegerPrefs flagForgotPass2 = new IntegerPrefs(this, ShareReference.FLAG_FORGOT_PASS_2, 0);
    public final StringPrefs flagHistoryReferNo = new StringPrefs(this, ShareReference.FLAG_HISTORY_REFER_NO, "");

    public void clearAllData() {
        userAccountPref.clearAll();
        qrCodeDynamicPref.clearAll();
        historyTransaction.clearAll();
        oldPassword.clearAll();
        flagForgotPass.clearAll();
        flagForgotPass2.clearAll();
        flagHistoryReferNo.clearAll();
    }
}