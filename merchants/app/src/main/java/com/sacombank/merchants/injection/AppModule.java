package com.sacombank.merchants.injection;

import android.content.Context;
import android.support.annotation.NonNull;

import com.sacombank.merchants.SacombankApp;

import dagger.Module;
import dagger.Provides;

@Module
public final class AppModule {
    @NonNull
    private final SacombankApp mApp;

    public AppModule(@NonNull SacombankApp app) {
        mApp = app;
    }

    @Provides
    public Context provideAppContext() {
        return mApp;
    }

    @Provides
    public SacombankApp provideApp() {
        return mApp;
    }
}
