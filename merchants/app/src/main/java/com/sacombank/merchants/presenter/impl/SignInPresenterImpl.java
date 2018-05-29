package com.sacombank.merchants.presenter.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.sacombank.merchants.api.ApiManager;
import com.sacombank.merchants.interactor.SignInInteractor;
import com.sacombank.merchants.model.jsonobject.LoginData;
import com.sacombank.merchants.presenter.SignInPresenter;
import com.sacombank.merchants.view.SignInView;
import com.stb.api.STBAppClient;
import com.stb.api.bo.MerchantAccountLogin;
import com.stb.api.crypto.TripleDESCrypto;
import com.stb.api.listeners.ApiResponse;

import javax.inject.Inject;

public final class SignInPresenterImpl extends BasePresenterImpl<SignInView> implements SignInPresenter {
    private static final String TAG = SignInPresenterImpl.class.getSimpleName();


    /**
     * The interactor
     */
    @NonNull
    private final SignInInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public SignInPresenterImpl(@NonNull SignInInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onStart(boolean viewCreated) {
        super.onStart(viewCreated);
        Log.e(TAG, "onStart: >>> view = " + mView);
        // Your code here. Your view is available using mView and will not be null until next onStop()
    }

    @Override
    public void onViewAttached(@NonNull SignInView view) {
        super.onViewAttached(view);
        Log.e(TAG, "onViewAttached: >>> view = " + mView);
    }

    @Override
    public void onStop() {
        // Your code here, mView will be null after this method until next onStart()
        Log.e(TAG, "onStop: >>> view = " + mView);
        super.onStop();
    }

    @Override
    public void onPresenterDestroyed() {
        /*
         * Your code here. After this method, your presenter (and view) will be completely destroyed
         * so make sure to cancel any HTTP call or database connection
         */
        Log.e(TAG, "onPresenterDestroyed: >>> view = " + mView);
        super.onPresenterDestroyed();
    }

    @Override
    public void login(LoginData loginData) {
        MerchantAccountLogin object = new MerchantAccountLogin();
        object.UserName = loginData.getUsername();
        object.Password = TripleDESCrypto.encrypt(loginData.getPassword(), STBAppClient.getSessionKey());
        object.FirebaseToken = loginData.getFirebaseToken();
        ApiManager.requestLogin(object, new ApiResponse<MerchantAccountLogin>() {
            @Override
            public void onSuccess(MerchantAccountLogin result) {
                Log.e(TAG, "onSuccess: login");

                // Goto Home page
                if (mView != null)
                    mView.loginSuccess(result);
            }

            @Override
            public void onError(MerchantAccountLogin error) {

                Log.e(TAG, "onError: >>>" + new Gson().toJson(error));
                if (mView != null) {
                    Log.e(TAG, "onError: login" + mView);
                    mView.loginError(error);
                }
            }
        });
    }
}