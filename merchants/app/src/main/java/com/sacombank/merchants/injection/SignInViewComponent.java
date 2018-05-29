package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.SignInFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = SignInViewModule.class)
public interface SignInViewComponent {
    void inject(SignInFragment fragment);
}