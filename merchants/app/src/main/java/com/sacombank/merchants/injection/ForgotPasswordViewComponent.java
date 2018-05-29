package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.ForgotPasswordFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = ForgotPasswordViewModule.class)
public interface ForgotPasswordViewComponent {
    void inject(ForgotPasswordFragment fragment);
}