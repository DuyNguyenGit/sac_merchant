package com.sacombank.merchants;

import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sacombank.merchants.view.impl.MainActivity;
import com.sacombank.merchants.api.ApiManager;
import com.sacombank.merchants.widgets.dialog.DialogFactory;
import com.stb.api.bo.AppSessionHandshake;
import com.stb.api.listeners.ApiResponse;

public class SplashScreenActivity extends AppCompatActivity {
    private final static int DURING_TIME = 3000;

    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ((SacombankApp) getApplication()).clearAllData();
        ApiManager.requestHandshake(new ApiResponse<AppSessionHandshake>() {
            @Override
            public void onSuccess(AppSessionHandshake result) {
                gotoHomePage();
            }

            @Override
            public void onError(AppSessionHandshake error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogFactory.createMessageDialog(SplashScreenActivity.this, getString(R.string.dialog_not_internet));
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void gotoHomePage() {
        Intent homePage = new Intent(this, MainActivity.class);
        homePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString("user_account", ((SacombankApp) getApplication()).userAccountPref.getValue());
        homePage.putExtras(bundle);
        startActivity(homePage);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null)
            mHandler.removeCallbacks(mRunnable);
    }
}
