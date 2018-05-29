package com.sacombank.merchants.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sacombank.merchants.R;
import com.sacombank.merchants.adapter.AccountInfoAdapter;
import com.sacombank.merchants.api.ApiManager;
import com.sacombank.merchants.injection.AccountInfoViewModule;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.DaggerAccountInfoViewComponent;
import com.sacombank.merchants.presenter.AccountInfoPresenter;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.view.AccountInfoView;
import com.sacombank.merchants.widgets.dialog.DialogFactory;
import com.stb.api.bo.AppSessionHandshake;
import com.stb.api.bo.MerchantAccountInquiry;
import com.stb.api.listeners.ApiResponse;

import javax.inject.Inject;

public final class AccountInfoFragment extends BaseFragment<AccountInfoPresenter, AccountInfoView> implements AccountInfoView {
    @Inject
    PresenterFactory<AccountInfoPresenter> mPresenterFactory;

    RecyclerView rcvInfo;
    AccountInfoAdapter adapter;


    public AccountInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intiView(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        handleCallApi();
    }

    private void intiView(View view) {


        rcvInfo = (RecyclerView) view.findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        rcvInfo.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rcvInfo.getContext(),
                layoutManager.getOrientation());
        rcvInfo.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerAccountInfoViewComponent.builder()
                .appComponent(parentComponent)
                .accountInfoViewModule(new AccountInfoViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void handleCallApi() {
        if (mPresenter != null) {
            ((BottomTabbarActivity)getActivity()).showHideCover(true);
            mPresenter.getAccountInfo();
        }
    }

    @Override
    public void onBackPressed() {

    }

    @NonNull
    @Override
    protected PresenterFactory<AccountInfoPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public <T> void getAccountSuccess(final T result) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((BottomTabbarActivity)getActivity()).showHideCover(false);
                adapter = new AccountInfoAdapter(getActivity(), AccountInfoAdapter.getData((MerchantAccountInquiry) result));
                rcvInfo.setAdapter(adapter);
            }
        });

    }

    @Override
    public <T> void getAccountError(final T result) {
        if (result != null) {

            if (((MerchantAccountInquiry) result).RespCode != null && ((MerchantAccountInquiry) result).RespCode.equalsIgnoreCase(ApiManager.SESSION_TIMEOUT)) {

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

                final String errorMsg = ((MerchantAccountInquiry) result).Description;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((BottomTabbarActivity) getActivity()).showHideCover(false);
                        DialogFactory.createMessageDialogAccountInfo(getActivity(), TextUtils.isEmpty(errorMsg) ? "Không lấy được dữ liệu" : errorMsg, new DialogFactory.DialogListener.AccountInfoListener() {

                            @Override
                            public void getAccountInfoError() {
                                adapter = new AccountInfoAdapter(getActivity(), AccountInfoAdapter.getData((MerchantAccountInquiry) result));
                                rcvInfo.setAdapter(adapter);
                            }
                        });
                    }
                });

            }

        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((BottomTabbarActivity) getActivity()).showHideCover(false);
                    adapter = new AccountInfoAdapter(getActivity(), AccountInfoAdapter.getData((MerchantAccountInquiry) result));
                    rcvInfo.setAdapter(adapter);
                }
            });
        }

    }
}
