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

import com.sacombank.merchants.R;
import com.sacombank.merchants.api.ApiManager;
import com.sacombank.merchants.injection.AccountPasswordViewModule;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.DaggerAccountPasswordViewComponent;
import com.sacombank.merchants.model.jsonobject.PasswordData;
import com.sacombank.merchants.presenter.AccountPasswordPresenter;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.view.AccountPasswordView;
import com.sacombank.merchants.widgets.dialog.DialogFactory;
import com.stb.api.bo.AppSessionHandshake;
import com.stb.api.bo.MerchantPasswordChange;
import com.stb.api.listeners.ApiResponse;

import javax.inject.Inject;

public final class AccountPasswordFragment extends BaseFragment<AccountPasswordPresenter, AccountPasswordView> implements AccountPasswordView {
    @Inject
    PresenterFactory<AccountPasswordPresenter> mPresenterFactory;
    private Button btnChangePass;
    private EditText edtOld;
    private EditText edtNew1;
    private EditText edtNew2;


    public AccountPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtOld = (EditText) view.findViewById(R.id.edtPasswordOld);
        edtNew1 = (EditText) view.findViewById(R.id.edtPasswordNew1);
        edtNew2 = (EditText) view.findViewById(R.id.edtPasswordNew2);
        btnChangePass = (Button) view.findViewById(R.id.btnChangePassword);
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatePassword();
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        edtNew1.setText("");
        edtNew2.setText("");
        edtOld.setText("");
        edtOld.requestFocus();
    }

    private void validatePassword() {
        if (edtNew1.getText().toString().trim().equals(edtNew2.getText().toString().trim())) {
            handleCallApi();
        } else {
            DialogFactory.createMessageDialog(getActivity(), getString(R.string.dialogcheckpass));
        }
    }


    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerAccountPasswordViewComponent.builder()
                .appComponent(parentComponent)
                .accountPasswordViewModule(new AccountPasswordViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void handleCallApi() {
        if (mPresenter != null) {
            ((BottomTabbarActivity) getActivity()).showHideCover(true);
            mPresenter.changePassword(new PasswordData(edtOld.getText().toString(), edtNew1.getText().toString()));

        }
    }

    @Override
    public void onBackPressed() {

    }

    @NonNull
    @Override
    protected PresenterFactory<AccountPasswordPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public <T> void changePassSuccess(T result) {

        final String description = ((MerchantPasswordChange) result).Description;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((BottomTabbarActivity) getActivity()).showHideCover(false);
                DialogFactory.createMessageDialog(getActivity(), TextUtils.isEmpty(description) ? "Thay đổi mật khẩu thành công" : description);
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
                        ((BottomTabbarActivity) getActivity()).showHideCover(false);
                        DialogFactory.createMessageDialog(getActivity(), errorMsg);
                    }
                });

            }

        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((BottomTabbarActivity) getActivity()).showHideCover(false);
                }
            });
        }
    }
}
