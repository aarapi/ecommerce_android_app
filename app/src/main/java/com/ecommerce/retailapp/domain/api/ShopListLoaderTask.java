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
import com.ecommerce.retailapp.domain.mock.FakeWebServer;
import com.ecommerce.retailapp.model.CenterRepository;
import com.ecommerce.retailapp.utils.AppConstants;
import com.ecommerce.retailapp.utils.Utils;
import com.ecommerce.retailapp.utils.Utils.AnimationType;
import com.ecommerce.retailapp.view.activities.ECartHomeActivity;
import com.ecommerce.retailapp.view.adapters.ShopListAdapter;
import com.ecommerce.retailapp.view.fragment.ProductOverviewFragment;
import com.example.connectionframework.requestframework.sender.Repository;

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

        if (null != ((ECartHomeActivity) context).getProgressBar()
                && CenterRepository.getCenterRepository().getListOfShop().size() == 0)
            ((ECartHomeActivity) context).getProgressBar().setVisibility(
                    View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if (null != ((ECartHomeActivity) context).getProgressBar())
            ((ECartHomeActivity) context).getProgressBar().setVisibility(
                    View.GONE);

        if(CenterRepository.getCenterRepository().getListOfShop() != null) {
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
                                        new ProductOverviewFragment(),
                                        ((ECartHomeActivity) context), null,
                                        AnimationType.SLIDE_LEFT);

                            }
                        });
            }
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

        if(CenterRepository.getCenterRepository().getListOfShop().size() == 0) {
            FakeWebServer.getFakeWebServer().getAllShopList(AppConstants.CURRENT_CATEGORY,context);
        }

        return null;
    }

}