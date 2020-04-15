
package com.ecommerce.retailapp.domain.api;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.model.entities.ShopModel;
import com.ecommerce.retailapp.view.fragment.ProductOverviewFragment;
import com.example.connectionframework.requestframework.sender.Repository;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import com.ecommerce.retailapp.domain.mock.LocalServer;
import com.ecommerce.retailapp.model.CenterRepository;
import com.ecommerce.retailapp.utils.AppConstants;
import com.ecommerce.retailapp.view.activities.ECartHomeActivity;
import com.ecommerce.retailapp.view.adapters.ProductsInCategoryPagerAdapter;
import com.ecommerce.retailapp.view.fragment.ProductListFragment;

import java.util.ArrayList;
import java.util.Set;

/**
 * The Class ImageLoaderTask.
 */
public class ProductLoaderTask extends AsyncTask<String, Void, Void> {

    private ProductOverviewFragment productOverviewFragment;
    private Context context;
    private ViewPager viewPager;
    private TabLayout tabs;
    private RecyclerView recyclerView;
    private String shopName;
    private ArrayList<ShopModel> listOfShop = new ArrayList<>();

    public ProductLoaderTask(ProductOverviewFragment productOverviewFragment, String shopName, ViewPager viewPager, TabLayout tabs) {
        this.productOverviewFragment = productOverviewFragment;
        this.context = productOverviewFragment.getContext();
        this.shopName = shopName;
        this.viewPager = viewPager;
        this.tabs = tabs;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        ((ECartHomeActivity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        AppConstants.disableBackPress = true;

    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        productOverviewFragment.getShimmer_view_container().setVisibility(View.GONE);
        ((ECartHomeActivity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
              AppConstants.disableBackPress = false;
        if(CenterRepository.getCenterRepository().getMapOfProductsInCategory() != null) {
          setUpUi();
        }else {
            if (null != ((ECartHomeActivity) context).getRl_error_server()) {
                ((ECartHomeActivity) context).getTv_error_message().setText(Repository.newInstance().getMessageError());
                ((ECartHomeActivity) context).getRl_error_server().setVisibility(
                        View.VISIBLE);
            }
        }

    }

    @Override
    protected Void doInBackground(String... params) {
        LocalServer.getFakeWebServer().getAllProductsOfCategory(shopName, context);
        return null;
    }

    private void setUpUi() {

        setupViewPager();


    }

    private void setupViewPager() {

        ProductsInCategoryPagerAdapter adapter = new ProductsInCategoryPagerAdapter(
                ((ECartHomeActivity) context).getSupportFragmentManager());

        Set<String> keys = CenterRepository.getCenterRepository().getMapOfProductsInCategory().keySet();

        for (String string : keys) {

            adapter.addFrag(new ProductListFragment(string), string);

        }

        viewPager.setAdapter(adapter);

        tabs.setupWithViewPager(viewPager);
        tabs.setSelectedTabIndicatorHeight((int) (5 * productOverviewFragment.getResources().getDisplayMetrics().density));
        tabs.setSelectedTabIndicatorColor(productOverviewFragment.getResources().getColor(R.color.secondary_color));
        tabs.setTabTextColors(Color.parseColor("#727272"),
                productOverviewFragment.getResources().getColor(R.color.secondary_color));
        tabs.setVisibility(View.VISIBLE);

        if (null != ((ECartHomeActivity) context).getFrag_container())
            ((ECartHomeActivity) context).getFrag_container().setVisibility(
                    View.VISIBLE);

    }





}