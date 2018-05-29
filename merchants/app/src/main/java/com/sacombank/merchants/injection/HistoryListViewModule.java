package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.HistoryListInteractor;
import com.sacombank.merchants.interactor.impl.HistoryListInteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.HistoryListPresenter;
import com.sacombank.merchants.presenter.impl.HistoryListPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class HistoryListViewModule {
    @Provides
    public HistoryListInteractor provideInteractor() {
        return new HistoryListInteractorImpl();
    }

    @Provides
    public PresenterFactory<HistoryListPresenter> providePresenterFactory(@NonNull final HistoryListInteractor interactor) {
        return new PresenterFactory<HistoryListPresenter>() {
            @NonNull
            @Override
            public HistoryListPresenter create() {
                return new HistoryListPresenterImpl(interactor);
            }
        };
    }
}
