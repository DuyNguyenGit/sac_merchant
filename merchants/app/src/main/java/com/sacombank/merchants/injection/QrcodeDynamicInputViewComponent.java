package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.QRCodeDynamicInputFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = QrcodeDynamicInputViewModule.class)
public interface QrcodeDynamicInputViewComponent {
    void inject(QRCodeDynamicInputFragment fragment);
}