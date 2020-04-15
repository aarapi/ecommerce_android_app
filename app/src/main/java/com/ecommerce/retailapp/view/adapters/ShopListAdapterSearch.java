/*
 * Copyright (c) 2020. http://annoyingprojects.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Albi Arapi <albiarapi1@gmail.com>, 2020.
 */

package com.ecommerce.retailapp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.model.CenterRepository;
import com.ecommerce.retailapp.model.entities.ShopModel;
import com.ecommerce.retailapp.utils.ColorGenerator;
import com.ecommerce.retailapp.view.customview.TextDrawable;
import com.ecommerce.retailapp.view.customview.TextDrawable.IBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Hitesh Sahu (hiteshsahu.com)
 */
public class ShopListAdapterSearch extends
        RecyclerView.Adapter<ShopListAdapterSearch.VersionViewHolder> implements
        ItemTouchHelperAdapter {

    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

    private IBuilder mDrawableBuilder;

    private TextDrawable drawable;

    private String ImageUrl;

    private ArrayList<ShopModel> productList = new ArrayList<ShopModel>();
    private OnItemClickListener clickListener;

    private Context context;

    public ShopListAdapterSearch(Context context) {


            productList = CenterRepository.getCenterRepository().getListOfSearchedShops();


        this.context = context;
    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.item_shop_list_search, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VersionViewHolder holder,
                                 final int position) {

        holder.itemName.setText(productList.get(position)
                .getShopName());
        holder.itemDesc.setText(productList.get(position).getShopDescription());
        String openingTime = new SimpleDateFormat("HH:mm").format(productList.get(position).getOpenTime().getTime());
        String closeTime = new SimpleDateFormat("HH:mm").format(productList.get(position).getCloseTime().getTime());
        if (productList.get(position).getAvalibility() == 0){
            holder.availability.setText("Closed");
            holder.availability.setTextColor(context.getResources().getColor(R.color.red));
        }

        holder.tv_time.setText(openingTime + " - " + closeTime);

        mDrawableBuilder = TextDrawable.builder().beginConfig().withBorder(4)
                .endConfig().roundRect(10);

        drawable = mDrawableBuilder.build(String.valueOf(productList
                .get(position).getShopName().charAt(0)), mColorGenerator
                .getColor(productList.get(position).getShopName()));



        ImageUrl = productList.get(position).getShopImage();


        Glide.with(context).load(ImageUrl).placeholder(drawable)
                .error(drawable)
                .centerCrop().into(holder.imagView);

    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    public void SetOnItemClickListener(
            final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public void onItemDismiss(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(productList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(productList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    class VersionViewHolder extends RecyclerView.ViewHolder implements
            OnClickListener {
        TextView itemName, itemDesc, availability, tv_time;
        ImageView imagView;

        public VersionViewHolder(View itemView) {
            super(itemView);

            itemName = (TextView) itemView.findViewById(R.id.item_name);

            itemDesc = (TextView) itemView.findViewById(R.id.item_short_desc);

            availability = (TextView) itemView
                    .findViewById(R.id.iteam_avilable);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);

            itemName.setSelected(true);

            imagView = ((ImageView) itemView.findViewById(R.id.product_thumb));

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());
        }
    }

}
