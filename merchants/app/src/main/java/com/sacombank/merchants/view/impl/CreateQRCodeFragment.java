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
import com.sacombank.merchants.SacombankApp;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.CreateQrcodeViewModule;
import com.sacombank.merchants.injection.DaggerCreateQrcodeViewComponent;
import com.sacombank.merchants.presenter.CreateQrcodePresenter;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.view.CreateQrcodeView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.sacombank.merchants.view.impl.QRCodeDynamicGenerateFragment.KEY;

public final class CreateQRCodeFragment extends BaseFragment<CreateQrcodePresenter, CreateQrcodeView> implements CreateQrcodeView {
    @Inject
    PresenterFactory<CreateQrcodePresenter> mPresenterFactory;


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private int currentPage;
    private BaseFragment currentFragment;
    private int[] imageResIdUnselected = {R.drawable.ic_qrcode_static_unselected,
            R.drawable.ic_qrcode_dynamiv_unselected,
            R.drawable.ic_nfc_unselected};
    private int[] imageResIdSelected = {R.drawable.ic_qrcode_static_selected,
            R.drawable.ic_qrcode_dynamic_selected,
            R.drawable.ic_nfc_selected};


    public CreateQRCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_qrcode, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String user = ((SacombankApp) getActivity().getApplication()).userAccountPref.getValue();
        if (user.isEmpty()) {
            ((BottomTabbarActivity) getActivity()).goLoginPage();
            return;
        }

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
                currentFragment = (BaseFragment) adapter.getItem(position);
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
                ImageView imageView = (ImageView) tab.getCustomView().findViewById(R.id.imgTab);
                TextView tvTitle = (TextView) tab.getCustomView().findViewById(R.id.tvTabTitle);
                imageView.setImageResource(imageResIdSelected[tab.getPosition()]);
                tvTitle.setTextColor(ResourcesCompat.getColor(getResources(), R.color.title_tabbar_selected_account_manager, null));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ImageView imageView = (ImageView) tab.getCustomView().findViewById(R.id.imgTab);
                TextView tvTitle = (TextView) tab.getCustomView().findViewById(R.id.tvTabTitle);
                imageView.setImageResource(imageResIdUnselected[tab.getPosition()]);
                tvTitle.setTextColor(ResourcesCompat.getColor(getResources(), R.color.title_tabbar_unselected_account_manager, null));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        customTab(0);
    }
    public void gotoTab(int pos) {
        TabLayout.Tab tab = tabLayout.getTabAt(pos);
        if (tab != null) {
            tab.select();
        }
    }

    private void customTab(int selectedPos) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i, i == selectedPos));
        }
        gotoTab(1);
    }


    private void setupTabs(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager(), getActivity());
        QRCodeDynamicGenerateFragment fragment = new QRCodeDynamicGenerateFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY, 0);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.tab_title_qr_static));
        adapter.addFragment(new QRCodeDynamicFragment(), getString(R.string.tab_title_qr_dynamic));
        //adapter.addFragment(new NFCFragment(), getString(R.string.tab_title_nfc));
        viewPager.setAdapter(adapter);
    }


    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerCreateQrcodeViewComponent.builder()
                .appComponent(parentComponent)
                .createQrcodeViewModule(new CreateQrcodeViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void handleCallApi() {

    }

    @Override
    public void onBackPressed() {
//        currentFragment.onBackPressed();
        ((BottomTabbarActivity) getActivity()).goHomePage();
    }

    @NonNull
    @Override
    protected PresenterFactory<CreateQrcodePresenter> getPresenterFactory() {
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
        updateCommonUI(AccountManagerFragment.class.getSimpleName(), "Hiển thị QR Code");
    }

}
