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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.model.CenterRepository;
import com.ecommerce.retailapp.model.entities.ProductCategoryModel;
import com.ecommerce.retailapp.model.entities.ShopModel;
import com.ecommerce.retailapp.utils.AppConstants;
import com.ecommerce.retailapp.utils.ColorGenerator;
import com.ecommerce.retailapp.view.customview.LabelView;
import com.ecommerce.retailapp.view.customview.TextDrawable;
import com.ecommerce.retailapp.view.customview.TextDrawable.IBuilder;

import java.util.ArrayList;
import java.util.List;

public class ShopListAdapter extends
        RecyclerView.Adapter<ShopListAdapter.VersionViewHolder> {

    public static List<ShopModel> shopModelList = new ArrayList<>();
    OnItemClickListener clickListener;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private IBuilder mDrawableBuilder;
    private TextDrawable drawable;
    private String ImageUrl;
    private Context context;

    public ShopListAdapter(Context context) {

        shopModelList = getListOfShop();

        this.context = context;
    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.item_category_list, viewGroup, false);
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

//        LabelView label = new LabelView(context);
//        label.setText(categoryList.get(categoryIndex)
//                .getProductCategoryDiscount());
//        label.setBackgroundColor(0xffE91E63);
//        label.setTargetView(versionViewHolder.imagView, 10,
//                LabelView.Gravity.RIGHT_TOP);

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
                addItem, removeItem;
        ImageView imagView;

        public VersionViewHolder(View itemView) {
            super(itemView);

            itemName = (TextView) itemView.findViewById(R.id.item_name);

            itemDesc = (TextView) itemView.findViewById(R.id.item_short_desc);

            itemName.setSelected(true);

            imagView = ((ImageView) itemView.findViewById(R.id.imageView));

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());
        }
    }

    private ArrayList<ShopModel> getListOfShop(){
        ArrayList<ShopModel> shopList = new ArrayList<>();
       int size =  CenterRepository.getCenterRepository().getListOfShop().size();

       for (int i =0; i< size; i++){
           if (CenterRepository.getCenterRepository().getListOfShop().get(i).getCategoryName()
                   .equals(CenterRepository.getCenterRepository().getListOfCategory().get(AppConstants.CURRENT_CATEGORY).getProductCategoryName())){
               shopList.add(CenterRepository.getCenterRepository().getListOfShop().get(i));
           }
       }

       return shopList;
    }

}
