/*
 * Copyright (c) 2020. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.ecommerce.retailapp.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.domain.mining.FrequentItemsetData;
import com.ecommerce.retailapp.domain.mock.RequestFunction;
import com.ecommerce.retailapp.model.CenterRepository;
import com.ecommerce.retailapp.model.entities.Money;
import com.ecommerce.retailapp.model.entities.Product;
import com.ecommerce.retailapp.view.activities.APrioriResultActivity;
import com.ecommerce.retailapp.view.activities.ECartHomeActivity;
import com.ecommerce.retailapp.view.adapters.ReceiptProductListAdapter;
import com.example.connectionframework.requestframework.receiver.ReceiverBridge;
import com.example.connectionframework.requestframework.receiver.ReceiverBridgeInterface;
import com.example.connectionframework.requestframework.sender.RequestFunctions;
import com.example.connectionframework.requestframework.sender.SenderBridge;
import com.google.android.material.snackbar.Snackbar;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CheckoutPaymentFragmnet extends Fragment  {
    public static String CHECKOUT_DATA = "CHECKOUT_DATA";
    private List<Product> productList;
    private String cashOutAmount;
    private static boolean isCashPayment;
    private static Context contextEc;
    private ListView receiptListView;
    private RelativeLayout card_payment, cash_payment;

    public static CheckoutPaymentFragmnet newInstance(Bundle args, boolean isCashPaymentArg, Context context){
        CheckoutPaymentFragmnet checkoutPaymentFragmnet = new CheckoutPaymentFragmnet();
        checkoutPaymentFragmnet.setArguments(args);
        isCashPayment = isCashPaymentArg;
        contextEc = context;
        return checkoutPaymentFragmnet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        if (isCashPayment) {
            view = inflater.inflate(R.layout.frag_checkout_payment, container, false);
        }
        receiptListView = view.findViewById(R.id.receipt_list);

        return view;

    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() !=null){
           productList = (List<Product>) getArguments().getSerializable(CHECKOUT_DATA);
           cashOutAmount = (String) getArguments().getSerializable("TOTAL_AMOUNT");
            //CenterRepository.getCenterRepository().getListOfProductsInShoppingList().clear();
        }
        showReceipt();

    }


    public void showReceipt(){
        ReceiptProductListAdapter receiptProductListAdapter = new ReceiptProductListAdapter(productList, getContext());
        receiptListView.setAdapter(receiptProductListAdapter);

        SenderBridge senderBridge = new SenderBridge(getContext());
        senderBridge.sendMessageAssync(RequestFunction.getCategories(), getContext());

        //((ECartHomeActivity) contextEc).clearOrderList();
    }



}
