/*
 * Copyright (c) 2020. http://annoyingprojects.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Albi Arapi <albiarapi1@gmail.com>, 2020.
 */

package com.ecommerce.retailapp.domain.api;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.model.CenterRepository;
import com.ecommerce.retailapp.model.entities.ShopModel;
import com.ecommerce.retailapp.utils.AppConstants;
import com.ecommerce.retailapp.utils.Utils;
import com.ecommerce.retailapp.view.activities.ECartHomeActivity;
import com.ecommerce.retailapp.view.adapters.ShopListAdapter;
import com.ecommerce.retailapp.view.fragment.ProductOverviewFragment;

import java.util.ArrayList;

/**
 * The Class ImageLoaderTask.
 */
public class ShopListLoaderTask extends AsyncTask<String, Void, Void> {

    private static final int NUMBER_OF_COLUMNS = 2;
    private Context context;
    private RecyclerView recyclerView;

    public ShopListLoaderTask(RecyclerView listView, Context context) {

        this.recyclerView = listView;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
            if (recyclerView != null) {
                ShopListAdapter simpleRecyclerAdapter = new ShopListAdapter(
                        context);

                recyclerView.setAdapter(simpleRecyclerAdapter);

                simpleRecyclerAdapter
                        .SetOnItemClickListener(new ShopListAdapter.OnItemClickListener() {

                            @Override
                            public void onItemClick(View view, int position) {
                                AppConstants.CURRENT_SHOP = position;

                                Utils.switchFragmentWithAnimation(
                                        R.id.frag_container,
                                        new ProductOverviewFragment( CenterRepository.getCenterRepository()
                                                .getShopsOfCategory().get(AppConstants.CURRENT_SHOP).getShopName(),
                                                CenterRepository.getCenterRepository()
                                                        .getShopsOfCategory().get(AppConstants.CURRENT_SHOP).getCategoryName()),
                                        ((ECartHomeActivity) context), null,
                                        Utils.AnimationType.SLIDE_LEFT);

                            }
                        });
            }

    }

    @Override
    protected Void doInBackground(String... params) {
        setListOfShop();
        return null;
    }

    public void setListOfShop(){
         ArrayList<ShopModel> shopsOfCategory = new ArrayList<>();
        int size = CenterRepository.getCenterRepository().getListOfShop().size();
        for (int i =0; i< size; i++){
            if (CenterRepository.getCenterRepository().getListOfShop().get(i).getCategoryName()
                    .equals(CenterRepository.getCenterRepository().getListOfCategory().get(AppConstants.CURRENT_CATEGORY).getProductCategoryName())){
                shopsOfCategory.add(CenterRepository.getCenterRepository().getListOfShop().get(i));
            }
        }

        CenterRepository.getCenterRepository().setShopsOfCategory(shopsOfCategory);
    }

}