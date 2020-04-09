/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.ecommerce.retailapp.view.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.domain.mock.RequestFunction;
import com.ecommerce.retailapp.model.CenterRepository;
import com.ecommerce.retailapp.model.entities.Product;
import com.ecommerce.retailapp.utils.Utils;
import com.ecommerce.retailapp.utils.Utils.AnimationType;
import com.ecommerce.retailapp.view.activities.ECartHomeActivity;
import com.ecommerce.retailapp.view.adapters.ProductListAdapter;
import com.example.connectionframework.requestframework.sender.Repository;
import com.example.connectionframework.requestframework.sender.SenderBridge;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

public class SearchProductFragment extends BottomSheetDialogFragment {

    private static final int REQ_SCAN_RESULT = 200;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    ArrayList<Product> searchProductList = new ArrayList<>();
    boolean searchInProgress = false;
    private ImageButton btnSearch;
    private EditText serchInput;
    private RecyclerView recyclerView;
    private AVLoadingIndicatorView loading_bar;
    private Context context;
    private RelativeLayout rl_error_server;
    private TextView tv_error_message;


    private View rootView;

    public  SearchProductFragment (Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.frag_search_product,
                container, false);

        rl_error_server = rootView.findViewById(R.id.rl_error_server);
        tv_error_message = rootView.findViewById(R.id.tv_error_message);

        btnSearch = (ImageButton) rootView.findViewById(R.id.btn_search);

        loading_bar = rootView.findViewById(R.id.loading_bar);

        serchInput = (EditText) rootView.findViewById(R.id.edt_search_input);

        serchInput.setSelected(true);

        serchInput.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    sendMessage();
                    return true;
                }
                return false;
            }
        });

        recyclerView = (RecyclerView) rootView
                .findViewById(R.id.product_list_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              sendMessage();
            }
        });

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        return rootView;

    }


    public void sendMessage(){
        recyclerView.setVisibility(View.GONE);
        rl_error_server.setVisibility(View.GONE);
        loading_bar.setVisibility(View.VISIBLE);
        SenderBridge senderBridge = new SenderBridge(getContext());

        senderBridge.sendMessageAssync
                (RequestFunction.getSearchedProducts
                        (serchInput.getText().toString()), getContext());
    }

    public void onDataReceive(List<Object> data){

        Type founderListType = new TypeToken<ArrayList<Product>>(){}.getType();
        Gson gson = new Gson();
        if (data != null) {
            ArrayList<Product> products = gson.fromJson(gson.toJson(data.get(0)), founderListType);
            CenterRepository.getCenterRepository().setListOfSearchedProducts(products);
        }else
            CenterRepository.getCenterRepository().setListOfSearchedProducts(null);



        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loading_bar.setVisibility(View.GONE);
                if (CenterRepository.getCenterRepository().getListOfSearchedProducts() != null) {
                    ProductListAdapter adapter = new ProductListAdapter(getContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.SetOnItemClickListener(new ProductListAdapter.OnItemClickListener() {

                        @Override
                        public void onItemClick(View view, int position) {
//                            Utils.switchFragmentWithAnimation(R.id.frag_container,
//                                    new ProductDetailsFragment(
//                                            "SearchProducts",
//                                            position,
//                                            false),
//                                    ((ECartHomeActivity) (getContext())), null,
//                                    AnimationType.SLIDE_LEFT);
                        }
                    });
                }else {
                    rl_error_server.setVisibility(View.VISIBLE);
                    tv_error_message.setText(Repository.newInstance().getMessageError());
                }
            }
        });

    }

}
