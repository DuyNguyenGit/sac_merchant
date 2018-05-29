package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.GuidelineFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = GuidelineViewModule.class)
public interface GuidelineViewComponent {
    void inject(GuidelineFragment fragment);
}