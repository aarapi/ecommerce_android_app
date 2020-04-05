package com.ecommerce.retailapp.domain.api;

import android.content.Context;
import android.os.AsyncTask;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.domain.mock.FakeWebServer;
import com.ecommerce.retailapp.model.CenterRepository;
import com.ecommerce.retailapp.utils.AppConstants;
import com.ecommerce.retailapp.utils.Utils;
import com.ecommerce.retailapp.utils.Utils.AnimationType;
import com.ecommerce.retailapp.view.activities.ECartHomeActivity;
import com.ecommerce.retailapp.view.adapters.CategoryListAdapter;
import com.ecommerce.retailapp.view.adapters.CategoryListAdapter.OnItemClickListener;
import com.ecommerce.retailapp.view.fragment.ProductOverviewFragment;
import com.ecommerce.retailapp.view.fragment.ShopListFragment;
import com.example.connectionframework.requestframework.sender.Repository;

/**
 * The Class ImageLoaderTask.
 */
public class ProductCategoryLoaderTask extends AsyncTask<String, Void, Void> {

    private static final int NUMBER_OF_COLUMNS = 2;
    private Context context;
    private RecyclerView recyclerView;

    public ProductCategoryLoaderTask(RecyclerView listView, Context context) {

        this.recyclerView = listView;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        if (null != ((ECartHomeActivity) context).getProgressBar()
                && CenterRepository.getCenterRepository().getListOfCategory().size() == 0)
            ((ECartHomeActivity) context).getProgressBar().setVisibility(
                    View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if (null != ((ECartHomeActivity) context).getProgressBar())
            ((ECartHomeActivity) context).getProgressBar().setVisibility(
                    View.GONE);

        if(CenterRepository.getCenterRepository().getListOfCategory() != null) {
            if (recyclerView != null) {
                CategoryListAdapter simpleRecyclerAdapter = new CategoryListAdapter(
                        context);

                recyclerView.setAdapter(simpleRecyclerAdapter);

                simpleRecyclerAdapter
                        .SetOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(View view, int position) {

                                AppConstants.CURRENT_CATEGORY = position;

                                Utils.switchFragmentWithAnimation(
                                        R.id.frag_container,
                                        new ShopListFragment(),
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

        if(CenterRepository.getCenterRepository().getListOfCategory().size() == 0) {
            FakeWebServer.getFakeWebServer().addCategory(context);
        }

        return null;
    }

}