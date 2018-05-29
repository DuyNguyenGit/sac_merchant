package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.AccountLanguageFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = AccountLanguageViewModule.class)
public interface AccountLanguageViewComponent {
    void inject(AccountLanguageFragment fragment);
}