/*
 * Copyright (c) 2020. http://annoyingprojects.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Albi Arapi <albiarapi1@gmail.com>, 2020.
 */

package com.ecommerce.retailapp.view.adapters;

import android.content.Context;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.model.CenterRepository;
import com.ecommerce.retailapp.model.entities.Money;
import com.ecommerce.retailapp.model.entities.Product;
import com.ecommerce.retailapp.model.entities.ShopModel;
import com.ecommerce.retailapp.utils.ColorGenerator;
import com.ecommerce.retailapp.utils.Utils;
import com.ecommerce.retailapp.view.activities.ECartHomeActivity;
import com.ecommerce.retailapp.view.customview.TextDrawable;
import com.ecommerce.retailapp.view.customview.TextDrawable.IBuilder;
import com.ecommerce.retailapp.view.data.StoryInfo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Hitesh Sahu (hiteshsahu.com)
 */
public class  ShopListAdapter extends
        RecyclerView.Adapter<ShopListAdapter.VersionViewHolder> {

    public static List<ShopModel> shopModelList = new ArrayList<>();
    OnItemClickListener clickListener;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private IBuilder mDrawableBuilder;
    private TextDrawable drawable;
    private String ImageUrl;
    private Context context;


    public ShopListAdapter(Context context) {

        shopModelList = CenterRepository.getCenterRepository().getShopsOfCategory();

        this.context = context;
    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.item_shop_list, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VersionViewHolder versionViewHolder,
                                 int categoryIndex) {

        versionViewHolder.itemName.setText(shopModelList.get(categoryIndex)
                .getShopName());

        versionViewHolder.itemDesc.setText(shopModelList.get(categoryIndex)
                .getShopDescription());

        String openingTime = new SimpleDateFormat("HH:mm").format(shopModelList.get(categoryIndex).getOpenTime().getTime());
        String closeTime = new SimpleDateFormat("HH:mm").format(shopModelList.get(categoryIndex).getCloseTime().getTime());

        if (shopModelList.get(categoryIndex).getAvalibility() == 0){
            versionViewHolder.availability.setText("Closed");
            versionViewHolder.availability.setTextColor(context.getResources().getColor(R.color.red));
        }

        versionViewHolder.tv_time.setText(openingTime + " - " + closeTime);


        mDrawableBuilder = TextDrawable.builder().beginConfig().withBorder(4)
                .endConfig().roundRect(10);

        drawable = mDrawableBuilder.build(String.valueOf(shopModelList
                        .get(categoryIndex).getShopName().charAt(0)),
                mColorGenerator.getColor(shopModelList.get(categoryIndex)
                        .getShopName()));

        ImageUrl = shopModelList.get(categoryIndex).getShopImage();

        Glide.with(context).load(ImageUrl).placeholder(drawable)
                .error(drawable)
                .centerCrop().into(versionViewHolder.imagView);


        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        versionViewHolder.mStoryRVAdapter.setList(getStoryList(shopModelList.get(categoryIndex).getShopName()));
        versionViewHolder.rv_stories.setLayoutManager(layoutManager);
        versionViewHolder.rv_stories.setAdapter(versionViewHolder.mStoryRVAdapter);
        versionViewHolder.mStoryRVAdapter.notifyDataSetChanged();
    }

    private ArrayList<StoryInfo> getStoryList(String shopName){
        ArrayList<StoryInfo> storyInfos = new ArrayList<>();
        int size = CenterRepository.getCenterRepository().getListOfStoryProducts().size();
        for (int i =0; i<size; i++){
            StoryInfo storyInfo = new StoryInfo();
            storyInfo.ID = i+"";
            if (CenterRepository.getCenterRepository().getListOfStoryProducts().get(i).getShopName().equals(shopName)) {
                storyInfo.Name = CenterRepository.getCenterRepository().getListOfStoryProducts().get(i).getItemName();
                storyInfo.Title = CenterRepository.getCenterRepository().getListOfStoryProducts().get(i).getItemName();
                storyInfo.ProductPrice = CenterRepository.getCenterRepository().getListOfStoryProducts().get(i).getSellMRP();
                storyInfo.setLink(CenterRepository.getCenterRepository().getListOfStoryProducts().get(i).getImageURL());
                storyInfos.add(storyInfo);
            }
        }
        return storyInfos;
    }

    @Override
    public int getItemCount() {
        return shopModelList == null ? 0 : shopModelList.size();
    }

    public void SetOnItemClickListener(
            final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    class VersionViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        TextView itemName, itemDesc, itemCost, availability, quanitity,
                addItem, removeItem, tv_time;
        ImageView imagView;
        StoryRecyclerViewAdapter mStoryRVAdapter = new StoryRecyclerViewAdapter();
        private RecyclerView rv_stories;


        public VersionViewHolder(View itemView) {
            super(itemView);

            itemName = (TextView) itemView.findViewById(R.id.item_name);

            itemDesc = (TextView) itemView.findViewById(R.id.item_short_desc);

            itemName.setSelected(true);

            imagView = ((ImageView) itemView.findViewById(R.id.imageView));

            tv_time = itemView.findViewById(R.id.tv_time);

            availability = itemView.findViewById(R.id.availibility);

            rv_stories = itemView.findViewById(R.id.rv_stories);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());
        }
    }

}