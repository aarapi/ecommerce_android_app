

package com.ecommerce.retailapp.view.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import com.ecommerce.retailapp.model.entities.Product;
import com.ecommerce.retailapp.model.entities.ProductCategoryModel;
import com.ecommerce.retailapp.utils.AppConstants;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.palette.graphics.Palette;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.domain.api.ProductLoaderTask;
import com.ecommerce.retailapp.model.CenterRepository;
import com.ecommerce.retailapp.utils.Utils;
import com.ecommerce.retailapp.utils.Utils.AnimationType;
import com.ecommerce.retailapp.view.activities.ECartHomeActivity;
import com.ecommerce.retailapp.view.adapters.ProductsInCategoryPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ProductOverviewFragment extends Fragment {

    // SimpleRecyclerAdapter adapter;
    private KenBurnsView header;
    private Bitmap bitmap;
    private Toolbar mToolbar;
    private ViewPager viewPager;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TabLayout tabLayout;
    private  View view;
    private String shopName;
    private ShimmerFrameLayout shimmer_view_container;

    public ProductOverviewFragment() {
    }

    public ProductOverviewFragment(String shopName) {
        this.shopName = shopName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_category_details,
                container, false);
        shimmer_view_container = view.findViewById(R.id.shimmer_view_container);
        view.findViewById(R.id.search_item).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (CenterRepository.getCenterRepository().getMapOfProductsInCategory() != null) {
                            SearchProductFragment searchFragment = new SearchProductFragment(getContext(), false);
                            searchFragment.show(getFragmentManager(),
                                    OrderExecuteBootomFragment.TAG);
                        }else{
                            Toast.makeText(getContext(), "Kontrolloni Lidhjen me rrjetin", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


        getActivity().setTitle("Products");


        viewPager = (ViewPager) view.findViewById(R.id.htab_viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.htab_tabs);

        collapsingToolbarLayout = (CollapsingToolbarLayout) view
                .findViewById(R.id.htab_collapse_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        header = (KenBurnsView) view.findViewById(R.id.htab_header);
        header.setImageResource(R.drawable.header);


        mToolbar = (Toolbar) view.findViewById(R.id.htab_toolbar);
        if (mToolbar != null) {
            ((ECartHomeActivity) getActivity()).setSupportActionBar(mToolbar);
        }

        if (mToolbar != null) {
            ((ECartHomeActivity) getActivity()).getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);

            mToolbar.setNavigationIcon(R.drawable.ic_drawer);

        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ECartHomeActivity) getActivity()).getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {

                    if (((ECartHomeActivity) getContext()).getRl_error_server() != null)
                        ((ECartHomeActivity) getContext()).getRl_error_server().setVisibility(View.GONE);

                    Utils.switchContent(R.id.frag_container,
                            Utils.SHOP_FRAGMENT,
                            ((ECartHomeActivity) (getContext())),
                            AnimationType.SLIDE_RIGHT);

                }
                return true;
            }
        });

        getProducts();

        return view;
    }

//    private void setUpUi() {
//
//        setupViewPager(viewPager);
//
//        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF0000"));
//        tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
//        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#FF0000"));
//
//        bitmap = BitmapFactory
//                .decodeResource(getResources(), R.drawable.header);
//
//        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//            @SuppressWarnings("ResourceType")
//            @Override
//            public void onGenerated(Palette palette) {
//
//                int vibrantColor = palette.getVibrantColor(R.color.primary_500);
//                int vibrantDarkColor = palette
//                        .getDarkVibrantColor(R.color.primary_700);
//                collapsingToolbarLayout.setContentScrimColor(vibrantColor);
//                collapsingToolbarLayout
//                        .setStatusBarScrimColor(vibrantDarkColor);
//            }
//        });
//
//        tabLayout
//                .setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//                    @Override
//                    public void onTabSelected(TabLayout.Tab tab) {
//
//                        viewPager.setCurrentItem(tab.getPosition());
//
//                        switch (tab.getPosition()) {
//                            case 0:
//
//                                header.setImageResource(R.drawable.header);
//
//                                bitmap = BitmapFactory.decodeResource(
//                                        getResources(), R.drawable.header);
//
//                                Palette.from(bitmap).generate(
//                                        new Palette.PaletteAsyncListener() {
//                                            @SuppressWarnings("ResourceType")
//                                            @Override
//                                            public void onGenerated(Palette palette) {
//
//                                                int vibrantColor = palette
//                                                        .getVibrantColor(R.color.primary_500);
//                                                int vibrantDarkColor = palette
//                                                        .getDarkVibrantColor(R.color.primary_700);
//                                                collapsingToolbarLayout
//                                                        .setContentScrimColor(vibrantColor);
//                                                collapsingToolbarLayout
//                                                        .setStatusBarScrimColor(vibrantDarkColor);
//                                            }
//                                        });
//                                break;
//                            case 1:
//
//                                header.setImageResource(R.drawable.header_1);
//
//                                bitmap = BitmapFactory.decodeResource(
//                                        getResources(), R.drawable.header_1);
//
//                                Palette.from(bitmap).generate(
//                                        new Palette.PaletteAsyncListener() {
//                                            @SuppressWarnings("ResourceType")
//                                            @Override
//                                            public void onGenerated(Palette palette) {
//
//                                                int vibrantColor = palette
//                                                        .getVibrantColor(R.color.primary_500);
//                                                int vibrantDarkColor = palette
//                                                        .getDarkVibrantColor(R.color.primary_700);
//                                                collapsingToolbarLayout
//                                                        .setContentScrimColor(vibrantColor);
//                                                collapsingToolbarLayout
//                                                        .setStatusBarScrimColor(vibrantDarkColor);
//                                            }
//                                        });
//
//                                break;
//                            case 2:
//
//                                header.setImageResource(R.drawable.header2);
//
//                                Bitmap bitmap = BitmapFactory.decodeResource(
//                                        getResources(), R.drawable.header2);
//
//                                Palette.from(bitmap).generate(
//                                        new Palette.PaletteAsyncListener() {
//                                            @SuppressWarnings("ResourceType")
//                                            @Override
//                                            public void onGenerated(Palette palette) {
//
//                                                int vibrantColor = palette
//                                                        .getVibrantColor(R.color.primary_500);
//                                                int vibrantDarkColor = palette
//                                                        .getDarkVibrantColor(R.color.primary_700);
//                                                collapsingToolbarLayout
//                                                        .setContentScrimColor(vibrantColor);
//                                                collapsingToolbarLayout
//                                                        .setStatusBarScrimColor(vibrantDarkColor);
//                                            }
//                                        });
//
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void onTabUnselected(TabLayout.Tab tab) {
//
//                    }
//
//                    @Override
//                    public void onTabReselected(TabLayout.Tab tab) {
//
//                    }
//                });
//
//    }
//
//    private void setupViewPager(ViewPager viewPager) {
//        ProductsInCategoryPagerAdapter adapter = new ProductsInCategoryPagerAdapter(
//                getActivity().getSupportFragmentManager());
//        Set<String> keys = CenterRepository.getCenterRepository().getMapOfProductsInCategory()
//                .keySet();
//
//        for (String string : keys) {
//            adapter.addFrag(new ProductListFragment(string), string);
//        }
//
//        viewPager.setAdapter(adapter);
//    }

    private void getProducts(){
        shimmer_view_container.startShimmerAnimation();

        new ProductLoaderTask(this,
                shopName, viewPager, tabLayout)
                .execute();
    }
    public ShimmerFrameLayout getShimmer_view_container() {
        return shimmer_view_container;
    }

}