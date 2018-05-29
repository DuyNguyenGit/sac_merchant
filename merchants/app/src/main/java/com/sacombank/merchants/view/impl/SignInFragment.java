package com.sacombank.merchants.view.impl;

import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sacombank.merchants.R;
import com.sacombank.merchants.SacombankApp;
import com.sacombank.merchants.api.ApiManager;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.DaggerSignInViewComponent;
import com.sacombank.merchants.injection.SignInViewModule;
import com.sacombank.merchants.model.jsonobject.LoginData;
import com.sacombank.merchants.presenter.SignInPresenter;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.view.SignInView;
import com.sacombank.merchants.widgets.dialog.DialogFactory;
import com.stb.api.STBAppClient;
import com.stb.api.bo.AppSessionHandshake;
import com.stb.api.bo.MerchantAccountLogin;
import com.stb.api.bo.MerchantQRGeneration;
import com.stb.api.listeners.ApiResponse;

import javax.inject.Inject;

public final class SignInFragment extends BaseFragment<SignInPresenter, SignInView> implements SignInView, View.OnClickListener {
    @Inject
    PresenterFactory<SignInPresenter> mPresenterFactory;

    public static final String TAG = SignInFragment.class.getSimpleName();

    AutoCompleteTextView edtUsername;
    EditText edtPassword;
    Button btnLogin;
    TextView tvSkip;
    private FragmentListener listener;


    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View view) {
        edtUsername = (AutoCompleteTextView) view.findViewById(R.id.edtEmail);
        edtPassword = (EditText) view.findViewById(R.id.edtPassword);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        tvSkip = (TextView) view.findViewById(R.id.tvSkip);

        btnLogin.setOnClickListener(this);
        tvSkip.setOnClickListener(this);
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerSignInViewComponent.builder()
                .appComponent(parentComponent)
                .signInViewModule(new SignInViewModule())
                .build()
                .inject(this);
    }


    @Override
    public void onBackPressed() {
        getActivity().finish();
    }

    @NonNull
    @Override
    protected PresenterFactory<SignInPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                if(edtUsername.getText().toString().equals("") || edtPassword.getText().toString().equals("")){
                    DialogFactory.createMessageDialog(getActivity(), getString(R.string.signinotvaluel));
                }else {
                    handleCallApi();
                }
                break;
            case R.id.tvSkip:
                if (getActivity() != null)
                    ((LoginActivity) getActivity()).goHomePage();
                break;
        }
    }


    @Override
    protected void handleCallApi() {
        login();
    }

    private void login() {
        if (mPresenter != null) {
            ((LoginActivity) getActivity()).showHideCover(true);
            String firebaseToken = ((SacombankApp) getActivity().getApplication()).firebaseTokenPref.getValue();
            mPresenter.login(new LoginData(edtUsername.getText().toString(), edtPassword.getText().toString(), firebaseToken));
        }
    }

    @Override
    public <T> void loginSuccess(T result) {
        Log.e(TAG, "loginSuccess: >>>" + new Gson().toJson(result));
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((LoginActivity) getActivity()).showHideCover(false);
            }
        });
        //go to Home page
        this.listener.goHomePageWithData(result);
    }

    @Override
    public <T> void loginError(T result) {
        if (result != null) {
            if (((MerchantAccountLogin) result).RespCode != null && ((MerchantAccountLogin) result).RespCode.equalsIgnoreCase(ApiManager.SESSION_TIMEOUT)) {
                ApiManager.requestHandshake(new ApiResponse<AppSessionHandshake>() {
                    @Override
                    public void onSuccess(AppSessionHandshake result) {
                        login();
                    }

                    @Override
                    public void onError(AppSessionHandshake error) {
                        showErrorHandshake();
                    }
                });
            } else {
                final String errorMsg = ((MerchantAccountLogin) result).Description;
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


    public void setListener(LoginActivity listener) {
        this.listener = listener;
    }
}
