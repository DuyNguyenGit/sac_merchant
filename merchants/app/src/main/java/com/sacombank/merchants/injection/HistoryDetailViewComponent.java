package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.HistoryDetailFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = HistoryDetailViewModule.class)
public interface HistoryDetailViewComponent {
    void inject(HistoryDetailFragment fragment);
}