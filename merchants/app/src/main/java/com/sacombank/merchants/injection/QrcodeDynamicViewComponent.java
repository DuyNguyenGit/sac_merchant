package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.QRCodeDynamicFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = QrcodeDynamicViewModule.class)
public interface QrcodeDynamicViewComponent {
    void inject(QRCodeDynamicFragment fragment);
}