package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.HomePageFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = HomePageViewModule.class)
public interface HomePageViewComponent {
    void inject(HomePageFragment fragment);
}