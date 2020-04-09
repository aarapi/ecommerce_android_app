/*
 * Copyright (c) 2020. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.ecommerce.retailapp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.model.entities.Product;
import com.ecommerce.retailapp.utils.ColorGenerator;
import com.ecommerce.retailapp.view.customview.TextDrawable;

import java.util.List;

public class ReceiptProductListAdapter extends ArrayAdapter<Product> implements View.OnClickListener {

    private List<Product> dataSet;
    private Context mContext;
    private TextDrawable.IBuilder mDrawableBuilder;
    private TextDrawable drawable;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private String ImageUrl;


    // View lookup cache
    private static class ViewHolder {
        TextView tv_productName, tv_product_quantity, tv_product_price, tv_product_description;
        ImageView product_mage;
    }

    public ReceiptProductListAdapter(List<Product> data, Context context) {
        super(context, R.layout.item_product_receipt, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Product dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_product_receipt, parent, false);

            viewHolder.tv_productName = convertView.findViewById(R.id.tv_productName);
            viewHolder.tv_product_quantity = convertView.findViewById(R.id.tv_product_quantity);
            viewHolder.tv_product_price = convertView.findViewById(R.id.tv_product_price);
            viewHolder.tv_product_description = convertView.findViewById(R.id.product_description);
            viewHolder.product_mage = convertView.findViewById(R.id.product_mage);


            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        mDrawableBuilder = TextDrawable.builder().beginConfig().withBorder(4)
                .endConfig().roundRect(10);

        drawable = mDrawableBuilder.build(String.valueOf(dataModel.getItemName().charAt(0)), mColorGenerator
                .getColor(dataModel.getItemName()));

        ImageUrl = dataModel.getImageURL();

        viewHolder.tv_productName.setText(dataModel.getItemName());
        viewHolder.tv_product_quantity.setText(dataModel.getQuantity());
        viewHolder.tv_product_price.setText(dataModel.getSellMRP());
        viewHolder.tv_product_description.setText(dataModel.getItemDetail());

        Glide.with(getContext()).load(ImageUrl).placeholder(drawable)
                .error(drawable)
                .centerCrop().into(viewHolder.product_mage);


        return convertView;
    }
}
