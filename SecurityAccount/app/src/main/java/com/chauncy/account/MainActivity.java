package com.chauncy.account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.chauncy.account.fragment.SecurityAccountFragment;
import com.chauncy.account.model.AccountDataCenter;
import com.chauncy.account.model.DbDataCenter;
import com.chauncy.account.utils.AccountConstant;
import com.chauncy.account.utils.ComponentContext;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ComponentContext.init(MainActivity.this);

        //加载数据
        AccountDataCenter.get().asyncLoadAllAccount();
        DbDataCenter.get().asyncLoadData();

        TabLayout tabLayout = findViewById(R.id.main_tab_layout);
        final ViewPager viewPager = findViewById(R.id.main_view_pager);

        MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager(),
                new String[]{AccountConstant.ACCOUNT_TYPE_HK
                        ,AccountConstant.ACCOUNT_TYPE_USA,AccountConstant.ACCOUNT_TYPE_CN});
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                FragmentPagerAdapter adapter= (FragmentPagerAdapter) viewPager.getAdapter();
                SecurityAccountFragment fragment= (SecurityAccountFragment) adapter.getItem(position);
                fragment.refreshData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private class MainFragmentAdapter extends FragmentPagerAdapter {
        private String[] tabs;
        private List<Fragment> mFragments;
        public MainFragmentAdapter(@NonNull FragmentManager fm, String[] tabs) {
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.tabs = tabs;
            mFragments=new ArrayList<>();
            initFragment(tabs);
        }

        private void initFragment(String[] tabs) {
            for(String tab : tabs){
                mFragments.add(SecurityAccountFragment.newInstance(tab));
            }
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }


    }

}