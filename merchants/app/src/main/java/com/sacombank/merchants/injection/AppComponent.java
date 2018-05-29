package com.sacombank.merchants.injection;

import android.content.Context;

import com.sacombank.merchants.SacombankApp;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    Context getAppContext();

    SacombankApp getApp();
}