package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.QRCodeDynamicGenerateCreateFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = QrcodeDynamicGenerateCreateViewModule.class)
public interface QrcodeDynamicGenerateCreateViewComponent {
    void inject(QRCodeDynamicGenerateCreateFragment fragment);
}