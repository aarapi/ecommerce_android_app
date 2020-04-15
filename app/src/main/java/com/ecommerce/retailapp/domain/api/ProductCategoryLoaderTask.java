package com.ecommerce.retailapp.domain.api;

import android.os.AsyncTask;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.domain.mock.LocalServer;
import com.ecommerce.retailapp.model.CenterRepository;
import com.ecommerce.retailapp.utils.AppConstants;
import com.ecommerce.retailapp.utils.Utils;
import com.ecommerce.retailapp.utils.Utils.AnimationType;
import com.ecommerce.retailapp.view.activities.ECartHomeActivity;
import com.ecommerce.retailapp.view.adapters.CategoryListAdapter;
import com.ecommerce.retailapp.view.adapters.CategoryListAdapter.OnItemClickListener;
import com.ecommerce.retailapp.view.fragment.HomeFragment;
import com.ecommerce.retailapp.view.fragment.ShopListFragment;
import com.example.connectionframework.requestframework.sender.Repository;

/**
 * The Class ImageLoaderTask.
 */
public class ProductCategoryLoaderTask extends AsyncTask<String, Void, Void> {

    private static final int NUMBER_OF_COLUMNS = 2;
    private HomeFragment homeFragment;
    private RecyclerView recyclerView;

    public ProductCategoryLoaderTask(RecyclerView listView, HomeFragment homeFragment) {

        this.recyclerView = listView;
        this.homeFragment = homeFragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        homeFragment.getmShimmerViewContainer().setVisibility(View.GONE);
        if(CenterRepository.getCenterRepository().getListOfCategory() != null) {
            homeFragment.setStory();
            if (recyclerView != null) {
                CategoryListAdapter simpleRecyclerAdapter = new CategoryListAdapter(
                        homeFragment.getContext());

                recyclerView.setAdapter(simpleRecyclerAdapter);
                simpleRecyclerAdapter
                        .SetOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(View view, int position) {

                                AppConstants.CURRENT_CATEGORY = position;

                                Utils.switchFragmentWithAnimation(
                                        R.id.frag_container,
                                        new ShopListFragment(),
                                        ((ECartHomeActivity) homeFragment.getContext()), null,
                                        AnimationType.SLIDE_LEFT);

                            }
                        });
            }
        }else {
            if (null != ((ECartHomeActivity) homeFragment.getContext()).getRl_error_server()) {
                ((ECartHomeActivity) homeFragment.getContext()).getTv_error_message().setText(Repository.newInstance().getMessageError());
                ((ECartHomeActivity) homeFragment.getContext()).getRl_error_server().setVisibility(
                        View.VISIBLE);
            }
        }

    }

    @Override
    protected Void doInBackground(String... params) {

        if(CenterRepository.getCenterRepository().getListOfCategory() == null) {
            LocalServer.getFakeWebServer().addCategory(homeFragment.getContext());
        }

        return null;
    }

}