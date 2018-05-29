package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.CreateQrcodeInteractor;
import com.sacombank.merchants.interactor.impl.CreateQrcodeInteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.CreateQrcodePresenter;
import com.sacombank.merchants.presenter.impl.CreateQrcodePresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class CreateQrcodeViewModule {
    @Provides
    public CreateQrcodeInteractor provideInteractor() {
        return new CreateQrcodeInteractorImpl();
    }

    @Provides
    public PresenterFactory<CreateQrcodePresenter> providePresenterFactory(@NonNull final CreateQrcodeInteractor interactor) {
        return new PresenterFactory<CreateQrcodePresenter>() {
            @NonNull
            @Override
            public CreateQrcodePresenter create() {
                return new CreateQrcodePresenterImpl(interactor);
            }
        };
    }
}
