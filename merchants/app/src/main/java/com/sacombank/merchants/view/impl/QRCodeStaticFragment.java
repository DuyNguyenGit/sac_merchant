package com.sacombank.merchants.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.sacombank.merchants.R;
import com.sacombank.merchants.SacombankApp;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.DaggerQrcodeStaticViewComponent;
import com.sacombank.merchants.injection.QrcodeStaticViewModule;
import com.sacombank.merchants.presenter.QrcodeStaticPresenter;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.util.Utils;
import com.sacombank.merchants.view.QrcodeStaticView;
import com.stb.api.bo.MerchantAccountLogin;

import javax.inject.Inject;

public abstract class QRCodeStaticFragment extends BaseFragment<QrcodeStaticPresenter, QrcodeStaticView> implements QrcodeStaticView {
    private static final String TAG = QRCodeStaticFragment.class.getSimpleName();

    @Inject
    PresenterFactory<QrcodeStaticPresenter> mPresenterFactory;
    private ImageView imgQR;

    // Your presenter is available using the mPresenter variable

    public QRCodeStaticFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qrcode_static, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgQR = (ImageView) view.findViewById(R.id.imgStatic);
        MerchantAccountLogin userInfo = new Gson().fromJson(((SacombankApp) getActivity().getApplication()).userAccountPref.getValue(),
                MerchantAccountLogin.class);
        String qrBase64 = userInfo.StaticQRData;
        Log.e(TAG, "onViewCreated: >>>" + qrBase64);
        imgQR.setImageBitmap(Utils.convertBase64StringToBitmap(qrBase64));
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerQrcodeStaticViewComponent.builder()
                .appComponent(parentComponent)
                .qrcodeStaticViewModule(new QrcodeStaticViewModule())
                .build()
                .inject(this);
    }

    @Override
    public void onBackPressed() {

    }

    @NonNull
    @Override
    protected PresenterFactory<QrcodeStaticPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }
}
