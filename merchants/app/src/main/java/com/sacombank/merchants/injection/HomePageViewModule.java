package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.HomePageInteractor;
import com.sacombank.merchants.interactor.impl.HomePageInteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.HomePagePresenter;
import com.sacombank.merchants.presenter.impl.HomePagePresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class HomePageViewModule {
    @Provides
    public HomePageInteractor provideInteractor() {
        return new HomePageInteractorImpl();
    }

    @Provides
    public PresenterFactory<HomePagePresenter> providePresenterFactory(@NonNull final HomePageInteractor interactor) {
        return new PresenterFactory<HomePagePresenter>() {
            @NonNull
            @Override
            public HomePagePresenter create() {
                return new HomePagePresenterImpl(interactor);
            }
        };
    }
}
