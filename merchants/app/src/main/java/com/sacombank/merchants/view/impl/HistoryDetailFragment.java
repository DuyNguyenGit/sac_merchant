package com.sacombank.merchants.view.impl;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sacombank.merchants.R;
import com.sacombank.merchants.SacombankApp;
import com.sacombank.merchants.api.ApiManager;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.DaggerHistoryDetailViewComponent;
import com.sacombank.merchants.injection.HistoryDetailViewModule;
import com.sacombank.merchants.presenter.HistoryDetailPresenter;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.util.Utils;
import com.sacombank.merchants.view.HistoryDetailView;
import com.sacombank.merchants.widgets.dialog.DialogFactory;
import com.stb.api.bo.AppSessionHandshake;
import com.stb.api.bo.MerchantTransactionReversal;
import com.stb.api.bo.TransactionObject;
import com.stb.api.listeners.ApiResponse;

import javax.inject.Inject;

public final class HistoryDetailFragment extends BaseFragment<HistoryDetailPresenter, HistoryDetailView> implements HistoryDetailView, View.OnClickListener {
    @Inject
    PresenterFactory<HistoryDetailPresenter> mPresenterFactory;
    public static final String TAG = HistoryDetailFragment.class.getSimpleName();

    Button btnCancel;
    TextView tvApprovedNo, tvMoneyNo, tvCardNo, tvAccountNo, tvOwner, tvTip, tvDesc;
    TransactionObject data;
    private String mPassword;

    public HistoryDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new Gson().fromJson(((SacombankApp) getActivity().getApplication()).historyTransaction.getValue(),
                TransactionObject.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        tvApprovedNo = (TextView) view.findViewById(R.id.tvApproveNoValue);
        tvMoneyNo = (TextView) view.findViewById(R.id.tvMoneyNoValue);
        tvCardNo = (TextView) view.findViewById(R.id.tvCardNoValue);
        tvOwner = (TextView) view.findViewById(R.id.tvOwnerNameValue);
        tvTip = (TextView) view.findViewById(R.id.tvTipNoValue);
        tvDesc = (TextView) view.findViewById(R.id.tvDescValue);
        boolean bIsEnable = data.Status.equals(ApiManager.TransactionStatus.SUCCESSFUl.toString()) && data.CheckReversal;
        btnCancel.setEnabled(bIsEnable);
        if (!btnCancel.isEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                btnCancel.setBackground(getResources().getDrawable(R.drawable.bg_button_disable));
            } else {
                btnCancel.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_button_disable));

            }
            btnCancel.setTextColor(getResources().getColor(R.color.disable_button_text_color));
        }
        tvApprovedNo.setText(data.ApprovalCode);
        tvMoneyNo.setText(Utils.formatMoney(data.Amount));
        tvCardNo.setText(data.MaskCardNumber);
        tvOwner.setText(data.CardHolderName);
        tvTip.setText(data.Tips);
        tvDesc.setText(data.Description);
    }


    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerHistoryDetailViewComponent.builder()
                .appComponent(parentComponent)
                .historyDetailViewModule(new HistoryDetailViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void handleCallApi() {
        if (mPresenter != null) {
            ((BottomTabbarActivity) getActivity()).showHideCover(true);
            mPresenter.cancelTransaction(data, mPassword);
        }
    }

    @Override
    public void onBackPressed() {
//        FragmentManager fragmentManager = getFragmentManager();
//        if (fragmentManager.getBackStackEntryCount() > 0) {
//            fragmentManager.popBackStack();
//        }
    }

    @NonNull
    @Override
    protected PresenterFactory<HistoryDetailPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCancel:
                DialogFactory.createPaswordInputDialog(getActivity(), getString(R.string.dialog_message_password),
                        new DialogFactory.DialogListener.PasswordInputListener() {
                            @Override
                            public void setPassword(String password) {
                                mPassword = password;
                                handleCallApi();
                            }
                        });
                break;
        }

    }


    private void backToHistory() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
    }


    @Override
    public <T> void cancelTransactionSuccess(final T result) {
        if (result != null) {

            final String successMessage = ((MerchantTransactionReversal) result).Description.isEmpty() ? "Hủy giao dịch thành công" : ((MerchantTransactionReversal) result).Description;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((BottomTabbarActivity) getActivity()).showHideCover(false);
                    DialogFactory.createCancelTransactionDialogWithListener(getActivity(), successMessage,
                            new DialogFactory.DialogListener.CancelTransactionSuccessListener() {
                                @Override
                                public void cancelTransactionSuccess() {
                                    storeReferenceNo(((MerchantTransactionReversal) result).ReferenceNo);
                                    backToHistory();
                                }
                            });
                }
            });
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final String errorMsg = "Hủy giao dịch không thành công";
                    ((BottomTabbarActivity) getActivity()).showHideCover(false);
                    DialogFactory.createCancelTransactionDialogWithListener(getActivity(), errorMsg,
                            new DialogFactory.DialogListener.CancelTransactionSuccessListener() {
                                @Override
                                public void cancelTransactionSuccess() {
                                    clearReferenceNoInStore();
                                    backToHistory();
                                }
                            });
                }
            });
        }
    }

    @Override
    public <T> void cancelTransactionError(T result) {
        clearReferenceNoInStore();
        if (result != null) {

            if (((MerchantTransactionReversal) result).RespCode != null && ((MerchantTransactionReversal) result).RespCode.equalsIgnoreCase(ApiManager.SESSION_TIMEOUT)) {

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

                final String errorMsg = ((MerchantTransactionReversal) result).Description;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ((BottomTabbarActivity) getActivity()).showHideCover(false);
                        DialogFactory.createCancelTransactionDialogWithListener(getActivity(), TextUtils.isEmpty(errorMsg) ? "Hủy giao dịch thất bại" : errorMsg,
                                new DialogFactory.DialogListener.CancelTransactionSuccessListener() {
                                    @Override
                                    public void cancelTransactionSuccess() {
                                        backToHistory();
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
                    final String errorMsg = "Hủy giao dịch thất bại";
                    DialogFactory.createCancelTransactionDialogWithListener(getActivity(), errorMsg,
                            new DialogFactory.DialogListener.CancelTransactionSuccessListener() {
                                @Override
                                public void cancelTransactionSuccess() {
                                    backToHistory();
                                }
                            });
                }
            });
        }
    }

    private void clearReferenceNoInStore() {
        getApp().flagHistoryReferNo.setValue("");
    }
    private void storeReferenceNo(String referNo) {
        getApp().flagHistoryReferNo.setValue(referNo);
    }
}
