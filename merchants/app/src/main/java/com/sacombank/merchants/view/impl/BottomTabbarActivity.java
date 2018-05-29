package com.sacombank.merchants.view.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sacombank.merchants.R;
import com.sacombank.merchants.SacombankApp;
import com.sacombank.merchants.adapter.TabAdapter;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.BottomTabbarViewModule;
import com.sacombank.merchants.injection.DaggerBottomTabbarViewComponent;
import com.sacombank.merchants.model.staticmodel.TabModel;
import com.sacombank.merchants.presenter.BottomTabbarPresenter;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.util.constants.Constant;
import com.sacombank.merchants.view.BottomTabbarView;
import com.sacombank.merchants.widgets.FooterMenu;
import com.sacombank.merchants.widgets.dialog.DialogFactory;

import javax.inject.Inject;

public final class BottomTabbarActivity extends BaseActivity<BottomTabbarPresenter, BottomTabbarView> implements  BottomTabbarView, View.OnClickListener {
    private static final String TAG = BottomTabbarActivity.class.getSimpleName();
    @Inject
    PresenterFactory<BottomTabbarPresenter> mPresenterFactory;

    FooterMenu footerMenu;
    TextView tvTitle;
    ImageView imgBack;
    RelativeLayout layoutBack;
    String[] titleList;
    BaseFragment currentFragment;
    private FragmentManager fragmentManager;

    private TabAdapter.OnTabClickListener mOnTabClickListener = new TabAdapter.OnTabClickListener() {

        @Override
        public void onClick(TabModel tab) {
            BaseFragment fragment = null;
            switch (tab.getId()) {
                case Constant.FOOTER_CREATE_QR:
                    Log.e(TAG, "onClick: Footer >>>" + tab.getId());
                    fragment = new CreateQRCodeFragment();
                    break;
                case Constant.FOOTER_MOBILEPOS:
                    Log.e(TAG, "onClick: Footer >>>" + tab.getId());

                    DialogFactory.createMessageDialogMobilePos(BottomTabbarActivity.this, getString(R.string.notify_update_mobilepos),
                            new DialogFactory.DialogListener.MobilePosListener() {
                                @Override
                                public void goHome() {
                                    //goHomePage();
                                }
                            });
                    //fragment = new MobilePosFragment();
                    return;
                case Constant.FOOTER_HISTORY:
                    Log.e(TAG, "onClick: Footer >>>" + tab.getId());
                    fragment = new HistoryFragment();
                    break;
                case Constant.FOOTER_INTRODUCE:
                    Log.e(TAG, "onClick: Footer >>>" + tab.getId());
                    fragment = new IntroduceFragment();
                    break;
                default:
                    Log.e(TAG, "onClick: Footer >>>" + tab.getId());
                    break;
            }
            setTitle(titleList[tab.getId()]);
            processPushFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_tabbar);
        titleList = getResources().getStringArray(R.array.bottom_item);
        fragmentManager = getSupportFragmentManager();
        initView();
        updateView();
    }

    private void updateView() {
        String user = ((SacombankApp) getApplication()).userAccountPref.getValue();
        if (user.isEmpty())
            hideLockIcon();
        else
            showLockIcon();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String title = bundle.getString("title");
        int index = bundle.getInt("index", 0);
        tvTitle.setText(title);
        pushFragment(index);
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tvTitleNav);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        layoutBack = (RelativeLayout) findViewById(R.id.layout_back);
        layoutBack.setOnClickListener(this);
        footerMenu = (FooterMenu) findViewById(R.id.footer_menu);
        footerMenu.setOnTabClickListener(mOnTabClickListener);
    }

    protected void setTitle(String title) {
        tvTitle.setText(title);
    }

    private void pushFragment(int index) {
        BaseFragment fragment = null;
        switch (index) {
            case 0:
                //TODO : MOBILEPOS FRAGMENT
                fragment = null;
                break;
            case 1:
                fragment = new AccountManagerFragment();
                break;
            case 2:
                fragment = new CreateQRCodeFragment();
                break;
            case 3:
                fragment = new HistoryFragment();
                break;
            case 4:
                fragment = new IntroduceFragment();
                break;
            case 5:
                fragment = new GuidelineFragment();
                break;
        }
        processPushFragment(fragment);
    }


    private void processPushFragment(BaseFragment fragment) {
        if (fragment == null) return;
        fragment.setBaseListener(this);
        currentFragment = (BaseFragment) fragment;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerBottomTabbarViewComponent.builder()
                .appComponent(parentComponent)
                .bottomTabbarViewModule(new BottomTabbarViewModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<BottomTabbarPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public void onBackPressed() {
        if(fragmentManager.getBackStackEntryCount()>1){
            super.onBackPressed();
        }else {
            goHomePage();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                if(fragmentManager.getBackStackEntryCount()>1){
                    onBackPressed();
                    break;
                }else {
                    goHomePage();
                }
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    @Override
    protected void logout() {
        DialogFactory.createMessageDialogLogout(this, getResources().getString(R.string.confirm_logout),
                new DialogFactory.DialogListener.LogoutListener() {
                    @Override
                    public void logOut() {
                        ((SacombankApp) getApplication()).userAccountPref.setValue("");
                        goHomePage();
                    }
                });
    }

    @Override
    protected void forceLogout() {
        ((SacombankApp) getApplication()).userAccountPref.setValue("");
        goLoginPage();
    }

    @Override
    protected void goHomePage() {
        Intent intent = new Intent(BottomTabbarActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void goLoginPage() {
        Intent intent = new Intent(BottomTabbarActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void updateNavigationBar(String fragmentClass, String title) {
        setTitle(title);
    }
}
