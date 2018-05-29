package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.ForgotPass2Fragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = ForgotPass2ViewModule.class)
public interface ForgotPass2ViewComponent {
    void inject(ForgotPass2Fragment fragment);
}