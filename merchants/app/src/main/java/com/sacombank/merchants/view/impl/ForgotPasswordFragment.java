package com.sacombank.merchants.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sacombank.merchants.R;
import com.sacombank.merchants.SacombankApp;
import com.sacombank.merchants.view.ForgotPasswordView;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.ForgotPasswordPresenter;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.ForgotPasswordViewModule;
import com.sacombank.merchants.injection.DaggerForgotPasswordViewComponent;

import javax.inject.Inject;

public final class ForgotPasswordFragment extends BaseFragment<ForgotPasswordPresenter, ForgotPasswordView> implements ForgotPasswordView {
    @Inject
    PresenterFactory<ForgotPasswordPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart

    }


    public void navigateToScreen() {
        if (((SacombankApp) getActivity().getApplication()).flagForgotPass2.getValue() == 0) {
            nextFragment(new ForgotPass1Fragment());
        } else {
            nextFragment(new ForgotPass2Fragment());
        }
    }

    private void nextFragment(BaseFragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_forgot_pass, fragment);
        transaction.commit();
    }


    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerForgotPasswordViewComponent.builder()
                .appComponent(parentComponent)
                .forgotPasswordViewModule(new ForgotPasswordViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void handleCallApi() {

    }

    @Override
    public void onBackPressed() {
        getActivity().finish();
    }

    @NonNull
    @Override
    protected PresenterFactory<ForgotPasswordPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }
}
