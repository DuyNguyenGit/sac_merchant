package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.QRCodeStaticFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = QrcodeStaticViewModule.class)
public interface QrcodeStaticViewComponent {
    void inject(QRCodeStaticFragment fragment);
}