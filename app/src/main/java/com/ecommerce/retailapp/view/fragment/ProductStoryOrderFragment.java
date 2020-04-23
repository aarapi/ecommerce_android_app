/*
 * Copyright (c) 2020. http://annoyingprojects.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Albi Arapi <albiarapi1@gmail.com>, 2020.
 */
package com.ecommerce.retailapp.view.fragment;

import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import androidx.fragment.app.Fragment;

import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.model.CenterRepository;
import com.ecommerce.retailapp.model.entities.Money;
import com.ecommerce.retailapp.model.entities.Product;
import com.ecommerce.retailapp.utils.ColorGenerator;
import com.ecommerce.retailapp.utils.Utils;
import com.ecommerce.retailapp.utils.Utils.AnimationType;
import com.ecommerce.retailapp.view.activities.ECartHomeActivity;
import com.ecommerce.retailapp.view.customview.LabelView;
import com.ecommerce.retailapp.view.customview.TextDrawable;
import com.ecommerce.retailapp.view.customview.TextDrawable.IBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;

// TODO: Auto-generated Javadoc

/**
 * Fragment that appears in the "content_frame", shows a animal.
 */
public class ProductStoryOrderFragment extends Fragment implements OnClickListener {


    private ImageView itemImage;
    private ImageView iv_back;
    private TextView itemSellPrice;
    private TextView itemName;
    private TextView tv_product_name;
    private TextView quanitity;
    private TextView itemdescription;
    private IBuilder mDrawableBuilder;
    private TextDrawable drawable;
    private Product product;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

    /**
     * Instantiates a new product details fragment.
     */
    public ProductStoryOrderFragment(Product product) {
        this.product = product;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_product_detail,
                container, false);


        ((ECartHomeActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);


        itemSellPrice = ((TextView) rootView
                .findViewById(R.id.category_discount));

        quanitity = ((TextView) rootView.findViewById(R.id.iteam_amount));

        itemName = ((TextView) rootView.findViewById(R.id.product_name));
        tv_product_name = rootView.findViewById(R.id.tv_product_name);

        itemdescription = ((TextView) rootView
                .findViewById(R.id.product_description));

        itemImage = (ImageView) rootView.findViewById(R.id.product_image);
        iv_back = rootView.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);

        fillProductData();

        rootView.findViewById(R.id.add_item).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        // if current object is lready in shopping list
                        if (CenterRepository.getCenterRepository()
                                .getListOfProductsInShoppingList().contains(product)) {

                            // get position of current item in shopping list
                            int indexOfTempInShopingList = CenterRepository
                                    .getCenterRepository().getListOfProductsInShoppingList()
                                    .indexOf(product);

                            // increase quantity of current item in shopping
                            // list
                            if (Integer.parseInt(product.getQuantity()) == 0) {

                                ((ECartHomeActivity) getContext())
                                        .updateItemCount(true);

                            }

                            // update quanity in shopping list
                            CenterRepository
                                    .getCenterRepository()
                                    .getListOfProductsInShoppingList()
                                    .get(indexOfTempInShopingList)
                                    .setQuantity(
                                            String.valueOf(Integer
                                                    .valueOf(product
                                                            .getQuantity()) + 1));

                            // update checkout amount
                            ((ECartHomeActivity) getContext()).updateCheckOutAmount(
                                    BigDecimal.valueOf(Long
                                            .valueOf(product.getSellMRP())), true);

                            // update current item quanitity

                            quanitity.setText(product.getQuantity());

                        } else {

                            ((ECartHomeActivity) getContext())
                                    .updateItemCount(true);

                            product.setQuantity(String.valueOf(1));

                            quanitity.setText(product.getQuantity());

                            CenterRepository.getCenterRepository()
                                    .getListOfProductsInShoppingList().add(product);

                            ((ECartHomeActivity) getContext()).updateCheckOutAmount(
                                    BigDecimal.valueOf(Long
                                            .valueOf(product.getSellMRP())), true);
                        }
                        Utils.vibrate(getContext());
                    }

                });

        rootView.findViewById(R.id.remove_item).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (CenterRepository.getCenterRepository()
                                .getListOfProductsInShoppingList().contains(product)) {

                            int indexOfTempInShopingList = CenterRepository
                                    .getCenterRepository().getListOfProductsInShoppingList()
                                    .indexOf(product);

                            if (Integer.valueOf(product.getQuantity()) != 0) {

                                CenterRepository
                                        .getCenterRepository()
                                        .getListOfProductsInShoppingList()
                                        .get(indexOfTempInShopingList)
                                        .setQuantity(
                                                String.valueOf(Integer.valueOf(product
                                                        .getQuantity()) - 1));

                                ((ECartHomeActivity) getContext()).updateCheckOutAmount(
                                        BigDecimal.valueOf(Long
                                                .valueOf(product.getSellMRP())),
                                        false);

                                quanitity.setText(CenterRepository
                                        .getCenterRepository()
                                        .getListOfProductsInShoppingList()
                                        .get(indexOfTempInShopingList)
                                        .getQuantity());

                                Utils.vibrate(getContext());

                                if (Integer.valueOf(CenterRepository
                                        .getCenterRepository()
                                        .getListOfProductsInShoppingList()
                                        .get(indexOfTempInShopingList)
                                        .getQuantity()) == 0) {

                                    CenterRepository
                                            .getCenterRepository()
                                            .getListOfProductsInShoppingList()
                                            .remove(indexOfTempInShopingList);

                                    ((ECartHomeActivity) getContext())
                                            .updateItemCount(false);

                                }

                            }

                        } else {

                        }

                    }

                });

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {
                    goBack();

                }
                return true;
            }
        });

        return rootView;
    }


    public void fillProductData() {

        //Fetch and display item from Gloabl Data Model

        itemName.setText(product.getItemName());
        tv_product_name.setText(product.getItemName());

        quanitity.setText(product.getQuantity());

        itemdescription.setText(product.getItemDetail());

        String sellCostString = Money.albaniaCurrency(
                BigDecimal.valueOf(Long.valueOf(product.getSellMRP()))).toString()
                + "  ";

        String buyMRP = "";
        if (BigDecimal.valueOf(Long.valueOf(product.getMRP())).toString().equals("0")) {

        } else {
            buyMRP = Money.albaniaCurrency(
                    BigDecimal.valueOf(Long.valueOf(product.getMRP()))).toString();
        }

        String costString = sellCostString + buyMRP;

        itemSellPrice.setText(costString, BufferType.SPANNABLE);

        Spannable spannable = (Spannable) itemSellPrice.getText();

        spannable.setSpan(new StrikethroughSpan(), sellCostString.length(),
                costString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mDrawableBuilder = TextDrawable.builder().beginConfig()
                .withBorder(4).endConfig().roundRect(10);

        drawable = mDrawableBuilder.build(
                String.valueOf(product.getItemName().charAt(0)),
                mColorGenerator.getColor(product.getItemName()));

        Picasso.with(getActivity())
                .load(product.getImageURL()).placeholder(drawable)
                .error(drawable).fit().centerCrop()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(itemImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        // Try again online if cache failed

                        Picasso.with(getActivity())
                                .load(product.getImageURL())
                                .placeholder(drawable).error(drawable)
                                .fit().centerCrop().into(itemImage);
                    }
                });


    }

    public void goBack() {

        getActivity().onBackPressed();


    }

    @Override
    public void onClick(View view) {
        if (view == iv_back) {
            goBack();
        }
    }
}
