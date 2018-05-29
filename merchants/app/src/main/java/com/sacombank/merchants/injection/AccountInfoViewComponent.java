package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.AccountInfoFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = AccountInfoViewModule.class)
public interface AccountInfoViewComponent {
    void inject(AccountInfoFragment fragment);
}