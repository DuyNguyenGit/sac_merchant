package com.sacombank.merchants.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sacombank.merchants.R;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.DaggerMobilePosViewComponent;
import com.sacombank.merchants.injection.MobilePosViewModule;
import com.sacombank.merchants.presenter.MobilePosPresenter;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.view.MobilePosView;
import com.sacombank.merchants.widgets.dialog.DialogFactory;

import javax.inject.Inject;

public abstract class MobilePosFragment extends BaseFragment<MobilePosPresenter, MobilePosView> implements MobilePosView {
    @Inject
    PresenterFactory<MobilePosPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    public MobilePosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mobile_pos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DialogFactory.createMessageDialog(getActivity(), getString(R.string.notify_update_mobilepos));

    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerMobilePosViewComponent.builder()
                .appComponent(parentComponent)
                .mobilePosViewModule(new MobilePosViewModule())
                .build()
                .inject(this);
    }

    @Override
    public void onBackPressed() {

        ((BottomTabbarActivity) getActivity()).goHomePage();
    }

    @NonNull
    @Override
    protected PresenterFactory<MobilePosPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }
}
