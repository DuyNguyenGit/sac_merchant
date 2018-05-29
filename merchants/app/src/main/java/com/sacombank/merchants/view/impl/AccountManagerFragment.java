package com.sacombank.merchants.view.impl;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sacombank.merchants.R;
import com.sacombank.merchants.injection.AccountManagerViewModule;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.DaggerAccountManagerViewComponent;
import com.sacombank.merchants.presenter.AccountManagerPresenter;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.view.AccountManagerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public final class AccountManagerFragment extends BaseFragment<AccountManagerPresenter, AccountManagerView> implements AccountManagerView {
    @Inject
    PresenterFactory<AccountManagerPresenter> mPresenterFactory;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private int currentPage;
    private int[] imageResIdUnselected = {R.drawable.ic_info_unselected,
            R.drawable.ic_password_unselected,
            R.drawable.ic_languge_unselected};
    private int[] imageResIdSelected = {R.drawable.ic_info_selected,
            R.drawable.ic_password_selected,
            R.drawable.ic_language_selected};

    public AccountManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_manager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViewPager(view);
    }


    private void setupViewPager(View view) {

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupTabs(viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ImageView imageView = (ImageView)tab.getCustomView().findViewById(R.id.imgTab);
                TextView tvTitle = (TextView)tab.getCustomView().findViewById(R.id.tvTabTitle);
                imageView.setImageResource(imageResIdSelected[tab.getPosition()]);
                tvTitle.setTextColor(ResourcesCompat.getColor(getResources(), R.color.title_tabbar_selected_account_manager, null));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ImageView imageView = (ImageView)tab.getCustomView().findViewById(R.id.imgTab);
                TextView tvTitle = (TextView)tab.getCustomView().findViewById(R.id.tvTabTitle);
                imageView.setImageResource(imageResIdUnselected[tab.getPosition()]);
                tvTitle.setTextColor(ResourcesCompat.getColor(getResources(), R.color.title_tabbar_unselected_account_manager, null));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        customTab(0);
    }


    private void customTab(int selectedPos) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i, i==selectedPos));
        }
    }


    private void setupTabs(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager(), getActivity());
        adapter.addFragment(new AccountInfoFragment(), getString(R.string.tab_title_account_info));
        adapter.addFragment(new AccountPasswordFragment(), getString(R.string.tab_title_account_password));
        adapter.addFragment(new AccountLanguageFragment(), getString(R.string.tab_title_account_language));
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerAccountManagerViewComponent.builder()
                .appComponent(parentComponent)
                .accountManagerViewModule(new AccountManagerViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void handleCallApi() {

    }

    @Override
    public void onBackPressed() {

        ((BottomTabbarActivity) getActivity()).goHomePage();
    }

    @NonNull
    @Override
    protected PresenterFactory<AccountManagerPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private Context context;


        public ViewPagerAdapter(FragmentManager manager, Context context) {
            super(manager);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public View getTabView(int position, boolean isSelected) {
            View v = LayoutInflater.from(this.context).inflate(R.layout.tab_account_custom, null);
            TextView tv = (TextView) v.findViewById(R.id.tvTabTitle);
            ImageView img = (ImageView) v.findViewById(R.id.imgTab);
            tv.setText(mFragmentTitleList.get(position));
            if (isSelected) {
                img.setImageResource(imageResIdSelected[position]);
                tv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.title_tabbar_selected_account_manager, null));
            } else {
                img.setImageResource(imageResIdUnselected[position]);
                tv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.title_tabbar_unselected_account_manager, null));
            }
            return v;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCommonUI(AccountManagerFragment.class.getSimpleName(), "Quản lý tài khoản");
    }
}
