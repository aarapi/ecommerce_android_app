/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.ecommerce.retailapp.view.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.domain.api.ProductLoaderTask;
import com.ecommerce.retailapp.domain.mock.RequestFunction;
import com.ecommerce.retailapp.model.CenterRepository;
import com.ecommerce.retailapp.model.entities.Product;
import com.ecommerce.retailapp.utils.AppConstants;
import com.ecommerce.retailapp.utils.Utils;
import com.ecommerce.retailapp.utils.Utils.AnimationType;
import com.ecommerce.retailapp.view.activities.ECartHomeActivity;
import com.ecommerce.retailapp.view.adapters.ProductListAdapter;
import com.ecommerce.retailapp.view.adapters.ProductListAdapter.OnItemClickListener;
import com.example.connectionframework.requestframework.constants.MessagingFrameworkConstant;
import com.example.connectionframework.requestframework.receiver.ReceiverBridgeInterface;
import com.example.connectionframework.requestframework.sender.Message;
import com.example.connectionframework.requestframework.sender.Repository;
import com.example.connectionframework.requestframework.sender.SenderBridge;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.connectionframework.requestframework.sender.SenderBridge.returnMessage;

public class ProductListFragment extends Fragment {
    private String subcategoryKey;
    private boolean isShoppingList;
    private String shopName;
    private boolean isLoading = false;
    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;
    private ProductListAdapter adapter;


    public ProductListFragment() {
        isShoppingList = true;
    }

    public ProductListFragment(String subcategoryKey, String shopname) {

        isShoppingList = false;
        this.subcategoryKey = subcategoryKey;
        this.shopName = shopname;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_product_list_fragment, container,
                false);
        progressBar = view.findViewById(R.id.progress);
        relativeLayout = view.findViewById(R.id.rl_error_server);

        if (isShoppingList) {
            view.findViewById(R.id.slide_down).setVisibility(View.VISIBLE);
            view.findViewById(R.id.slide_down).setOnTouchListener(
                    new OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            Utils.switchFragmentWithAnimation(
                                    R.id.frag_container,
                                    new HomeFragment(),
                                    ((ECartHomeActivity) (getContext())), Utils.HOME_FRAGMENT,
                                    AnimationType.SLIDE_DOWN);

                            return false;
                        }
                    });
        }

        // Fill Recycler View

        RecyclerView recyclerView = (RecyclerView) view
                .findViewById(R.id.product_list_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new ProductListAdapter(subcategoryKey,
                getActivity(), isShoppingList);

        if (CenterRepository.getCenterRepository().getMapOfProductsInCategory().get(subcategoryKey).size() == 0) {

            final ReceiverBridgeInterface receiverBridgeInterface = new ReceiverBridgeInterface() {
                @Override
                public void onDataReceive(String jsonrRsponse) {
                    final Message message = returnMessage(jsonrRsponse);
                    if (message.getStatusCode() == MessagingFrameworkConstant.STATUS_CODES.Success) {
                        Gson gson = new Gson();
                        Type founderProductsType = new TypeToken<ArrayList<Product>>() {
                        }.getType();

                        ArrayList<Product> products = gson.fromJson(gson.toJson(message.getData().get(0)),
                                founderProductsType);

                        adapter.setProductList(products);
                        CenterRepository.getCenterRepository().getMapOfProductsInCategory().put(subcategoryKey, products);
                    }
                    if (((ECartHomeActivity) getContext()) != null) {
                        ((ECartHomeActivity) getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);

                                isLoading = false;
                                if (message.getStatusCode() != MessagingFrameworkConstant.STATUS_CODES.Success) {
                                    relativeLayout.setVisibility(View.VISIBLE);
                                    ((TextView) view.findViewById(R.id.tv_error_message))
                                            .setText(Repository.newInstance()
                                                    .getMessageError());
                                }
                            }
                        });
                    }
                }
            };

            if (!isLoading) {
                sendRequest(receiverBridgeInterface);
            }

            view.findViewById(R.id.ll_retry).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    relativeLayout.setVisibility(View.GONE);
                    sendRequest(receiverBridgeInterface);
                }
            });
        } else {
            adapter.setProductList(CenterRepository.getCenterRepository().getMapOfProductsInCategory().get(subcategoryKey));
            adapter.notifyDataSetChanged();
        }

        recyclerView.setAdapter(adapter);

        adapter.SetOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                Utils.addFragmentWithAnimation(R.id.frag_container,
                        new ProductDetailsFragment(subcategoryKey,
                                position,
                                false),
                        ((ECartHomeActivity) (getContext())),
                        null,
                        AnimationType.SLIDE_IN_SLIDE_OUT);

            }
        });

        // Handle Back press
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {

                    if (AppConstants.isFromShop) {
                        Utils.switchContent(R.id.frag_container,
                                Utils.SHOP_FRAGMENT,
                                ((ECartHomeActivity) (getContext())),
                                AnimationType.SLIDE_RIGHT);
                    } else {
                        Utils.switchContent(R.id.frag_container,
                                Utils.HOME_FRAGMENT,
                                ((ECartHomeActivity) (getContext())),
                                AnimationType.SLIDE_RIGHT);
                        AppConstants.isFromShop = true;
                    }

                }
                return true;
            }
        });



        return view;
    }

    private void sendRequest(ReceiverBridgeInterface receiverBridgeInterface) {
        SenderBridge senderBridge = new SenderBridge();
        senderBridge.sendMessageAssync(RequestFunction.getProductPAgination(subcategoryKey,
                shopName),
                receiverBridgeInterface);
        progressBar.setVisibility(View.VISIBLE);
        isLoading = true;
    }
}