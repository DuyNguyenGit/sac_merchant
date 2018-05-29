package com.sacombank.merchants.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sacombank.merchants.R;
import com.sacombank.merchants.SacombankApp;
import com.sacombank.merchants.api.ApiManager;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.presenter.BasePresenter;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.loader.PresenterLoader;
import com.sacombank.merchants.util.Utils;
import com.sacombank.merchants.widgets.dialog.DialogFactory;
import com.stb.api.bo.AppSessionHandshake;
import com.stb.api.bo.MerchantPasswordRetrieval;
import com.stb.api.listeners.ApiResponse;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BaseFragment<P extends BasePresenter<V>, V> extends Fragment implements LoaderManager.LoaderCallbacks<P> {
    /**
     * Do we need to call {@link #doStart()} from the {@link #onLoadFinished(Loader, BasePresenter)} method.
     * Will be true if presenter wasn't loaded when {@link #onStart()} is reached
     */
    private final AtomicBoolean mNeedToCallStart = new AtomicBoolean(false);
    /**
     * The presenter for this view
     */
    @Nullable
    protected P mPresenter;
    /**
     * Is this the first start of the fragment (after onCreate)
     */
    private boolean mFirstStart;

    //--------------------------
    public void setBaseListener(BaseListener listener) {
        this.listener = listener;
    }

    BaseListener listener;

    public interface BaseListener {
        void updateNavigationBar(String fragmentClass, String title);
    }

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

    protected void updateCommonUI(String fragmentClass, String title) {
        this.listener.updateNavigationBar(fragmentClass, title);
    }
    //--------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirstStart = true;

        injectDependencies();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(0, null, this).startLoading();
    }

    private void injectDependencies() {
        setupComponent(((SacombankApp) getActivity().getApplication()).getAppComponent());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPresenter == null) {
            mNeedToCallStart.set(true);
        } else {
            doStart();
        }
    }

    /**
     * Call the presenter callbacks for onStart
     */
    @SuppressWarnings("unchecked")
    private void doStart() {
        assert mPresenter != null;

        mPresenter.onViewAttached((V) this);

        mPresenter.onStart(mFirstStart);

        mFirstStart = false;
    }

    @Override
    public void onStop() {
        if (mPresenter != null) {
            mPresenter.onStop();

            mPresenter.onViewDetached();
        }

        super.onStop();
    }

    @Override
    public final Loader<P> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(getActivity(), getPresenterFactory());
    }

    @Override
    public final void onLoadFinished(Loader<P> loader, P presenter) {
        mPresenter = presenter;

        if (mNeedToCallStart.compareAndSet(true, false)) {
            doStart();
        }
    }

    @Override
    public final void onLoaderReset(Loader<P> loader) {
        mPresenter = null;
    }

    /**
     * Get the presenter factory implementation for this view
     *
     * @return the presenter factory
     */
    @NonNull
    protected abstract PresenterFactory<P> getPresenterFactory();

    /**
     * Setup the injection component for this view
     *
     * @param appComponent the app component
     */
    protected abstract void setupComponent(@NonNull AppComponent appComponent);

    protected SacombankApp getApp() {
        return (SacombankApp) getActivity().getApplication();
    }

    protected boolean userLogined() {
        String loginedUser = getApp().userAccountPref.getValue();
        return !TextUtils.isEmpty(loginedUser);
    }

    protected void logout(){
        getApp().userAccountPref.setValue("");
    }

    protected void gotoLogin() {
        logout();
        ((BaseActivity) getActivity()).showHideCover(false);
        ((BaseActivity) getActivity()).goLoginPage();
    }

    protected void requestApi() {
        if (Utils.isNetworkAvailable(this.getActivity())) {
            handleCallApi();
        } else {
            DialogFactory.createMessageDialog(this.getActivity(), getString(R.string.dialog_not_internet));
        }
    }

    protected void showErrorHandshake() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO : hide Loading
                ((BaseActivity) getActivity()).showHideCover(false);
                DialogFactory.createTryAgainDialog(getActivity(), "Yêu cầu vượt quá thời gian quy định, bạn vui lòng thử lại",
                        new DialogFactory.DialogListener.RetryListener() {
                            @Override
                            public void retry() {
                                ApiManager.requestHandshake(new ApiResponse<AppSessionHandshake>() {
                                    @Override
                                    public void onSuccess(AppSessionHandshake result) {
                                        requestApi();
                                    }

                                    @Override
                                    public void onError(AppSessionHandshake error) {
                                        showErrorHandshake();
                                    }
                                });
                            }
                        });
            }
        });
    }

    protected <T> void handleRequestApiError(T error) {
        if (error != null) {

            if (((MerchantPasswordRetrieval) error).RespCode != null && ((MerchantPasswordRetrieval) error).RespCode.equalsIgnoreCase(ApiManager.SESSION_TIMEOUT)) {

                ApiManager.requestHandshake(new ApiResponse<AppSessionHandshake>() {
                    @Override
                    public void onSuccess(AppSessionHandshake result) {

                        if (userLogined()) {
                            // Login again
                            gotoLogin();
                        } else {
                            // Tiếp tục gọi API
                            requestApi();
                        }

                    }

                    @Override
                    public void onError(AppSessionHandshake error) {

                        showErrorHandshake();

                    }
                });
            } else {

                final String errorMsg = ((MerchantPasswordRetrieval) error).Description;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingPage();
                        showErrorMessage(errorMsg);

                    }
                });

            }

        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideLoadingPage();
                    handleErrorApi();
                }
            });
        }
    }

    protected abstract void handleErrorApi();


    protected void showLoadingPage() {
        if (getActivity() != null) {
            if (getActivity() instanceof LoginActivity) {
                ((LoginActivity) getActivity()).showHideCover(true);
            } else if (getActivity() instanceof BottomTabbarActivity) {
                ((BottomTabbarActivity) getActivity()).showHideCover(true);
            } else if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showHideCover(true);
            }
        }
    }

    protected void hideLoadingPage() {
        if (getActivity() != null) {
            if (getActivity() instanceof LoginActivity) {
                ((LoginActivity) getActivity()).showHideCover(false);
            } else if (getActivity() instanceof BottomTabbarActivity) {
                ((BottomTabbarActivity) getActivity()).showHideCover(false);
            } else if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showHideCover(false);
            }
        }
    }


    protected abstract void showErrorMessage(String errorMsg);

    protected abstract void handleCallApi();

    public abstract void onBackPressed();

}
