/*
<!--
  ~ Copyright (c) 2017. http://annoyingprojects.com- All Rights Reserved
  ~ Unauthorized copying of this file, via any medium is strictly prohibited
  ~ If you use or distribute this project then you MUST ADD A COPY OF LICENCE
  ~ along with the project.
  ~  Written by Albi Arapi <albiarapi1@gmail.com>, 2017.
  -->
 */

package com.ecommerce.retailapp.view.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProductsInCategoryPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ProductsInCategoryPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
