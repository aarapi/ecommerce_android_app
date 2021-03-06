/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.ecommerce.retailapp.view.fragment;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.domain.api.ProductLoaderTask;
import com.ecommerce.retailapp.domain.mock.RequestFunction;
import com.ecommerce.retailapp.model.CenterRepository;
import com.ecommerce.retailapp.model.entities.Product;
import com.ecommerce.retailapp.model.entities.ShopModel;
import com.ecommerce.retailapp.utils.AppConstants;
import com.ecommerce.retailapp.utils.Utils;
import com.ecommerce.retailapp.view.activities.ECartHomeActivity;
import com.ecommerce.retailapp.view.adapters.ProductListAdapter;
import com.ecommerce.retailapp.view.adapters.ShopListAdapterSearch;
import com.example.connectionframework.requestframework.constants.MessagingFrameworkConstant;
import com.example.connectionframework.requestframework.receiver.ReceiverBridgeInterface;
import com.example.connectionframework.requestframework.sender.Message;
import com.example.connectionframework.requestframework.sender.Repository;
import com.example.connectionframework.requestframework.sender.SenderBridge;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.connectionframework.requestframework.sender.SenderBridge.returnMessage;

public class SearchProductFragment extends BottomSheetDialogFragment implements TextWatcher, ReceiverBridgeInterface {

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

    private String shopName;

    private ProgressBar progressBar;



    private View rootView;

    public SearchProductFragment(Context context, boolean isHome, String shopName) {
        this.isHome = isHome;
        this.context = context;
        this.shopName = shopName;
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
        progressBar = rootView.findViewById(R.id.progress);

        serchInput = (EditText) rootView.findViewById(R.id.edt_search_input);
        serchInput.addTextChangedListener(this);


        serchInput.setSelected(true);


        recyclerView = (RecyclerView) rootView
                .findViewById(R.id.product_list_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        if (isHome){
            serchInput.setHint("Kerko dyqanin");
            setSearchedShop(null);
            getSearchedShops();
        }else {
            setSearchProductList(null);
            getSearchedProducts();
        }

        tv_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideKeyboard(context);
                dismiss();
            }
        });

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        return rootView;

    }

    public void getSearchedShops(){
        if (CenterRepository.getCenterRepository().getListOfShop() != null){
            ShopListAdapterSearch adapter = new ShopListAdapterSearch(getContext());
            recyclerView.setAdapter(adapter);
            adapter.SetOnItemClickListener(new ShopListAdapterSearch.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {
                    AppConstants.isFromShop = false;
                    Utils.switchFragmentWithAnimation(
                            R.id.frag_container,
                            new ProductOverviewFragment( CenterRepository.getCenterRepository()
                                    .getListOfSearchedShops().get(position).getShopName(),
                                    CenterRepository.getCenterRepository()
                                            .getListOfSearchedShops().get(position).getCategoryName()),
                            ((ECartHomeActivity) context), null,
                            Utils.AnimationType.SLIDE_LEFT);
                    dismiss();
                }
            });
            adapter.notifyDataSetChanged();

            recyclerView.setVisibility(View.VISIBLE);
        }
    }
    private void setSearchedShop(String searchString){
        if (searchString == null) {
            CenterRepository.getCenterRepository().setListOfSearchedShops(CenterRepository.getCenterRepository().getListOfShop());
        }else {
            ArrayList<ShopModel> searchedProducts = new ArrayList<>();
            ArrayList<ShopModel> shopModels = CenterRepository.getCenterRepository().getListOfShop();
            int size = shopModels.size();

            for (int i=0; i < size; i++) {
                if (shopModels.get(i).getShopName().toLowerCase().contains(searchString.toLowerCase())){
                    searchedProducts.add(shopModels.get(i));
                }
            }
            CenterRepository.getCenterRepository().setListOfSearchedShops(searchedProducts);
        }

    }


    public void getSearchedProducts(){
        if (CenterRepository.getCenterRepository().getListOfSearchedProducts() != null) {
            ProductListAdapter adapter = new ProductListAdapter(getContext());
            recyclerView.setAdapter(adapter);
            adapter.SetOnItemClickListener(new ProductListAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {
                }
            });
            adapter.notifyDataSetChanged();

            recyclerView.setVisibility(View.VISIBLE);
        }else {
            rl_error_server.setVisibility(View.VISIBLE);
        }

    }


    private void setSearchProductList(String searchString){
        Set<String> keySet = CenterRepository.getCenterRepository().getMapOfProductsInCategory().keySet();
        if (searchString == null) {
            for (String string : keySet) {
                CenterRepository.getCenterRepository().setListOfSearchedProducts(
                        CenterRepository.getCenterRepository().getMapOfProductsInCategory().get(string));
                break;
            }
        }else {
            progressBar.setVisibility(View.VISIBLE);
            SenderBridge senderBridge = new SenderBridge();
            senderBridge.sendMessageAssync(RequestFunction.getSearchedProducts(searchString, shopName), this);

        }

    }






    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (isHome){
            setSearchedShop(charSequence.toString());
            getSearchedShops();
        }else {
            setSearchProductList(charSequence.toString());
            getSearchedProducts();

        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public  void hideKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.

        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


    @Override
    public void onDataReceive(String jsonrRsponse) {
        final Message message = returnMessage(jsonrRsponse);
        if (message.getStatusCode() == MessagingFrameworkConstant.STATUS_CODES.Success) {
            Gson gson = new Gson();
            Type founderProductsType = new TypeToken<ArrayList<Product>>() {
            }.getType();

            ArrayList<Product> products = gson.fromJson(gson.toJson(message.getData().get(0)),
                    founderProductsType);

            CenterRepository.getCenterRepository().setListOfSearchedProducts(products);
        }


        ((ECartHomeActivity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                if (message.getStatusCode() == MessagingFrameworkConstant.STATUS_CODES.Success) {
                    getSearchedProducts();
                }
            }
        });


    }
}
