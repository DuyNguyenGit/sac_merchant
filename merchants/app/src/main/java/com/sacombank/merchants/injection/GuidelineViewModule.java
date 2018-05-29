package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.GuidelineInteractor;
import com.sacombank.merchants.interactor.impl.GuidelineInteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.GuidelinePresenter;
import com.sacombank.merchants.presenter.impl.GuidelinePresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class GuidelineViewModule {
    @Provides
    public GuidelineInteractor provideInteractor() {
        return new GuidelineInteractorImpl();
    }

    @Provides
    public PresenterFactory<GuidelinePresenter> providePresenterFactory(@NonNull final GuidelineInteractor interactor) {
        return new PresenterFactory<GuidelinePresenter>() {
            @NonNull
            @Override
            public GuidelinePresenter create() {
                return new GuidelinePresenterImpl(interactor);
            }
        };
    }
}
