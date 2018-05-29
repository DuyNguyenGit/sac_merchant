package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.CreateQRCodeFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = CreateQrcodeViewModule.class)
public interface CreateQrcodeViewComponent {
    void inject(CreateQRCodeFragment fragment);
}