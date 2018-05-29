package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.AccountPasswordFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = AccountPasswordViewModule.class)
public interface AccountPasswordViewComponent {
    void inject(AccountPasswordFragment fragment);
}