/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */
package com.ecommerce.retailapp.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.appcompat.widget.Toolbar;
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

import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.model.CenterRepository;
import com.ecommerce.retailapp.model.entities.Money;
import com.ecommerce.retailapp.model.entities.Product;
import com.ecommerce.retailapp.utils.ColorGenerator;
import com.ecommerce.retailapp.utils.Utils;
import com.ecommerce.retailapp.utils.Utils.AnimationType;
import com.ecommerce.retailapp.view.activities.ECartHomeActivity;
import com.ecommerce.retailapp.view.adapters.SimilarProductsPagerAdapter;
import com.ecommerce.retailapp.view.customview.ClickableViewPager;
import com.ecommerce.retailapp.view.customview.ClickableViewPager.OnItemClickListener;
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
public class ProductDetailsFragment extends Fragment implements OnClickListener {


    private int productListNumber;
    private ImageView itemImage;
    private  ImageView iv_back;
    private TextView itemSellPrice;
    private TextView itemName;
    private TextView tv_product_name;
    private TextView quanitity;
    private TextView itemdescription;
    private IBuilder mDrawableBuilder;
    private TextDrawable drawable;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private String subcategoryKey;
    private boolean isFromCart;

    /**
     * Instantiates a new product details fragment.
     */
    public ProductDetailsFragment(String subcategoryKey, int productNumber,
                                  boolean isFromCart) {

        this.subcategoryKey = subcategoryKey;
        this.productListNumber = productNumber;
        this.isFromCart = isFromCart;
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

                        if (isFromCart) {

                            //Update Quantity on shopping List
                            CenterRepository
                                    .getCenterRepository()
                                    .getListOfProductsInShoppingList()
                                    .get(productListNumber)
                                    .setQuantity(
                                            String.valueOf(

                                                    Integer.valueOf(CenterRepository
                                                            .getCenterRepository()
                                                            .getListOfProductsInShoppingList()
                                                            .get(productListNumber)
                                                            .getQuantity()) + 1));


                            //Update Ui
                            quanitity.setText(CenterRepository
                                    .getCenterRepository().getListOfProductsInShoppingList()
                                    .get(productListNumber).getQuantity());

                            Utils.vibrate(getActivity());

                            //Update checkout amount on screen
                            ((ECartHomeActivity) getActivity()).updateCheckOutAmount(
                                    BigDecimal.valueOf(Long
                                            .valueOf(CenterRepository
                                                    .getCenterRepository()
                                                    .getListOfProductsInShoppingList()
                                                    .get(productListNumber)
                                                    .getSellMRP())), true);

                        } else {

                            // current object
                            Product tempObj = CenterRepository
                                    .getCenterRepository().getMapOfProductsInCategory()
                                    .get(subcategoryKey).get(productListNumber);

                            // if current object is lready in shopping list
                            if (CenterRepository.getCenterRepository()
                                    .getListOfProductsInShoppingList().contains(tempObj)) {

                                // get position of current item in shopping list
                                int indexOfTempInShopingList = CenterRepository
                                        .getCenterRepository().getListOfProductsInShoppingList()
                                        .indexOf(tempObj);

                                // increase quantity of current item in shopping
                                // list
                                if (Integer.parseInt(tempObj.getQuantity()) == 0) {

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
                                                        .valueOf(tempObj
                                                                .getQuantity()) + 1));

                                // update checkout amount
                                ((ECartHomeActivity) getContext()).updateCheckOutAmount(
                                        BigDecimal.valueOf(Long
                                                .valueOf(CenterRepository
                                                        .getCenterRepository()
                                                        .getMapOfProductsInCategory()
                                                        .get(subcategoryKey)
                                                        .get(productListNumber)
                                                        .getSellMRP())), true);

                                // update current item quanitity

                                    quanitity.setText(tempObj.getQuantity());

                            } else {

                                ((ECartHomeActivity) getContext())
                                        .updateItemCount(true);

                                tempObj.setQuantity(String.valueOf(1));

                                quanitity.setText(tempObj.getQuantity());

                                CenterRepository.getCenterRepository()
                                        .getListOfProductsInShoppingList().add(tempObj);

                                ((ECartHomeActivity) getContext()).updateCheckOutAmount(
                                        BigDecimal.valueOf(Long
                                                .valueOf(CenterRepository
                                                        .getCenterRepository()
                                                        .getMapOfProductsInCategory()
                                                        .get(subcategoryKey)
                                                        .get(productListNumber)
                                                        .getSellMRP())), true);

                            }

                            Utils.vibrate(getContext());

                        }
                    }

                });

        rootView.findViewById(R.id.remove_item).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (isFromCart)

                        {

                            if (Integer.valueOf(CenterRepository
                                    .getCenterRepository().getListOfProductsInShoppingList()
                                    .get(productListNumber).getQuantity()) > 2) {

                                CenterRepository
                                        .getCenterRepository()
                                        .getListOfProductsInShoppingList()
                                        .get(productListNumber)
                                        .setQuantity(
                                                String.valueOf(

                                                        Integer.valueOf(CenterRepository
                                                                .getCenterRepository()
                                                                .getListOfProductsInShoppingList()
                                                                .get(productListNumber)
                                                                .getQuantity()) - 1));

                                quanitity.setText(CenterRepository
                                        .getCenterRepository().getListOfProductsInShoppingList()
                                        .get(productListNumber).getQuantity());

                                ((ECartHomeActivity) getActivity()).updateCheckOutAmount(
                                        BigDecimal.valueOf(Long
                                                .valueOf(CenterRepository
                                                        .getCenterRepository()
                                                        .getListOfProductsInShoppingList()
                                                        .get(productListNumber)
                                                        .getSellMRP())), false);

                                Utils.vibrate(getActivity());
                            } else if (Integer.valueOf(CenterRepository
                                    .getCenterRepository().getListOfProductsInShoppingList()
                                    .get(productListNumber).getQuantity()) == 1) {
                                ((ECartHomeActivity) getActivity())
                                        .updateItemCount(false);

                                ((ECartHomeActivity) getActivity()).updateCheckOutAmount(
                                        BigDecimal.valueOf(Long
                                                .valueOf(CenterRepository
                                                        .getCenterRepository()
                                                        .getListOfProductsInShoppingList()
                                                        .get(productListNumber)
                                                        .getSellMRP())), false);

                                CenterRepository.getCenterRepository()
                                        .getListOfProductsInShoppingList()
                                        .remove(productListNumber);

                                if (Integer
                                        .valueOf(((ECartHomeActivity) getActivity())
                                                .getItemCount()) == 0) {

                                    MyCartFragment.updateMyCartFragment(false);

                                }

                                Utils.vibrate(getActivity());

                            }

                        } else {

                            Product tempObj = CenterRepository
                                    .getCenterRepository().getMapOfProductsInCategory()
                                    .get(subcategoryKey).get(productListNumber);

                            if (CenterRepository.getCenterRepository()
                                    .getListOfProductsInShoppingList().contains(tempObj)) {

                                int indexOfTempInShopingList = CenterRepository
                                        .getCenterRepository().getListOfProductsInShoppingList()
                                        .indexOf(tempObj);

                                if (Integer.valueOf(tempObj.getQuantity()) != 0) {

                                    CenterRepository
                                            .getCenterRepository()
                                            .getListOfProductsInShoppingList()
                                            .get(indexOfTempInShopingList)
                                            .setQuantity(
                                                    String.valueOf(Integer.valueOf(tempObj
                                                            .getQuantity()) - 1));

                                    ((ECartHomeActivity) getContext()).updateCheckOutAmount(
                                            BigDecimal.valueOf(Long
                                                    .valueOf(CenterRepository
                                                            .getCenterRepository()
                                                            .getMapOfProductsInCategory()
                                                            .get(subcategoryKey)
                                                            .get(productListNumber)
                                                            .getSellMRP())),
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

        if (!isFromCart) {


            //Fetch and display item from Gloabl Data Model

            itemName.setText(CenterRepository.getCenterRepository()
                    .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                    .getItemName());
            tv_product_name.setText(CenterRepository.getCenterRepository()
                    .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                    .getItemName());

            quanitity.setText(CenterRepository.getCenterRepository()
                    .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                    .getQuantity());

            itemdescription.setText(CenterRepository.getCenterRepository()
                    .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                    .getItemDetail());

            String sellCostString = Money.albaniaCurrency(
                    BigDecimal.valueOf(Long.valueOf(CenterRepository
                            .getCenterRepository().getMapOfProductsInCategory()
                            .get(subcategoryKey).get(productListNumber)
                            .getSellMRP()))).toString()
                    + "  ";

            String buyMRP = "";
            if (BigDecimal.valueOf(Long.valueOf(CenterRepository
                    .getCenterRepository().getMapOfProductsInCategory()
                    .get(subcategoryKey).get(productListNumber)
                    .getMRP())).toString().equals("0") ){

            }else {
                 buyMRP = Money.albaniaCurrency(
                        BigDecimal.valueOf(Long.valueOf(CenterRepository
                                .getCenterRepository().getMapOfProductsInCategory()
                                .get(subcategoryKey).get(productListNumber)
                                .getMRP()))).toString();
            }

            String costString = sellCostString + buyMRP;

            itemSellPrice.setText(costString, BufferType.SPANNABLE);

            Spannable spannable = (Spannable) itemSellPrice.getText();

            spannable.setSpan(new StrikethroughSpan(), sellCostString.length(),
                    costString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            mDrawableBuilder = TextDrawable.builder().beginConfig()
                    .withBorder(4).endConfig().roundRect(10);

            drawable = mDrawableBuilder.build(
                    String.valueOf(CenterRepository.getCenterRepository()
                            .getMapOfProductsInCategory().get(subcategoryKey)
                            .get(productListNumber).getItemName().charAt(0)),
                    mColorGenerator.getColor(CenterRepository
                            .getCenterRepository().getMapOfProductsInCategory()
                            .get(subcategoryKey).get(productListNumber)
                            .getItemName()));

            Picasso.with(getActivity())
                    .load(CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                            .get(subcategoryKey).get(productListNumber)
                            .getImageURL()).placeholder(drawable)
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
                                    .load(CenterRepository.getCenterRepository()
                                            .getMapOfProductsInCategory()
                                            .get(subcategoryKey)
                                            .get(productListNumber)
                                            .getImageURL())
                                    .placeholder(drawable).error(drawable)
                                    .fit().centerCrop().into(itemImage);
                        }
                    });

            LabelView label = new LabelView(getActivity());

            label.setText(CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                    .get(subcategoryKey).get(productListNumber).getDiscount());
            label.setBackgroundColor(0xffE91E63);

            label.setTargetView(itemImage, 10, LabelView.Gravity.RIGHT_TOP);
        } else {


            //Fetch and display products from Shopping list

            itemName.setText(CenterRepository.getCenterRepository()
                    .getListOfProductsInShoppingList().get(productListNumber).getItemName());
            tv_product_name.setText(CenterRepository.getCenterRepository()
                    .getListOfProductsInShoppingList().get(productListNumber).getItemName());

            quanitity.setText(CenterRepository.getCenterRepository()
                    .getListOfProductsInShoppingList().get(productListNumber).getQuantity());

            itemdescription.setText(CenterRepository.getCenterRepository()
                    .getListOfProductsInShoppingList().get(productListNumber).getItemDetail());

            String sellCostString = Money.albaniaCurrency(
                    BigDecimal.valueOf(Long.valueOf(CenterRepository
                            .getCenterRepository().getListOfProductsInShoppingList()
                            .get(productListNumber).getSellMRP()))).toString()
                    + "  ";

            String buyMRP = "";
            if (BigDecimal.valueOf(Long.valueOf(CenterRepository
                    .getCenterRepository().getListOfProductsInShoppingList()
                    .get(productListNumber).getMRP())).toString().equals("0")){

            }else {
                 buyMRP = Money.albaniaCurrency(
                        BigDecimal.valueOf(Long.valueOf(CenterRepository
                                .getCenterRepository().getListOfProductsInShoppingList()
                                .get(productListNumber).getMRP()))).toString();

            }
            String costString = sellCostString + buyMRP;

            itemSellPrice.setText(costString, BufferType.SPANNABLE);

            Spannable spannable = (Spannable) itemSellPrice.getText();

            spannable.setSpan(new StrikethroughSpan(), sellCostString.length(),
                    costString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            mDrawableBuilder = TextDrawable.builder().beginConfig()
                    .withBorder(4).endConfig().roundRect(10);

            drawable = mDrawableBuilder.build(
                    String.valueOf(CenterRepository.getCenterRepository()
                            .getListOfProductsInShoppingList().get(productListNumber)
                            .getItemName().charAt(0)),
                    mColorGenerator.getColor(CenterRepository
                            .getCenterRepository().getListOfProductsInShoppingList()
                            .get(productListNumber).getItemName()));

            Picasso.with(getActivity())
                    .load(CenterRepository.getCenterRepository()
                            .getListOfProductsInShoppingList().get(productListNumber)
                            .getImageURL()).placeholder(drawable)
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
                                    .load(CenterRepository.getCenterRepository()
                                            .getListOfProductsInShoppingList()
                                            .get(productListNumber)
                                            .getImageURL())
                                    .placeholder(drawable).error(drawable)
                                    .fit().centerCrop().into(itemImage);
                        }
                    });

            LabelView label = new LabelView(getActivity());

            label.setText(CenterRepository.getCenterRepository()
                    .getListOfProductsInShoppingList().get(productListNumber).getDiscount());
            label.setBackgroundColor(0xffE91E63);

            label.setTargetView(itemImage, 10, LabelView.Gravity.RIGHT_TOP);

        }
    }

    public void goBack(){
        if (isFromCart) {

            Utils.switchContent(R.id.frag_container,
                    Utils.SHOPPING_LIST_TAG,
                    ((ECartHomeActivity) (getActivity())),
                    AnimationType.SLIDE_UP);
        } else {

            getActivity().onBackPressed();
        }

    }

    @Override
    public void onClick(View view) {
        if (view == iv_back){
            goBack();
        }
    }
}
