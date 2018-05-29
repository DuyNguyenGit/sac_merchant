package com.sacombank.merchants.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.sacombank.merchants.R;
import com.sacombank.merchants.SacombankApp;
import com.sacombank.merchants.api.ApiManager;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.DaggerQrcodeDynamicInputViewComponent;
import com.sacombank.merchants.injection.QrcodeDynamicInputViewModule;
import com.sacombank.merchants.model.jsonobject.QRCodeInputData;
import com.sacombank.merchants.presenter.QrcodeDynamicInputPresenter;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.view.QrcodeDynamicInputView;
import com.sacombank.merchants.widgets.MoneyTextWatcher;
import com.sacombank.merchants.widgets.dialog.DialogFactory;
import com.stb.api.bo.AppSessionHandshake;
import com.stb.api.bo.MerchantQRGeneration;
import com.stb.api.listeners.ApiResponse;

import javax.inject.Inject;

import static com.sacombank.merchants.view.impl.QRCodeDynamicGenerateFragment.KEY;

public final class QRCodeDynamicInputFragment extends BaseFragment<QrcodeDynamicInputPresenter, QrcodeDynamicInputView> implements QrcodeDynamicInputView {
    @Inject
    PresenterFactory<QrcodeDynamicInputPresenter> mPresenterFactory;

    public static final int QRCodeDynamicInputFragment = 1;
    EditText edtTotal, edtTip, edtBill, edtDesc;

    public QRCodeDynamicInputFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_qrcode_dynamic_input, container, false);

        return view;
    }

    private void nextFragment() {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        QRCodeDynamicGenerateCreateFragment fragment = new QRCodeDynamicGenerateCreateFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY, QRCodeDynamicInputFragment);
        fragment.setArguments(bundle);
        trans.replace(R.id.frame_qrcode_dynamic, fragment);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);
        trans.commit();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View view) {
        edtBill = (EditText) view.findViewById(R.id.edtBillCount);
        edtDesc = (EditText) view.findViewById(R.id.edtDescription);
        edtTip = (EditText) view.findViewById(R.id.edtMoneyTip);
        edtTotal = (EditText) view.findViewById(R.id.edtMoneyTotal);

        edtTotal.addTextChangedListener(new MoneyTextWatcher(edtTotal, "#,###"));
        edtTip.addTextChangedListener(new MoneyTextWatcher(edtTip, "#,###"));

        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        Button btn = (Button) view.findViewById(R.id.btnComplete);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtTotal.getText().toString()) ||
                        TextUtils.isEmpty(edtBill.getText().toString())) {
                    DialogFactory.createMessageDialog(getActivity(), getResources().getString(R.string.dialog_message_input_info_full));
                    if (TextUtils.isEmpty(edtTotal.getText().toString())){
                        edtTotal.requestFocus();
                    }
                    else if (TextUtils.isEmpty(edtBill.getText().toString())){
                        edtBill.requestFocus();
                    }

                    return;
                }
                QRCodeInputData data = new QRCodeInputData(
                        edtTotal.getText().toString(),
                        edtTip.getText().toString(),
                        edtBill.getText().toString(),
                        edtDesc.getText().toString()
                );
                if (mPresenter != null) {
                    ((BottomTabbarActivity)getActivity()).showHideCover(true);
                    mPresenter.generateQRCode(data);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtBill.setText("");
                edtDesc.setText("");
                edtTip.setText("");
                edtTotal.setText("");
                edtTotal.requestFocus();
            }
        });
    }

    private void validateInfo() {
        if (TextUtils.isEmpty(edtTotal.getText().toString()) ||
                TextUtils.isEmpty(edtTip.getText().toString())) {
            DialogFactory.createMessageDialog(getActivity(), getResources().getString(R.string.dialog_message_input_info_full));
            return;
        }
        handleCallApi();
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerQrcodeDynamicInputViewComponent.builder()
                .appComponent(parentComponent)
                .qrcodeDynamicInputViewModule(new QrcodeDynamicInputViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void handleCallApi() {
        QRCodeInputData data = new QRCodeInputData(
                edtTotal.getText().toString(),
                edtTip.getText().toString(),
                edtBill.getText().toString(),
                edtDesc.getText().toString()
        );
        if (mPresenter != null) {
            ((BottomTabbarActivity)getActivity()).showHideCover(true);
            mPresenter.generateQRCode(data);
        }
    }

    @Override
    public void onBackPressed() {

    }

    @NonNull
    @Override
    protected PresenterFactory<QrcodeDynamicInputPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public <T> void generateQRCodeSuccess(T result) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((BottomTabbarActivity)getActivity()).showHideCover(false);
            }
        });
        ((SacombankApp) getActivity().getApplication()).qrCodeDynamicPref.setValue(new Gson().toJson(result));
        nextFragment();
    }

    @Override
    public <T> void generateQRCodeError(T result) {
        if (result != null) {

            if (((MerchantQRGeneration) result).RespCode != null && ((MerchantQRGeneration) result).RespCode.equalsIgnoreCase(ApiManager.SESSION_TIMEOUT)) {

                ApiManager.requestHandshake(new ApiResponse<AppSessionHandshake>() {
                    @Override
                    public void onSuccess(AppSessionHandshake result) {

                        if (userLogined())
                            gotoLogin();
                        else
                            handleCallApi();

                    }

                    @Override
                    public void onError(AppSessionHandshake error) {

                        showErrorHandshake();

                    }
                });
            } else {

                final String errorMsg = ((MerchantQRGeneration) result).Description;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((BottomTabbarActivity) getActivity()).showHideCover(false);
                        DialogFactory.createMessageDialog(getActivity(), TextUtils.isEmpty(errorMsg) ? getString(R.string.dialog_qrcode_create_error) : errorMsg);

                    }
                });

            }

        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((BottomTabbarActivity) getActivity()).showHideCover(false);
                    DialogFactory.createMessageDialog(getActivity(), getString(R.string.dialog_qrcode_create_error));
                }
            });
        }
    }
}
