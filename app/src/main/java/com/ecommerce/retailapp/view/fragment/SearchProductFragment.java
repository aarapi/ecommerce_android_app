/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.ecommerce.retailapp.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.model.CenterRepository;
import com.ecommerce.retailapp.model.entities.Product;
import com.ecommerce.retailapp.utils.AppConstants;
import com.ecommerce.retailapp.utils.Utils;
import com.ecommerce.retailapp.view.activities.ECartHomeActivity;
import com.ecommerce.retailapp.view.adapters.ProductListAdapter;
import com.example.connectionframework.requestframework.sender.Repository;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SearchProductFragment extends BottomSheetDialogFragment implements TextWatcher {

    private static final int REQ_SCAN_RESULT = 200;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    ArrayList<Product> searchProductList = new ArrayList<>();
    boolean searchInProgress = false;
    private EditText serchInput;
    private RecyclerView recyclerView;
    private AVLoadingIndicatorView loading_bar;
    private Context context;
    private RelativeLayout rl_error_server;
    private TextView tv_error_message, tv_cancel;
    private boolean isHome;


    private View rootView;

    public  SearchProductFragment (Context context, boolean isHome) {
        this.isHome = isHome;
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.frag_search_product,
                container, false);

        rl_error_server = rootView.findViewById(R.id.rl_error_server);
        tv_error_message = rootView.findViewById(R.id.tv_error_message);

        tv_cancel = (TextView) rootView.findViewById(R.id.tv_cancel);

        loading_bar = rootView.findViewById(R.id.loading_bar);

        serchInput = (EditText) rootView.findViewById(R.id.edt_search_input);
        serchInput.addTextChangedListener(this);

        serchInput.setSelected(true);


        recyclerView = (RecyclerView) rootView
                .findViewById(R.id.product_list_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        setSearchProductList(null);
        getSearchedProducts();


        tv_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        return rootView;

    }


    public void getSearchedProducts(){
        if (CenterRepository.getCenterRepository().getListOfSearchedProducts() != null) {
            ProductListAdapter adapter = new ProductListAdapter(getContext());
            recyclerView.setAdapter(adapter);
            adapter.SetOnItemClickListener(new ProductListAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {
//                    Utils.addFragmentWithAnimation(R.id.frag_container,
//                            new ProductDetailsFragment("", position, false),
//                            ((ECartHomeActivity) (getContext())), null,
//                            Utils.AnimationType.SLIDE_IN_SLIDE_OUT);

                }
            });
            adapter.notifyDataSetChanged();

            recyclerView.setVisibility(View.VISIBLE);
        }else {
            rl_error_server.setVisibility(View.VISIBLE);
        }

    }


    private void setSearchProductList(String searchString){
        Set<String> keySet = CenterRepository.getCenterRepository().getMapAllProducts().keySet();
        if (searchString == null) {
//            for (String string : keySet) {
//                CenterRepository.getCenterRepository().setListOfSearchedProducts(CenterRepository.getCenterRepository().getMapAllProducts().get(string));
//                break;
//            }
        }else {
            List<Product> searchedProducts = new ArrayList<>();

            for (String string : keySet) {
                ArrayList<Product> products = CenterRepository.getCenterRepository().getMapAllProducts().get(string);
                int sizeProducts = products.size();
                for (int j =0; j<sizeProducts;j++){
                    if (isHome){
                        if (products.get(j).getItemName().toLowerCase().contains(searchString.toLowerCase())){
                            searchedProducts.add(products.get(j));
                        }
                    }else {
                        if (products.get(j).getItemName().toLowerCase().contains(searchString.toLowerCase())
                                && products.get(j).getShopName().equals(CenterRepository.getCenterRepository()
                                .getShopsOfCategory().get(AppConstants.CURRENT_SHOP).getShopName())){
                            searchedProducts.add(products.get(j));
                        }
                    }
                }
            }
            CenterRepository.getCenterRepository().setListOfSearchedProducts(searchedProducts);

        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        setSearchProductList(charSequence.toString());
        getSearchedProducts();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
