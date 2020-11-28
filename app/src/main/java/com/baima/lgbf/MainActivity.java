package com.baima.lgbf;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.baima.lgbf.fragment.CurrentFragment;
import com.baima.lgbf.fragment.MoreFragment;
import com.baima.lgbf.util.DisclaimerUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    private ViewPager view_pager;
    private RadioGroup rg_bottom_tab;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showDisclaimerDialog();
        initViews();
        initData();
    }

    private void showDisclaimerDialog() {
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        boolean isFirst = sharedPreferences.getBoolean("isFirst", true);
        if (!isFirst) {
            return;
        }

        DisclaimerUtil.showDisclaimerDialog(MainActivity.this);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean("isFirst", false);
        edit.apply();
    }

    private void initViews() {
        view_pager = findViewById(R.id.view_pager);
        rg_bottom_tab = findViewById(R.id.rg_bottom_tab);

        view_pager.setOnPageChangeListener(this);
        rg_bottom_tab.setOnCheckedChangeListener(this);

        RadioButton radioButton = (RadioButton) rg_bottom_tab.getChildAt(0);
        radioButton.setChecked(true);
    }

    private void initData() {
        fragmentList = new ArrayList<>();
        CurrentFragment currentFragment = new CurrentFragment();
        MoreFragment moreFragment = new MoreFragment();

        fragmentList.add(currentFragment);
        fragmentList.add(moreFragment);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        view_pager.setAdapter(myPagerAdapter);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton rb = group.findViewById(checkedId);
        int i = group.indexOfChild(rb);
        view_pager.setCurrentItem(i);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        RadioButton radioButton = (RadioButton) rg_bottom_tab.getChildAt(i);
        radioButton.setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }
}
