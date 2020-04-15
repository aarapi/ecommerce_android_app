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
import com.ecommerce.retailapp.utils.ColorGenerator;
import com.ecommerce.retailapp.view.activities.ECartHomeActivity;
import com.ecommerce.retailapp.view.activities.StoryActivity;
import com.ecommerce.retailapp.view.customview.TextDrawable;
import com.ecommerce.retailapp.view.customview.TextDrawable.IBuilder;
import com.ecommerce.retailapp.view.data.StoryInfo;

import java.util.ArrayList;
import java.util.List;


public class StoryRecyclerViewAdapter
        extends RecyclerView.Adapter<StoryViewHolder>
        implements View.OnClickListener {

    public static String STORY_LIST_INFO = "STORY_LIST_INFO";
    public static String SELECTED_ITEM_INFO = "SELECTED_ITEM_INFO";
    private ArrayList<StoryInfo> mList = new ArrayList<>();


    public List<StoryInfo> getList() {
        return mList;
    }

    public void setList(ArrayList<StoryInfo> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    @SuppressLint("InflateParams")
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_cell_layout, null);
        return new StoryViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        StoryInfo item = getItem(position);
        holder.bind(item);
    }

    public StoryInfo getItem(int position) {
        return mList.size() > position ? mList.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View itemView) {
        StoryInfo clickedItem = (StoryInfo) itemView.getTag();
        showCampaign(clickedItem, (ECartHomeActivity) itemView.getContext(), itemView);
    }

    private void showCampaign(StoryInfo storyInfo, ECartHomeActivity mActivity, @Nullable View itemView) {

        Bundle bundleParams = new Bundle();
        bundleParams.putSerializable(STORY_LIST_INFO, mList);
        bundleParams.putSerializable(SELECTED_ITEM_INFO, storyInfo);
        Intent intent = new Intent(itemView.getContext(), StoryActivity.class);
        intent.putExtra("data", bundleParams);
        mActivity.startActivity(intent);

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
    void bind(StoryInfo item) {

        boolean animateSeen = (itemView.getTag() == item);

        float alpha = item.isSeen(itemView.getContext()) ? 0.4f : 1f;
        if (alpha != mTextView.getAlpha()) {
            if (animateSeen) {
                mTextView.animate().alpha(alpha);
            } else {
                mTextView.setAlpha(alpha);
            }
        }

        mTextView.setText(item.Title);
        itemView.setTag(item);
        String imageUrl = item.getLink();

        mDrawableBuilder = TextDrawable.builder().beginConfig().withBorder(4)
                .endConfig().roundRect(10);

        drawable = mDrawableBuilder.build(String.valueOf(item.Title.charAt(0)), mColorGenerator
                .getColor(item.Title));

        ImageUrl = imageUrl;


        Glide.with(itemView.getContext()).load(ImageUrl).placeholder(drawable)
                .error(drawable)
                .centerCrop().into(mImageView);
    }


}
