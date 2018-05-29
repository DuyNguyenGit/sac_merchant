package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.IntroduceInteractor;
import com.sacombank.merchants.interactor.impl.IntroduceInteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.IntroducePresenter;
import com.sacombank.merchants.presenter.impl.IntroducePresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class IntroduceViewModule {
    @Provides
    public IntroduceInteractor provideInteractor() {
        return new IntroduceInteractorImpl();
    }

    @Provides
    public PresenterFactory<IntroducePresenter> providePresenterFactory(@NonNull final IntroduceInteractor interactor) {
        return new PresenterFactory<IntroducePresenter>() {
            @NonNull
            @Override
            public IntroducePresenter create() {
                return new IntroducePresenterImpl(interactor);
            }
        };
    }
}
