package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.BottomTabbarActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = BottomTabbarViewModule.class)
public interface BottomTabbarViewComponent {
    void inject(BottomTabbarActivity activity);
}