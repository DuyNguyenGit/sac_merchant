package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.MainInteractor;
import com.sacombank.merchants.interactor.impl.MainInteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.MainPresenter;
import com.sacombank.merchants.presenter.impl.MainPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class MainViewModule {
    @Provides
    public MainInteractor provideInteractor() {
        return new MainInteractorImpl();
    }

    @Provides
    public PresenterFactory<MainPresenter> providePresenterFactory(@NonNull final MainInteractor interactor) {
        return new PresenterFactory<MainPresenter>() {
            @NonNull
            @Override
            public MainPresenter create() {
                return new MainPresenterImpl(interactor);
            }
        };
    }
}
