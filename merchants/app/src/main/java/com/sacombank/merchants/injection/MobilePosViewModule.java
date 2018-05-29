package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.MobilePosInteractor;
import com.sacombank.merchants.interactor.impl.MobilePosInteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.MobilePosPresenter;
import com.sacombank.merchants.presenter.impl.MobilePosPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class MobilePosViewModule {
    @Provides
    public MobilePosInteractor provideInteractor() {
        return new MobilePosInteractorImpl();
    }

    @Provides
    public PresenterFactory<MobilePosPresenter> providePresenterFactory(@NonNull final MobilePosInteractor interactor) {
        return new PresenterFactory<MobilePosPresenter>() {
            @NonNull
            @Override
            public MobilePosPresenter create() {
                return new MobilePosPresenterImpl(interactor);
            }
        };
    }
}
