package com.sacombank.merchants.view.impl;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sacombank.merchants.R;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.DaggerGuidelineViewComponent;
import com.sacombank.merchants.injection.GuidelineViewModule;
import com.sacombank.merchants.presenter.GuidelinePresenter;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.view.GuidelineView;

import javax.inject.Inject;

public final class GuidelineFragment extends BaseFragment<GuidelinePresenter, GuidelineView> implements GuidelineView {
    @Inject
    PresenterFactory<GuidelinePresenter> mPresenterFactory;
    private WebView webView;

    // Your presenter is available using the mPresenter variable

    public GuidelineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guideline, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webView = (WebView) view.findViewById(R.id.webview);
        webView.setWebViewClient(new SCBWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        String pdfURL = "https://www.sacombank.com.vn/company/Documents/TestApp/HDSD_DVCNT.pdf";
        webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdfURL);
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerGuidelineViewComponent.builder()
                .appComponent(parentComponent)
                .guidelineViewModule(new GuidelineViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void handleCallApi() {

    }

    @Override
    public void onBackPressed() {

        ((BottomTabbarActivity) getActivity()).goHomePage();
    }

    @NonNull
    @Override
    protected PresenterFactory<GuidelinePresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    private class SCBWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCommonUI(AccountManagerFragment.class.getSimpleName(), "Hướng dẫn");
    }

}
