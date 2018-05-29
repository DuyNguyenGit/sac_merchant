package com.sacombank.merchants.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sacombank.merchants.R;
import com.sacombank.merchants.SacombankApp;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.DaggerHistoryViewComponent;
import com.sacombank.merchants.injection.HistoryViewModule;
import com.sacombank.merchants.presenter.HistoryPresenter;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.view.HistoryView;

import javax.inject.Inject;

public final class HistoryFragment extends BaseFragment<HistoryPresenter, HistoryView> implements HistoryView {
    @Inject
    PresenterFactory<HistoryPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String user = ((SacombankApp) getActivity().getApplication()).userAccountPref.getValue();
        if (user.isEmpty()) {
            ((BottomTabbarActivity) getActivity()).goLoginPage();
        } else {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_history, new HistoryListFragment());
            transaction.commit();
        }
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerHistoryViewComponent.builder()
                .appComponent(parentComponent)
                .historyViewModule(new HistoryViewModule())
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
    protected PresenterFactory<HistoryPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCommonUI(AccountManagerFragment.class.getSimpleName(), "Lịch sử giao dịch QR Code");
    }

}
