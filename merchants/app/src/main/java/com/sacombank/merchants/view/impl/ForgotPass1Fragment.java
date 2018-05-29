package com.sacombank.merchants.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import com.sacombank.merchants.injection.DaggerForgotPass1ViewComponent;
import com.sacombank.merchants.injection.ForgotPass1ViewModule;
import com.sacombank.merchants.presenter.ForgotPass1Presenter;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.view.ForgotPass1View;
import com.sacombank.merchants.widgets.dialog.DialogFactory;
import com.stb.api.bo.AppSessionHandshake;
import com.stb.api.bo.MerchantPasswordRetrieval;
import com.stb.api.listeners.ApiResponse;

import javax.inject.Inject;

public final class ForgotPass1Fragment extends BaseFragment<ForgotPass1Presenter, ForgotPass1View> implements ForgotPass1View, View.OnClickListener {
    private static final String TAG = ForgotPass1Fragment.class.getSimpleName();
    @Inject
    PresenterFactory<ForgotPass1Presenter> mPresenterFactory;

    EditText edtAccount;
    Button btnContinue;

    // Your presenter is available using the mPresenter variable

    public ForgotPass1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_pass1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart

        edtAccount = (EditText) view.findViewById(R.id.edtAccount);
        btnContinue = (Button) view.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(this);
    }


    private void nextFragment() {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        ForgotPass2Fragment fragment = new ForgotPass2Fragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(KEY, ForgotPass2Fragment);
//        fragment.setArguments(bundle);
        trans.replace(R.id.layout_forgot_pass, fragment);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        trans.addToBackStack(null);
        trans.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerForgotPass1ViewComponent.builder()
                .appComponent(parentComponent)
                .forgotPass1ViewModule(new ForgotPass1ViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void handleErrorApi() {
        ((LoginActivity) getActivity()).showHideCover(false);
        ((LoginActivity) getActivity()).gotoTab(0);
    }

    @Override
    protected void showErrorMessage(String errorMsg) {
        DialogFactory.createMessageDialogForgotPassword(getActivity(), errorMsg, new DialogFactory.DialogListener.ForgotPass1ErrorListener() {
            @Override
            public void forgotPassError() {
                ((LoginActivity) getActivity()).gotoTab(0);
            }
        });
    }

    @Override
    protected void handleCallApi() {
        if (mPresenter != null) {
            ((LoginActivity) getActivity()).showHideCover(true);
            mPresenter.forgotPass(edtAccount.getText().toString());
        }
    }

    @Override
    public void onBackPressed() {

    }

    @NonNull
    @Override
    protected PresenterFactory<ForgotPass1Presenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnContinue:
                if(edtAccount.getText().toString().equals("")){
                    DialogFactory.createMessageDialog(getActivity(), getString(R.string.forgotAccount));
                }else {
                    requestApi();
                }
                break;
        }
    }

    @Override
    public <T> void forgotPassSuccess(T result) {
        Log.e(TAG, "forgotPassSuccess: >>>" + new Gson().toJson(result));
        ((SacombankApp) getActivity().getApplication()).flagForgotPass.setValue(1);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((LoginActivity) getActivity()).showHideCover(false);
                ((LoginActivity) getActivity()).gotoTab(0);
            }
        });
    }

    @Override
    public <T> void forgotPassError(T result) {
        handleRequestApiError(result);
    }
}
