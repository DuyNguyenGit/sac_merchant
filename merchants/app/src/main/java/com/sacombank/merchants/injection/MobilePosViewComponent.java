package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.MobilePosFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = MobilePosViewModule.class)
public interface MobilePosViewComponent {
    void inject(MobilePosFragment fragment);
}