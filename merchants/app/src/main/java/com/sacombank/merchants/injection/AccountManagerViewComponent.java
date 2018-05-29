package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.AccountManagerFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = AccountManagerViewModule.class)
public interface AccountManagerViewComponent {
    void inject(AccountManagerFragment fragment);
}