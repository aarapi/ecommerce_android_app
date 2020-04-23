/*
 * Copyright (c) 2020. http://annoyingprojects.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Albi Arapi <albiarapi1@gmail.com>, 2020.
 */

package com.ecommerce.retailapp.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.domain.mock.RequestFunction;
import com.ecommerce.retailapp.model.entities.Product;
import com.ecommerce.retailapp.view.activities.ECartHomeActivity;
import com.ecommerce.retailapp.view.adapters.ReceiptProductListAdapter;
import com.example.connectionframework.requestframework.sender.SenderBridge;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class OrderExecuteBootomFragment extends Fragment
        implements View.OnClickListener {
    public static final String TAG = "ActionBottomDialog";
    private ItemClickListener mListener;
    private RelativeLayout order_button;
    private List<Product> productList;
    private String cashOutAmount;
    public static String CHECKOUT_DATA = "CHECKOUT_DATA";
    private EditText et_location, et_name, et_phone;
    private ListView receiptListView;
    private TextView total_amount, checkout_total_amount, tv_transport;
    private static boolean isCashPayment;
    private static ECartHomeActivity eCartHomeActivity;
    private SweetAlertDialog pDialog;
    private TextView tv_cancel;



    public static OrderExecuteBootomFragment newInstance(Bundle args,ECartHomeActivity eCartHomeActivityArg, boolean isCashPaymentArg) {
        OrderExecuteBootomFragment orderExecuteBootomFragment = new OrderExecuteBootomFragment();
        orderExecuteBootomFragment.setArguments(args);
        isCashPayment = isCashPaymentArg;
        eCartHomeActivity = eCartHomeActivityArg;
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
        ((ECartHomeActivity) getContext()).getCheckout_button().setVisibility(View.GONE);
        receiptListView = view.findViewById(R.id.receipt_list);
        total_amount = view.findViewById(R.id.checkout_amount);
        checkout_total_amount = view.findViewById(R.id.checkout_total_amount);
        tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_transport = view.findViewById(R.id.transport);
        tv_cancel.setOnClickListener(this);

        showReceipt();

        et_location = view.findViewById(R.id.et_location);
        et_location.setOnClickListener(this);
        et_location.setOnClickListener(this);
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
                 pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();

                SenderBridge senderBridge = new SenderBridge(getContext());
                senderBridge.sendMessageAssync(RequestFunction.makeAnOrder(productList,inputValue), getContext());
            }
        }else if (view == tv_cancel){
            hideKeyboard(getContext());
            getActivity().onBackPressed();
        }else if (view == et_location){

        }

    }

    public interface ItemClickListener {
        void onItemClick(String item);
    }


    public void showReceipt(){
        ReceiptProductListAdapter receiptProductListAdapter = new ReceiptProductListAdapter(productList, getContext());
        receiptListView.setAdapter(receiptProductListAdapter);

        total_amount.setText(cashOutAmount);

        String amount = cashOutAmount.substring(4).replaceAll("\\D+","");
        double transport = Long.valueOf(amount) * 0.02;
        if (transport < 150) {
            transport = 100;
        } else {
            transport = 200;
        }
        BigDecimal amountValue = BigDecimal.valueOf(Long.valueOf(amount) + transport);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);

        cashOutAmount = "ALL "+ decimalFormat.format(amountValue.doubleValue());

        checkout_total_amount.setText(cashOutAmount);
        tv_transport.setText(transport + "");



    }

    public RelativeLayout getOrder_button() {
        return order_button;
    }

    public SweetAlertDialog getpDialog() {
        return pDialog;
    }

    public boolean validateInput(String ... inputValue) {

        if (inputValue[0].isEmpty()) {
            et_location.setError("Plotesoni fushen!");
            return false;
        }else if (inputValue[1].isEmpty()){
            et_name.setError("Plotesoni fushen!");
            return false;
        }else if (inputValue[2].isEmpty()){
            et_phone.setError("Plotesoni fushen!");
        }




        return true;
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public void dismiss(){
        getActivity().onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((ECartHomeActivity) getContext()).getCheckout_button().setVisibility(View.VISIBLE);
    }

    public  void hideKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}