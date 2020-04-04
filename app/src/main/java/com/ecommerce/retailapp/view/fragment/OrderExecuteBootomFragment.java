/*
 * Copyright (c) 2020. http://annoyingprojects.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Albi Arapi <albiarapi1@gmail.com>, 2020.
 */

package com.ecommerce.retailapp.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.domain.mock.RequestFunction;
import com.ecommerce.retailapp.model.entities.Product;
import com.ecommerce.retailapp.view.adapters.ReceiptProductListAdapter;
import com.example.connectionframework.requestframework.sender.SenderBridge;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import static com.ecommerce.retailapp.view.fragment.CheckoutPaymentFragmnet.CHECKOUT_DATA;

public class OrderExecuteBootomFragment extends BottomSheetDialogFragment
        implements View.OnClickListener {
    public static final String TAG = "ActionBottomDialog";
    private ItemClickListener mListener;
    private TextView order_button;
    private List<Product> productList;
    private String cashOutAmount;
    private EditText et_location, et_name, et_phone;
    private ListView receiptListView;
    private TextView total_amount;
    private static boolean isCashPayment;



    public static OrderExecuteBootomFragment newInstance(Bundle args, boolean isCashPaymentArg) {
        OrderExecuteBootomFragment orderExecuteBootomFragment = new OrderExecuteBootomFragment();
        orderExecuteBootomFragment.setArguments(args);
        isCashPayment = isCashPaymentArg;
        return orderExecuteBootomFragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bootom_order_information, container, false);
    }


    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() !=null){
            productList = (List<Product>) getArguments().getSerializable(CHECKOUT_DATA);
            cashOutAmount = (String) getArguments().getSerializable("TOTAL_AMOUNT");
            //CenterRepository.getCenterRepository().getListOfProductsInShoppingList().clear();
        }

        receiptListView = view.findViewById(R.id.receipt_list);
        total_amount = view.findViewById(R.id.checkout_amount);

        showReceipt();

        et_location = view.findViewById(R.id.et_location);
        et_name = view.findViewById(R.id.et_name);
        et_phone = view.findViewById(R.id.et_phone);

        order_button = view.findViewById(R.id.order_button);
        order_button.setOnClickListener(this);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override public void onClick(View view) {
        if (view == order_button){
            String[] inputValue = {et_location.getText().toString(), et_name.getText().toString(), et_phone.getText().toString()};
            if (validateInput(inputValue)){
                SenderBridge senderBridge = new SenderBridge(getContext());
                senderBridge.sendMessageAssync(RequestFunction.makeAnOrder(productList,inputValue), getContext());

            }
        }

    }
    public interface ItemClickListener {
        void onItemClick(String item);
    }


    public void showReceipt(){
        ReceiptProductListAdapter receiptProductListAdapter = new ReceiptProductListAdapter(productList, getContext());
        receiptListView.setAdapter(receiptProductListAdapter);

        total_amount.setText(cashOutAmount);

        //((ECartHomeActivity) contextEc).clearOrderList();
    }


    public boolean validateInput(String ... inputValue) {

        if (inputValue[0].isEmpty()) {
            et_location.setError("Location field can not be empty!");

        }
        if (inputValue[1].isEmpty()) {
            et_name.setError("Name field can not be empty!");
        }
        if (inputValue[2].isEmpty()) {
            et_phone.setError("Phone field can not be empty!");
        }

        if (inputValue[0].isEmpty() || inputValue[1].isEmpty() || inputValue[2].isEmpty())
        {
            return false;
        }

        return true;
    }
}