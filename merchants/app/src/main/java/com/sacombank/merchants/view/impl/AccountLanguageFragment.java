package com.sacombank.merchants.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.sacombank.merchants.R;
import com.sacombank.merchants.view.AccountLanguageView;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.AccountLanguagePresenter;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.AccountLanguageViewModule;
import com.sacombank.merchants.injection.DaggerAccountLanguageViewComponent;
import com.sacombank.merchants.widgets.dialog.DialogFactory;

import javax.inject.Inject;

public final class AccountLanguageFragment extends BaseFragment<AccountLanguagePresenter, AccountLanguageView> implements AccountLanguageView {
    @Inject
    PresenterFactory<AccountLanguagePresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable
    Switch swVN;


    public AccountLanguageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_language, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swVN = (Switch) view.findViewById(R.id.VNSwitch);
        swVN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    swVN.setChecked(true);
                    //DialogFactory.createMessageDialog(getActivity(), getActivity().getResources().getString(R.string.language_dialog_msg));
                }
            }
        });
        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerAccountLanguageViewComponent.builder()
                .appComponent(parentComponent)
                .accountLanguageViewModule(new AccountLanguageViewModule())
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
    protected PresenterFactory<AccountLanguagePresenter> getPresenterFactory() {
        return mPresenterFactory;
    }
}
