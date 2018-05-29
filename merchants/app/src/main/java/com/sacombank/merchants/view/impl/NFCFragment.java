package com.sacombank.merchants.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sacombank.merchants.R;
import com.sacombank.merchants.view.NfcView;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.NfcPresenter;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.NfcViewModule;
import com.sacombank.merchants.injection.DaggerNfcViewComponent;

import javax.inject.Inject;

public abstract class NFCFragment extends BaseFragment<NfcPresenter, NfcView> implements NfcView {
    @Inject
    PresenterFactory<NfcPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    public NFCFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nfc, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerNfcViewComponent.builder()
                .appComponent(parentComponent)
                .nfcViewModule(new NfcViewModule())
                .build()
                .inject(this);
    }

    @Override
    public void onBackPressed() {

    }

    @NonNull
    @Override
    protected PresenterFactory<NfcPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }
}
