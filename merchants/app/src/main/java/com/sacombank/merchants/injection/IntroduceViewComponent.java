package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.IntroduceFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = IntroduceViewModule.class)
public interface IntroduceViewComponent {
    void inject(IntroduceFragment fragment);
}