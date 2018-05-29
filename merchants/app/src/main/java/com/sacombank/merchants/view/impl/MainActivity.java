package com.sacombank.merchants.view.impl;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sacombank.merchants.R;
import com.sacombank.merchants.SacombankApp;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.DaggerMainViewComponent;
import com.sacombank.merchants.injection.MainViewModule;
import com.sacombank.merchants.presenter.MainPresenter;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.view.MainView;
import com.sacombank.merchants.widgets.dialog.DialogFactory;

import javax.inject.Inject;

public class MainActivity extends BaseActivity<MainPresenter, MainView> implements MainView {

    private static final String TAG = MainActivity.class.getSimpleName();

    ImageView imgCloud;
    LinearLayout homeLayout;
    LinearLayout greetingLayout;
    boolean isLogined = false;
    String[] titleList;

    @Inject
    PresenterFactory<MainPresenter> mPresenterFactory;
    private ImageView imgMobilePos;
    private ImageView imgAccountManager;
    private ImageView imgQRCode;
    private ImageView imgHistory;
    private ImageView imgIntroduce;
    private ImageView imgGuideLine;
    private TextView tvMobilePos;
    private TextView tvAccountManager;
    private TextView tvQRCode;
    private TextView tvHistory;
    private TextView tvIntroduce;
    private TextView tvGuideLine;
    private int[] drawableList = new int[6];
    private static final int[] drawableListNotLogin = {R.drawable.ic_mobilepos_notlogin,
            R.drawable.ic_login,
            R.drawable.ic_qrcode_notlogin,
            R.drawable.ic_history_notlogin,
            R.drawable.ic_introduce,
            R.drawable.ic_guideline};
    private static final int[] drawableListLogin = {R.drawable.ic_mobilepos,
            R.drawable.ic_account_manager,
            R.drawable.ic_qrcode,
            R.drawable.ic_history,
            R.drawable.ic_introduce,
            R.drawable.ic_guideline};
    private RelativeLayout layoutMobilePos;
    private RelativeLayout layoutAccountManager;
    private RelativeLayout layoutQRCode;
    private RelativeLayout layoutHistory;
    private RelativeLayout layoutIntroduce;
    private RelativeLayout layoutGuideLine;
    private int padding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        String user = ((SacombankApp) getApplication()).userAccountPref.getValue();
        isLogined = !user.isEmpty();

        initView();
        updateView();
        initPermission();
    }

    private void initView() {
        homeLayout = (LinearLayout) findViewById(R.id.layout_home);
        greetingLayout = (LinearLayout) findViewById(R.id.layout_greeting);
        imgCloud = (ImageView) findViewById(R.id.imgCloud);

        imgMobilePos = (ImageView) findViewById(R.id.imgMobilePos);
        imgAccountManager = (ImageView) findViewById(R.id.imgAccountManager);
        imgQRCode = (ImageView) findViewById(R.id.imgQRCode);
        imgHistory = (ImageView) findViewById(R.id.imgHistory);
        imgIntroduce = (ImageView) findViewById(R.id.imgIntroduce);
        imgGuideLine = (ImageView) findViewById(R.id.imgGuideLine);

        tvMobilePos = (TextView) findViewById(R.id.tvMobilePos);
        tvAccountManager = (TextView) findViewById(R.id.tvAccountManager);
        tvQRCode = (TextView) findViewById(R.id.tvQRCode);
        tvHistory = (TextView) findViewById(R.id.tvHistory);
        tvIntroduce = (TextView) findViewById(R.id.tvIntroduce);
        tvGuideLine = (TextView) findViewById(R.id.tvGuideLine);

        layoutMobilePos = (RelativeLayout) findViewById(R.id.layoutMobilePos);
        layoutAccountManager = (RelativeLayout) findViewById(R.id.layoutAccountManager);
        layoutQRCode = (RelativeLayout) findViewById(R.id.layoutQRCode);
        layoutHistory = (RelativeLayout) findViewById(R.id.layoutHistory);
        layoutIntroduce = (RelativeLayout) findViewById(R.id.layoutIntroduce);
        layoutGuideLine = (RelativeLayout) findViewById(R.id.layoutGuideLine);

        layoutMobilePos.setOnClickListener(this);
        layoutAccountManager.setOnClickListener(this);
        layoutQRCode.setOnClickListener(this);
        layoutHistory.setOnClickListener(this);
        layoutIntroduce.setOnClickListener(this);
        layoutGuideLine.setOnClickListener(this);
    }

    private void updateView() {
        hideHomeIcon();
        updateHomeUI();
    }

    private void updateHomeUI() {
        if (isLogined) {
            padding = 0;
            drawableList = drawableListLogin;
            titleList = getResources().getStringArray(R.array.home_login_item);
            showLockIcon();
            greetingLayout.setVisibility(View.GONE);
        } else {
            padding = 30;
            drawableList = drawableListNotLogin;
            titleList = getResources().getStringArray(R.array.home_item);
            tvQRCode.setTextColor(getResources().getColor(R.color.disable_button_text_color));
            tvHistory.setTextColor(getResources().getColor(R.color.disable_button_text_color));
            greetingLayout.setVisibility(View.VISIBLE);
            hideLockIcon();
        }

        tvMobilePos.setTextColor(getResources().getColor(R.color.disable_button_text_color));

        tvMobilePos.setText(titleList[0]);
        tvAccountManager.setText(titleList[1]);
        tvQRCode.setText(titleList[2]);
        tvHistory.setText(titleList[3]);
        tvIntroduce.setText(titleList[4]);
        tvGuideLine.setText(titleList[5]);

        imgMobilePos.setImageResource(drawableList[0]);
        imgAccountManager.setImageResource(drawableList[1]);
        imgQRCode.setImageResource(drawableList[2]);
        imgHistory.setImageResource(drawableList[3]);
        imgIntroduce.setImageResource(drawableList[4]);
        imgGuideLine.setImageResource(drawableList[5]);


//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutMobilePos.getLayoutParams();
//        params.set
//        layoutAccountManager.setOnClickListener(this);
//        layoutQRCode.setOnClickListener(this);
//        layoutHistory.setOnClickListener(this);
//        layoutIntroduce.setOnClickListener(this);
//        layoutGuideLine.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutMobilePos:
                DialogFactory.createMessageDialog(view.getContext(), getString(R.string.notify_update_mobilepos));
                break;
            case R.id.layoutAccountManager:
                if (isLogined)
                    goAccountManagerPage();
                else
                    goLoginPage();
                break;
            case R.id.layoutQRCode:
                if (isLogined) {
                    Intent tabIntent2 = new Intent(MainActivity.this, BottomTabbarActivity.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("title", titleList[2]);
                    bundle2.putInt("index", 2);
                    tabIntent2.putExtras(bundle2);
                    startActivity(tabIntent2);
                    finish();
                } else {
                    goLoginPage();
                }
                break;
            case R.id.layoutHistory:
                if (isLogined) {
                    Intent tabIntent3 = new Intent(MainActivity.this, BottomTabbarActivity.class);
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("title", titleList[3]);
                    bundle3.putInt("index", 3);
                    tabIntent3.putExtras(bundle3);
                    startActivity(tabIntent3);
                    finish();
                } else {
                    goLoginPage();
                }
                break;
            case R.id.layoutIntroduce:
                Intent tabIntent4 = new Intent(MainActivity.this, BottomTabbarActivity.class);
                Bundle bundle4 = new Bundle();
                bundle4.putString("title", titleList[4]);
                bundle4.putInt("index", 4);
                tabIntent4.putExtras(bundle4);
                startActivity(tabIntent4);
                finish();
                break;
            case R.id.layoutGuideLine:
                Intent tabIntent5 = new Intent(MainActivity.this, BottomTabbarActivity.class);
                Bundle bundle5 = new Bundle();
                bundle5.putString("title", titleList[5]);
                bundle5.putInt("index", 5);
                tabIntent5.putExtras(bundle5);
                startActivity(tabIntent5);
                finish();
                break;
            case R.id.imgLock:
                logout();
                break;
        }
    }

    private void goAccountManagerPage() {
        Intent accountIntent = new Intent(MainActivity.this, BottomTabbarActivity.class);
        Bundle accountBundle = new Bundle();
        accountBundle.putString("title", titleList[1]);
        accountBundle.putInt("index", 1);
        accountIntent.putExtras(accountBundle);
        startActivity(accountIntent);
        finish();
    }


    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerMainViewComponent.builder()
                .appComponent(parentComponent)
                .mainViewModule(new MainViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void logout() {
        DialogFactory.createMessageDialogLogout(this, getResources().getString(R.string.confirm_logout),
                new DialogFactory.DialogListener.LogoutListener() {
                    @Override
                    public void logOut() {
                        ((SacombankApp) getApplication()).userAccountPref.setValue("");
                        isLogined = false;
                        updateHomeUI();
                        goLoginPage();
                    }
                });
    }

    @Override
    protected void forceLogout() {
        ((SacombankApp) getApplication()).userAccountPref.setValue("");
        goLoginPage();
    }

    @Override
    protected void goLoginPage() {
        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent1);
        finish();
    }

    @NonNull
    @Override
    protected PresenterFactory<MainPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //Permisson don't granted
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    System.out.print("Permission isn't granted ");
                }
                // Permisson don't granted and dont show dialog again.
                else {
                    System.out.print("Permisson don't granted and dont show dialog again ");
                }
                //Register permission
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.print("Permision Write File is Granted");
            } else {
                System.out.print("Permision Write File is Denied");
                Toast.makeText(MainActivity.this, "Bạn không cấp quyền! Một số tính năng có thể không hoạt động!", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void updateNavigationBar(String fragmentClass, String title) {

    }

    @Override
    public void addToolBar() {

    }

    @Override
    public void addTabBar() {

    }

    @Override
    public void showMessage(String msg) {

    }

    @Override
    protected void goHomePage() {

    }
}
