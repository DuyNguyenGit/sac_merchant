package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.BottomTabbarInteractor;
import com.sacombank.merchants.interactor.impl.BottomTabbarInteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.BottomTabbarPresenter;
import com.sacombank.merchants.presenter.impl.BottomTabbarPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class BottomTabbarViewModule {
    @Provides
    public BottomTabbarInteractor provideInteractor() {
        return new BottomTabbarInteractorImpl();
    }

    @Provides
    public PresenterFactory<BottomTabbarPresenter> providePresenterFactory(@NonNull final BottomTabbarInteractor interactor) {
        return new PresenterFactory<BottomTabbarPresenter>() {
            @NonNull
            @Override
            public BottomTabbarPresenter create() {
                return new BottomTabbarPresenterImpl(interactor);
            }
        };
    }
}
