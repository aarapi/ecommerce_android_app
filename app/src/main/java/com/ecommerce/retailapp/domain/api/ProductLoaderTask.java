
package com.ecommerce.retailapp.domain.api;

import android.content.Context;
import android.os.AsyncTask;

import com.ecommerce.retailapp.model.entities.Product;
import com.ecommerce.retailapp.model.entities.ShopModel;
import com.example.connectionframework.requestframework.sender.Repository;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.ecommerce.retailapp.domain.mock.FakeWebServer;
import com.ecommerce.retailapp.model.CenterRepository;
import com.ecommerce.retailapp.utils.AppConstants;
import com.ecommerce.retailapp.view.activities.ECartHomeActivity;
import com.ecommerce.retailapp.view.adapters.ProductsInCategoryPagerAdapter;
import com.ecommerce.retailapp.view.fragment.ProductListFragment;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Class ImageLoaderTask.
 */
public class ProductLoaderTask extends AsyncTask<String, Void, Void> {

    private Context context;
    private ViewPager viewPager;
    private TabLayout tabs;
    private RecyclerView recyclerView;
    private ArrayList<ShopModel> listOfShop = new ArrayList<>();

    public ProductLoaderTask(RecyclerView listView, Context context,
                             ViewPager viewpager, TabLayout tabs) {

        this.viewPager = viewpager;
        this.tabs = tabs;
        this.context = context;

}
    public ProductLoaderTask(Context context,
                             ViewPager viewpager, TabLayout tabs) {

        this.viewPager = viewpager;
        this.tabs = tabs;
        this.context = context;

    }



    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        if (null != ((ECartHomeActivity) context).getProgressBar()) {
            ((ECartHomeActivity) context).getProgressBar().setVisibility(
                    View.VISIBLE);
        }


    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if (null != ((ECartHomeActivity) context).getProgressBar())
            ((ECartHomeActivity) context).getProgressBar().setVisibility(
                    View.GONE);

        if(CenterRepository.getCenterRepository().getMapAllProducts() != null) {
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
        setProducstOfShop();
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

//		viewPager.setPageTransformer(true,
//				new );

        tabs.setupWithViewPager(viewPager);

        if (null != ((ECartHomeActivity) context).getFrag_container())
            ((ECartHomeActivity) context).getFrag_container().setVisibility(
                    View.VISIBLE);

    }


    private void setProducstOfShop(){
        ConcurrentHashMap<String, ArrayList<Product>> mapOfProductsInCategory = new ConcurrentHashMap<>();
        Set<String> keys =  CenterRepository.getCenterRepository().getMapAllProducts().keySet();

        for (String string : keys) {
            ArrayList<Product> products = new ArrayList<>();
            int sizeOfProducts = CenterRepository.getCenterRepository().getMapAllProducts().get(string).size();
            for (int i = 0; i < sizeOfProducts; i++){
                if (CenterRepository.getCenterRepository().getMapAllProducts().get(string).get(i).getShopName()
                        .equals(CenterRepository.getCenterRepository().getShopsOfCategory().get(AppConstants.CURRENT_SHOP).getShopName())){
                    products.add(CenterRepository.getCenterRepository().getMapAllProducts().get(string).get(i));
                }
            }
            if(products.size() > 0)
            mapOfProductsInCategory.put(string, products);
        }

        CenterRepository.getCenterRepository().setMapOfProductsInCategory(mapOfProductsInCategory);

    }



}