package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.NFCFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = NfcViewModule.class)
public interface NfcViewComponent {
    void inject(NFCFragment fragment);
}