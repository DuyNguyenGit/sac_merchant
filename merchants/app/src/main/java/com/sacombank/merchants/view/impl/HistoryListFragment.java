package com.sacombank.merchants.view.impl;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sacombank.merchants.R;
import com.sacombank.merchants.SacombankApp;
import com.sacombank.merchants.adapter.HistoryAdapter;
import com.sacombank.merchants.api.ApiManager;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.DaggerHistoryListViewComponent;
import com.sacombank.merchants.injection.HistoryListViewModule;
import com.sacombank.merchants.presenter.HistoryListPresenter;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.util.Utils;
import com.sacombank.merchants.view.HistoryListView;
import com.sacombank.merchants.widgets.dialog.DatePickerFragment;
import com.sacombank.merchants.widgets.dialog.DialogFactory;
import com.sacombank.merchants.widgets.recyclerviewlistener.EndlessRecyclerViewScrollListener;
import com.stb.api.bo.AppSessionHandshake;
import com.stb.api.bo.MerchantTransactionInquiry;
import com.stb.api.bo.TransactionObject;
import com.stb.api.listeners.ApiResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public final class HistoryListFragment extends BaseFragment<HistoryListPresenter, HistoryListView> implements HistoryListView, View.OnClickListener, View.OnTouchListener {
    private static final String TAG = HistoryListFragment.class.getSimpleName();
    @Inject
    PresenterFactory<HistoryListPresenter> mPresenterFactory;

    RecyclerView rcvHistory;
    HistoryAdapter adapter;
    LinearLayout btnSearch;
    EditText edtCard, edtDate;
    private List<TransactionObject> mListData = new ArrayList<>();
    private boolean isSearch;
    private String mSearchDate = "";
    boolean bIsStarted = false;
    private boolean isLoadMoreData = false;

    public HistoryListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private HistoryAdapter.HistoryItemClickListener mItemClickListener = new HistoryAdapter.HistoryItemClickListener() {

        @Override
        public void onItemClick(TransactionObject data, int pos) {
            nextDetail(data);
        }
    };

    private void nextDetail(TransactionObject data) {
        ((SacombankApp) getActivity().getApplication()).historyTransaction.setValue(new Gson().toJson(data));
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(R.id.frame_history, new HistoryDetailFragment());
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);
        trans.commit();
    }

    private void initView(View view) {
        rcvHistory = (RecyclerView) view.findViewById(R.id.lv_history);
        edtCard = (EditText) view.findViewById(R.id.edtCard);
        edtDate = (EditText) view.findViewById(R.id.edtDate);
        btnSearch = (LinearLayout) view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        edtDate.setOnTouchListener(this);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        rcvHistory.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rcvHistory.getContext(),
                layoutManager.getOrientation());
        rcvHistory.addItemDecoration(dividerItemDecoration);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);

            }
        };
        rcvHistory.addOnScrollListener(scrollListener);
        adapter = new HistoryAdapter(getActivity(), mListData, mItemClickListener);
        rcvHistory.setAdapter(adapter);
    }

    private void loadNextDataFromApi(int page) {
        String cardNo = edtCard.getText().toString();
        String date = edtDate.getText().toString();
        if (date.length() > 0 && date.length() < 8) {
            DialogFactory.createMessageDialog(getActivity(), "Vui lòng nhập đầy đủ ngày giao dịch.");
        } else {
            isLoadMoreData = true;
            mPresenter.getHistoryTransaction(cardNo, mSearchDate, page);
        }

    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerHistoryListViewComponent.builder()
                .appComponent(parentComponent)
                .historyListViewModule(new HistoryListViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void handleCallApi() {
        if (mPresenter != null) {
            ((BottomTabbarActivity) getActivity()).showHideCover(true);
            String cardNo = edtCard.getText().toString();
            mPresenter.getHistoryTransaction(cardNo, mSearchDate, 0);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: >>>");
        if (!bIsStarted) {
            validateInfo();
            bIsStarted = true;
        }
        String CanceledReferNo = getApp().flagHistoryReferNo.getValue();
        if (mListData.size() > 0 && !TextUtils.isEmpty(CanceledReferNo)) {

            for (int i = 0; i < mListData.size(); i++) {
                TransactionObject object = mListData.get(i);
                if (object.ReferenceNo.equalsIgnoreCase(CanceledReferNo)) {
                    object.Status = ApiManager.TransactionStatus.REVERSAL.toString();
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void validateInfo() {
        String cardNo = edtCard.getText().toString();
        String date = edtDate.getText().toString();
        if (date.length() > 0 && date.length() < 8) {
            DialogFactory.createMessageDialog(getActivity(), "Vui lòng nhập đầy đủ ngày giao dịch.");
        } else {
            handleCallApi();
        }
    }

    @Override
    public void onBackPressed() {

    }

    @NonNull
    @Override
    protected PresenterFactory<HistoryListPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public <T> void getHistorySuccess(final T result) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((BottomTabbarActivity) getActivity()).showHideCover(false);
                TransactionObject[] transactionObjects = ((MerchantTransactionInquiry) result).TransactionObject;
                if (transactionObjects != null && transactionObjects.length > 0) {
                    List<TransactionObject> nextList = Arrays.asList(((MerchantTransactionInquiry) result).TransactionObject);
                    if (isSearch) {// Search
                        mListData.clear();
                        mListData.addAll(nextList);
                        adapter.notifyDataSetChanged();
                    }
                    else{//get default and load more
                        mListData.addAll(nextList);
                        int curSize = adapter.getItemCount();
                        adapter.notifyItemRangeInserted(curSize, mListData.size() - 1);
                    }
                }
                else {
                    if (isSearch) {
                        mListData.clear();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Không tìm thấy dữ liệu !", Toast.LENGTH_SHORT).show();
                    }
                }
                isSearch = false;
            }
        });

    }

    @Override
    public <T> void getHistoryError(final T result) {
        isSearch = false;
        if (result != null) {

            if (((MerchantTransactionInquiry) result).RespCode != null && ((MerchantTransactionInquiry) result).RespCode.equalsIgnoreCase(ApiManager.SESSION_TIMEOUT)) {

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

                final String errorMsg = ((MerchantTransactionInquiry) result).Description;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ((BottomTabbarActivity) getActivity()).showHideCover(false);
                        if (!isLoadMoreData) {
                            TransactionObject[] transactionObjects = new TransactionObject[10];
                            adapter = new HistoryAdapter(getActivity(),
                                    new ArrayList<>(Arrays.asList((transactionObjects))), mItemClickListener);
                            rcvHistory.setAdapter(adapter);
                            DialogFactory.createMessageDialog(getActivity(), TextUtils.isEmpty(errorMsg) ? getString(R.string.dialog_not_found_data) : errorMsg);
                        }
                    }
                });

            }

        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((BottomTabbarActivity) getActivity()).showHideCover(false);
                    if (!isLoadMoreData) {
                        TransactionObject[] transactionObjects = new TransactionObject[10];
                        adapter = new HistoryAdapter(getActivity(),
                                new ArrayList<>(Arrays.asList((transactionObjects))), mItemClickListener);
                        rcvHistory.setAdapter(adapter);
                        DialogFactory.createMessageDialog(getActivity(), getString(R.string.dialog_not_found_data));
                    }

                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearch:
                hideKeyBoard(view);
                if (mPresenter != null) {
                    String cardNo = edtCard.getText().toString();
                    String date = edtDate.getText().toString();
                    isSearch = true;
                    isLoadMoreData = false;
                    handleCallApi();
                }
                break;
        }
    }

    private void hideKeyBoard(View view) {
        Log.e(TAG, "hideKeyBoard: ");
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showDatePickerDialog() {
        DialogFragment  newFragment = DatePickerFragment.newInstance(new DatePickerFragment.DatePickerListener() {
            @Override
            public void setDate(String date, String formatDate) {
                edtDate.setText(date);
                mSearchDate = formatDate;
            }
            @Override
            public void setOnCancel(String temp) {
                edtDate.setText(temp);
            }
        });
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            Log.e(TAG, "onTouch: >>>>");
            showDatePickerDialog();
            return true;
        }
        return false;
    }

}
