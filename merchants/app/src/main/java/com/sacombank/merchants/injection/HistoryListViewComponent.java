package com.sacombank.merchants.injection;

import com.sacombank.merchants.view.impl.HistoryListFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = HistoryListViewModule.class)
public interface HistoryListViewComponent {
    void inject(HistoryListFragment fragment);
}