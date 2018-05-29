package com.sacombank.merchants.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sacombank.merchants.R;
import com.sacombank.merchants.view.IntroduceView;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.IntroducePresenter;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.IntroduceViewModule;
import com.sacombank.merchants.injection.DaggerIntroduceViewComponent;

import javax.inject.Inject;

public final class IntroduceFragment extends BaseFragment<IntroducePresenter, IntroduceView> implements IntroduceView {
    @Inject
    PresenterFactory<IntroducePresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    public IntroduceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_introduce, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerIntroduceViewComponent.builder()
                .appComponent(parentComponent)
                .introduceViewModule(new IntroduceViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void handleCallApi() {

    }

    @Override
    public void onBackPressed() {
        ((BottomTabbarActivity) getActivity()).goHomePage();
    }

    @NonNull
    @Override
    protected PresenterFactory<IntroducePresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCommonUI(AccountManagerFragment.class.getSimpleName(), "Giới thiệu");
    }

}
