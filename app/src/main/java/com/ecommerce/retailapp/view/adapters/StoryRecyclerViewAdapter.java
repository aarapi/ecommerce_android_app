/*
 * Copyright (c) 2020. http://annoyingprojects.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Albi Arapi <albiarapi1@gmail.com>, 2020.
 */

package com.ecommerce.retailapp.view.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.model.CenterRepository;
import com.ecommerce.retailapp.model.entities.Product;
import com.ecommerce.retailapp.utils.AppConstants;
import com.ecommerce.retailapp.utils.ColorGenerator;
import com.ecommerce.retailapp.utils.Utils;
import com.ecommerce.retailapp.view.activities.ECartHomeActivity;
import com.ecommerce.retailapp.view.activities.StoryActivity;
import com.ecommerce.retailapp.view.customview.TextDrawable;
import com.ecommerce.retailapp.view.customview.TextDrawable.IBuilder;
import com.ecommerce.retailapp.view.data.StoryInfo;
import com.ecommerce.retailapp.view.fragment.ProductOverviewFragment;

import java.util.ArrayList;
import java.util.List;


public class StoryRecyclerViewAdapter
        extends RecyclerView.Adapter<StoryViewHolder>
        implements View.OnClickListener {

    public static String STORY_LIST_INFO = "STORY_LIST_INFO";
    public static String SELECTED_ITEM_INFO = "SELECTED_ITEM_INFO";
    private ArrayList<Product> mList = new ArrayList<>();

    private boolean isShop;

    private Context context;

    public StoryRecyclerViewAdapter(boolean isShop, Context context) {
        this.isShop = isShop;
        this.context = context;
    }

    public List<Product> getList() {
        return mList;
    }

    public void setList(ArrayList<Product> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    @SuppressLint("InflateParams")
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (isShop) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_cell_layout_shop, null);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_cell_layout, null);
        }

        return new StoryViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        Product item = getItem(position);
        holder.bind(item);
    }

    public Product getItem(int position) {
        return mList.size() > position ? mList.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View itemView) {
        Product clickedItem = (Product) itemView.getTag();
        if (!isShop) {
            showCampaign(clickedItem, (ECartHomeActivity) context, itemView);
        } else {
            AppConstants.isFromShop = false;
            Utils.switchFragmentWithAnimation(
                    R.id.frag_container,
                    new ProductOverviewFragment(clickedItem.getItemName(),
                            clickedItem.getSubCategoryName()),
                    ((ECartHomeActivity) context), null,
                    Utils.AnimationType.SLIDE_LEFT);
        }


    }

    private void showCampaign(Product storyInfo, ECartHomeActivity mActivity, @Nullable View itemView) {
        Bundle bundleParams = new Bundle();
        bundleParams.putSerializable(STORY_LIST_INFO, mList);
        bundleParams.putSerializable(SELECTED_ITEM_INFO, storyInfo);
        Intent intent = new Intent(itemView.getContext(), StoryActivity.class);
        intent.putExtra("data", bundleParams);
        mActivity.startActivityForResult(intent, 1);
    }
}

class StoryViewHolder extends RecyclerView.ViewHolder {

    private ImageView mImageView;
    private TextView mTextView;
    private IBuilder mDrawableBuilder;
    private TextDrawable drawable;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

    private String ImageUrl;

    StoryViewHolder(@NonNull View itemView, @NonNull StoryRecyclerViewAdapter adapter) {
        super(itemView);

        itemView.setOnClickListener(adapter);

        mImageView = itemView.findViewById(R.id.imageView);
        mTextView = itemView.findViewById(R.id.textView);
    }

    /**
     * bind a item to a view
     *
     * @param item to bind
     */
    void bind(Product item) {

        boolean animateSeen = (itemView.getTag() == item);


        mTextView.setText(item.getItemName());
        itemView.setTag(item);
        String imageUrl = item.getImageURL();

        mDrawableBuilder = TextDrawable.builder().beginConfig().withBorder(4)
                .endConfig().roundRect(10);

        drawable = mDrawableBuilder.build(String.valueOf(item.getItemName().charAt(0)), mColorGenerator
                .getColor(item.getItemName()));

        ImageUrl = imageUrl;


        Glide.with(itemView.getContext()).load(ImageUrl).placeholder(drawable)
                .error(drawable)
                .centerCrop().into(mImageView);
    }


}
