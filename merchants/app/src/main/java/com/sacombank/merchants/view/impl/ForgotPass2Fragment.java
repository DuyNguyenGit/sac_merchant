package com.sacombank.merchants.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sacombank.merchants.R;
import com.sacombank.merchants.SacombankApp;
import com.sacombank.merchants.api.ApiManager;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.DaggerForgotPass2ViewComponent;
import com.sacombank.merchants.injection.ForgotPass2ViewModule;
import com.sacombank.merchants.model.jsonobject.PasswordData;
import com.sacombank.merchants.presenter.ForgotPass2Presenter;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.view.ForgotPass2View;
import com.sacombank.merchants.widgets.dialog.DialogFactory;
import com.stb.api.bo.AppSessionHandshake;
import com.stb.api.bo.MerchantPasswordChange;
import com.stb.api.listeners.ApiResponse;

import javax.inject.Inject;

public final class ForgotPass2Fragment extends BaseFragment<ForgotPass2Presenter, ForgotPass2View> implements ForgotPass2View, View.OnClickListener {
    @Inject
    PresenterFactory<ForgotPass2Presenter> mPresenterFactory;

    EditText edtPass1, edtPass2;
    Button btnConfirm;
    TextView tvSkip;
    // Your presenter is available using the mPresenter variable

    public ForgotPass2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_pass2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart

        edtPass1 = (EditText) view.findViewById(R.id.edtPass1);
        edtPass2 = (EditText) view.findViewById(R.id.edtPass2);
        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        tvSkip = (TextView) view.findViewById(R.id.tvSkip);

        btnConfirm.setOnClickListener(this);
        tvSkip.setOnClickListener(this);
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerForgotPass2ViewComponent.builder()
                .appComponent(parentComponent)
                .forgotPass2ViewModule(new ForgotPass2ViewModule())
                .build()
                .inject(this);
    }


    private void validatePassword() {
        if (edtPass1.getText().toString().trim().equals(edtPass2.getText().toString().trim()) ){
            handleCallApi();
        }else {
            DialogFactory.createMessageDialog(getActivity(), "Mật khẩu lần 2 không khớp mật khẩu lần 1, vui lòng nhập lại");
        }
    }

    @Override
    protected void handleCallApi() {
        if (mPresenter != null) {
            ((LoginActivity) getActivity()).showHideCover(true);
            mPresenter.changePassword(
                    new PasswordData(
                            ((SacombankApp) getActivity().getApplication()).oldPassword.getValue(),
                            edtPass1.getText().toString()));

        }
    }

    @Override
    public void onBackPressed() {

    }

    @NonNull
    @Override
    protected PresenterFactory<ForgotPass2Presenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                validatePassword();

                break;
            case R.id.tvSkip:
                clearFlagsInMemory();
                gotoHomePage();
                break;
        }
    }

    private void clearFlagsInMemory() {
        ((SacombankApp) getActivity().getApplication()).flagForgotPass.setValue(0);
        ((SacombankApp) getActivity().getApplication()).flagForgotPass2.setValue(0);
    }

    private void gotoHomePage() {
        ((LoginActivity) getActivity()).goHomePage();
    }

    @Override
    public <T> void changePassSuccess(T result) {
        final String description = ((MerchantPasswordChange) result).Description;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((LoginActivity)getActivity()).showHideCover(false);
                DialogFactory.createMessageDialogWithListener(getActivity(), TextUtils.isEmpty(description) ? "Thay đổi mật khẩu thành công" : description,
                        new DialogFactory.DialogListener.ForgotPassSuccessListener() {
                            @Override
                            public void forgotPassSuccess() {
                                clearFlagsInMemory();
                                gotoHomePage();
                            }
                        });
            }
        });
    }

    @Override
    public <T> void changePassError(T result) {
        if (result != null) {

            if (((MerchantPasswordChange) result).RespCode != null && ((MerchantPasswordChange) result).RespCode.equalsIgnoreCase(ApiManager.SESSION_TIMEOUT)) {

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

                final String errorMsg = ((MerchantPasswordChange) result).Description;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((LoginActivity) getActivity()).showHideCover(false);
                        DialogFactory.createMessageDialog(getActivity(), errorMsg);
                    }
                });

            }

        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((LoginActivity) getActivity()).showHideCover(false);
                }
            });
        }
    }
}
