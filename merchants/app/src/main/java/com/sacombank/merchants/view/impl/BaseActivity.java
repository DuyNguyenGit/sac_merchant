package com.sacombank.merchants.view.impl;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sacombank.merchants.R;
import com.sacombank.merchants.SacombankApp;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.presenter.BasePresenter;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.loader.PresenterLoader;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BaseActivity<P extends BasePresenter<V>, V> extends AppCompatActivity implements LoaderManager.LoaderCallbacks<P>,BaseFragment.BaseListener, View.OnClickListener {
    private static final String TAG = BaseActivity.class.getSimpleName();
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
     * Is this the first start of the activity (after onCreate)
     */
    private boolean mFirstStart;

    private FrameLayout baseLayout;
    private FrameLayout coverLayout;
    private FrameLayout contentLayout;
    private RelativeLayout toolBar;
    private ImageView imgHome;
    private ImageView imgLock;


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        baseLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        coverLayout = (FrameLayout) baseLayout.findViewById(R.id.cover_layout_common);
        contentLayout = (FrameLayout) baseLayout.findViewById(R.id.content_layout);
        toolBar = (RelativeLayout) baseLayout.findViewById(R.id.toolBar);
        imgHome = (ImageView) baseLayout.findViewById(R.id.imgHome);
        imgLock = (ImageView) baseLayout.findViewById(R.id.imgLock);
        imgHome.setOnClickListener(this);
        imgLock.setOnClickListener(this);
        getLayoutInflater().inflate(layoutResID, contentLayout, true);
        super.setContentView(baseLayout);
    }

    protected void showToolBar(){
        toolBar.setVisibility(View.VISIBLE);
    }

    protected void hideToolBar(){
        toolBar.setVisibility(View.GONE);
    }

    protected void showIcon(){
        imgHome.setVisibility(View.VISIBLE);
        imgLock.setVisibility(View.VISIBLE);
    }

    protected void hideIcon(){
        imgHome.setVisibility(View.GONE);
        imgLock.setVisibility(View.GONE);
    }

    protected void showHideCover(boolean isLoading){
        int visible = isLoading ? View.VISIBLE : View.GONE;
        coverLayout.setVisibility(visible);
    }

    protected void hideHomeIcon() {
        imgHome.setVisibility(View.GONE);
    }

    protected void showHomeIcon() {
        imgHome.setVisibility(View.VISIBLE);
    }


    protected void hideLockIcon() {
        imgLock.setVisibility(View.GONE);
    }

    protected void showLockIcon() {
        imgLock.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TODO : Hide toolbar
//        if (Build.VERSION.SDK_INT < 16) {
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        } else {
////            View decorView = getWindow().getDecorView();
////            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
////            decorView.setSystemUiVisibility(uiOptions);
////            ActionBar actionBar = getSupportActionBar();
////            actionBar.hide();
//        }
        mFirstStart = true;

        injectDependencies();

        getSupportLoaderManager().initLoader(0, null, this).startLoading();
    }

    private void injectDependencies() {
        setupComponent(((SacombankApp) getApplication()).getAppComponent());
    }

    @Override
    protected void onStart() {
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
    protected void onStop() {
        if (mPresenter != null) {
            mPresenter.onStop();

            mPresenter.onViewDetached();
        }
        Log.e(TAG, "onStop: >>>===");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy: >>>===");
        super.onDestroy();
    }
    public static final long DISCONNECT_TIMEOUT = 5*60*1000; // 5 min = 5 * 60 * 1000 ms

    private Handler disconnectHandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "run: >>>===");
            forceLogout();
        }
    };

    public void resetDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.removeCallbacksAndMessages(null);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    @Override
    public void onUserInteraction(){
        Log.e(TAG, "onUserInteraction: >>>===");
        //resetDisconnectTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        //resetDisconnectTimer();
    }

    @Override
    public final Loader<P> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, getPresenterFactory());
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgHome:
                goHomePage();
                break;
            case R.id.imgLock:
                logout();
                break;
        }
    }

    protected abstract void logout();
    protected abstract void forceLogout();

    protected abstract void goHomePage();
    protected abstract void goLoginPage();

}
