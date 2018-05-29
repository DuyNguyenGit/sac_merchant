package com.sacombank.merchants.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sacombank.merchants.R;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.DaggerQrcodeDynamicViewComponent;
import com.sacombank.merchants.injection.QrcodeDynamicViewModule;
import com.sacombank.merchants.presenter.QrcodeDynamicPresenter;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.view.QrcodeDynamicView;

import javax.inject.Inject;

public final class QRCodeDynamicFragment extends BaseFragment<QrcodeDynamicPresenter, QrcodeDynamicView> implements QrcodeDynamicView {
    @Inject
    PresenterFactory<QrcodeDynamicPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    public QRCodeDynamicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_qrcode_dynamic, container, false);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_qrcode_dynamic, new QRCodeDynamicInputFragment());
        transaction.commit();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerQrcodeDynamicViewComponent.builder()
                .appComponent(parentComponent)
                .qrcodeDynamicViewModule(new QrcodeDynamicViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void handleCallApi() {

    }

    @Override
    public void onBackPressed() {
    }

    @NonNull
    @Override
    protected PresenterFactory<QrcodeDynamicPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }
}
