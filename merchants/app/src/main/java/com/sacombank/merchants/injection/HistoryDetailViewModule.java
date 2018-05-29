package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.HistoryDetailInteractor;
import com.sacombank.merchants.interactor.impl.HistoryDetailInteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.HistoryDetailPresenter;
import com.sacombank.merchants.presenter.impl.HistoryDetailPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class HistoryDetailViewModule {
    @Provides
    public HistoryDetailInteractor provideInteractor() {
        return new HistoryDetailInteractorImpl();
    }

    @Provides
    public PresenterFactory<HistoryDetailPresenter> providePresenterFactory(@NonNull final HistoryDetailInteractor interactor) {
        return new PresenterFactory<HistoryDetailPresenter>() {
            @NonNull
            @Override
            public HistoryDetailPresenter create() {
                return new HistoryDetailPresenterImpl(interactor);
            }
        };
    }
}
