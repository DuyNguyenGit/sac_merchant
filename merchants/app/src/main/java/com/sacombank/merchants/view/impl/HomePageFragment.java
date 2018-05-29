package com.sacombank.merchants.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sacombank.merchants.R;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.DaggerHomePageViewComponent;
import com.sacombank.merchants.injection.HomePageViewModule;
import com.sacombank.merchants.presenter.HomePagePresenter;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.view.HomePageView;

import javax.inject.Inject;

public abstract class HomePageFragment extends BaseFragment<HomePagePresenter, HomePageView> implements HomePageView {
    @Inject
    PresenterFactory<HomePagePresenter> mPresenterFactory;
    private View mRootView;

    // Your presenter is available using the mPresenter variable

    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_home_page, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerHomePageViewComponent.builder()
                .appComponent(parentComponent)
                .homePageViewModule(new HomePageViewModule())
                .build()
                .inject(this);
    }

    @Override
    public void onBackPressed() {

    }

    @NonNull
    @Override
    protected PresenterFactory<HomePagePresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

}
