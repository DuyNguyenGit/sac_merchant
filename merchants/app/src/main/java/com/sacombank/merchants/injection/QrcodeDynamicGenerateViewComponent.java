package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.QRCodeDynamicGenerateFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = QrcodeDynamicGenerateViewModule.class)
public interface QrcodeDynamicGenerateViewComponent {
    void inject(QRCodeDynamicGenerateFragment fragment);
}
