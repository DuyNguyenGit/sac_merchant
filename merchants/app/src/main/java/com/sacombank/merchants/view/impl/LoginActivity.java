package com.sacombank.merchants.view.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.google.gson.Gson;
import com.sacombank.merchants.R;
import com.sacombank.merchants.SacombankApp;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.DaggerLoginViewComponent;
import com.sacombank.merchants.injection.LoginViewModule;
import com.sacombank.merchants.presenter.LoginPresenter;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.view.LoginView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public final class LoginActivity extends BaseActivity<LoginPresenter, LoginView> implements LoginView, FragmentListener {
    @Inject
    PresenterFactory<LoginPresenter> mPresenterFactory;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private int currentPage;
    private String TAG = LoginActivity.class.getSimpleName();

    @Override
    public void onBackPressed() {
        goHomePage();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        showToolBar();
        initView();
        hideLockIcon();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }
    }


    private void setupViewPager(final ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        SignInFragment signInFragment = new SignInFragment();
        signInFragment.setListener(this);
        adapter.addFragment(signInFragment, getString(R.string.login_title));
        adapter.addFragment(new ForgotPasswordFragment(), getString(R.string.forgot_pass_title));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                 BaseFragment currentFragment = (BaseFragment) adapter.getItem(position);
                if (currentFragment instanceof ForgotPasswordFragment) {
                    ((ForgotPasswordFragment) currentFragment).navigateToScreen();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerLoginViewComponent.builder()
                .appComponent(parentComponent)
                .loginViewModule(new LoginViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void logout() {

    }

    @Override
    protected void forceLogout() {

    }


    @Override
    public void goHomePage() {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void goLoginPage() {
        gotoTab(0);
    }

    @NonNull
    @Override
    protected PresenterFactory<LoginPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public <T> void goHomePageWithData(T data) {
        Log.e(TAG, "goHomePageWithData: data login success !!!!!!");

        ((SacombankApp) getApplication()).userAccountPref.setValue(new Gson().toJson(data));
        goHomePage();
    }

    @Override
    public void gotoChangePassword(String oldPassword) {
        ((SacombankApp) getApplication()).flagForgotPass2.setValue(1);
        ((SacombankApp) getApplication()).oldPassword.setValue(oldPassword);
        gotoTab(1);
    }

    public void gotoTab(final int pos) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TabLayout.Tab tab = tabLayout.getTabAt(pos);
                tab.select();
            }
        });
    }

    @Override
    public void updateNavigationBar(String fragmentClass, String title) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
