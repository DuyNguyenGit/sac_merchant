package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.HistoryFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = HistoryViewModule.class)
public interface HistoryViewComponent {
    void inject(HistoryFragment fragment);
}