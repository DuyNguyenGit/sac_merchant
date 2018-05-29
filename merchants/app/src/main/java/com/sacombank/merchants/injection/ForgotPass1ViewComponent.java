package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.ForgotPass1Fragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = ForgotPass1ViewModule.class)
public interface ForgotPass1ViewComponent {
    void inject(ForgotPass1Fragment fragment);
}