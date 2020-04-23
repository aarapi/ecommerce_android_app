
package com.ecommerce.retailapp.view.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;

import com.ecommerce.retailapp.model.CenterRepository;
import com.ecommerce.retailapp.view.adapters.StoryRecyclerViewAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.domain.api.ProductCategoryLoaderTask;
import com.ecommerce.retailapp.view.activities.ECartHomeActivity;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    int mutedColor = R.attr.colorPrimary;
    private RecyclerView recyclerView;
    private ShimmerFrameLayout mShimmerViewContainer;
    StoryRecyclerViewAdapter mStoryRVAdapter = new StoryRecyclerViewAdapter();
    private RecyclerView rv_stories;
    /**
     * The double back to exit pressed once.
     */
    private boolean doubleBackToExitPressedOnce;
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };
    private Handler mHandler = new Handler();
    private SearchProductFragment searchFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_product_category, container, false);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        rv_stories = view.findViewById(R.id.rv_stories);

        view.findViewById(R.id.search_item).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (CenterRepository.getCenterRepository().getListOfShop() != null) {
                            searchFragment = new SearchProductFragment(getContext(), true);
                            searchFragment.show(getFragmentManager(),
                                    OrderExecuteBootomFragment.TAG);
                        }else{
                            Toast.makeText(getContext(), "Kontrolloni Lidhjen me rrjetin", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.anim_toolbar);
        ((ECartHomeActivity) getActivity()).setSupportActionBar(toolbar);
        ((ECartHomeActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ECartHomeActivity) getActivity()).getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        ImageView header = (ImageView) view.findViewById(R.id.header);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.header);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @SuppressWarnings("ResourceType")
            @Override
            public void onGenerated(Palette palette) {

                mutedColor = palette.getMutedColor(R.color.transparent);

            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.scrollableview);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        mShimmerViewContainer.startShimmerAnimation();

        new ProductCategoryLoaderTask(recyclerView, this).execute();


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {

                    if (doubleBackToExitPressedOnce) {
                        // super.onBackPressed();

                        if (mHandler != null) {
                            mHandler.removeCallbacks(mRunnable);
                        }

                        getActivity().finish();

                        return true;
                    }

                    doubleBackToExitPressedOnce = true;
                    Toast.makeText(getActivity(),
                            "Please click BACK again to exit",
                            Toast.LENGTH_SHORT).show();

                    mHandler.postDelayed(mRunnable, 2000);

                }
                return true;
            }
        });


        return view;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    public ShimmerFrameLayout getmShimmerViewContainer() {
        return mShimmerViewContainer;
    }

    public void setStory(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        storyList();
        mStoryRVAdapter.setList(CenterRepository.getCenterRepository().getAdsProducts());
        rv_stories.setLayoutManager(layoutManager);
        rv_stories.setAdapter(mStoryRVAdapter);
        mStoryRVAdapter.notifyDataSetChanged();
    }

    private void storyList() {
        int size = CenterRepository.getCenterRepository().getAdsProducts().size();
        for (int i =0; i<size; i++){
            CenterRepository.getCenterRepository().getAdsProducts().get(i).setProductId(i + "");
        }
    }
}
