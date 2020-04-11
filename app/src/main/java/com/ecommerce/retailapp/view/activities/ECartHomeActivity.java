/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.ecommerce.retailapp.view.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import com.ecommerce.retailapp.domain.mock.CheckSetup;
import com.ecommerce.retailapp.view.fragment.OrderExecuteBootomFragment;
import com.example.connectionframework.requestframework.constants.Constants;
import com.example.connectionframework.requestframework.receiver.ReceiverActivity;
import com.example.connectionframework.requestframework.sender.Repository;
import com.google.android.material.navigation.NavigationView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.domain.helper.Connectivity;
import com.ecommerce.retailapp.domain.mining.AprioriFrequentItemsetGenerator;
import com.ecommerce.retailapp.domain.mining.FrequentItemsetData;
import com.ecommerce.retailapp.model.CenterRepository;
import com.ecommerce.retailapp.model.entities.Money;
import com.ecommerce.retailapp.model.entities.Product;
import com.ecommerce.retailapp.utils.PreferenceHelper;
import com.ecommerce.retailapp.utils.TinyDB;
import com.ecommerce.retailapp.utils.Utils;
import com.ecommerce.retailapp.utils.Utils.AnimationType;
import com.ecommerce.retailapp.view.fragment.HomeFragment;
import com.ecommerce.retailapp.view.fragment.WhatsNewDialog;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ECartHomeActivity extends AppCompatActivity implements ReceiverActivity, OnClickListener {

    public static final double MINIMUM_SUPPORT = 0.1;
    private static final String TAG = ECartHomeActivity.class.getSimpleName();
    AprioriFrequentItemsetGenerator<String> generator =
            new AprioriFrequentItemsetGenerator<>();
    private int itemCount = 0;
    private BigDecimal checkoutAmount = new BigDecimal(BigInteger.ZERO);
    private DrawerLayout mDrawerLayout;
    private FrameLayout frag_container;
    private RelativeLayout rl_error_server;
    private TextView tv_error_message;
    private LinearLayout ll_retry;
    private  OrderExecuteBootomFragment orderExecuteBootomFragment;

    private TextView checkOutAmount, itemCountTextView, checkout;
    private TextView offerBanner;
    private ProgressBar progressBar;

    private NavigationView mNavigationView;
    private View checkout_button;
    private View order_button;
    private HomeFragment homeFragment;
    private FrameLayout item_counter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecart);

        frag_container = findViewById(R.id.frag_container);
        rl_error_server = findViewById(R.id.rl_error_server);
        tv_error_message = findViewById(R.id.tv_error_message);
        ll_retry = findViewById(R.id.ll_retry);
        checkout_button = findViewById(R.id.checkout_button);
        order_button = findViewById(R.id.order_button);


        ll_retry.setOnClickListener(this);

        CenterRepository.getCenterRepository().setListOfProductsInShoppingList(
                new TinyDB(getApplicationContext()).getListObject(
                        PreferenceHelper.MY_CART_LIST_LOCAL, Product.class));

        itemCount = CenterRepository.getCenterRepository().getListOfProductsInShoppingList()
                .size();

        //	makeFakeVolleyJsonArrayRequest();

        offerBanner = ((TextView) findViewById(R.id.new_offers_banner));

        itemCountTextView = (TextView) findViewById(R.id.item_count);
        itemCountTextView.setSelected(true);
        itemCountTextView.setText(String.valueOf(itemCount));

        checkOutAmount = (TextView) findViewById(R.id.checkout_amount);
        checkOutAmount.setSelected(true);
        checkOutAmount.setText(Money.albaniaCurrency(checkoutAmount).toString());
        offerBanner.setSelected(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        progressBar = findViewById(R.id.loading_bar);

        checkOutAmount.setOnClickListener(this);


        if (itemCount != 0) {
            long amount = 0;

            for (Product product : CenterRepository.getCenterRepository()
                    .getListOfProductsInShoppingList()) {
                amount += Long.valueOf(product.getSellMRP()) * Integer.valueOf(product.getQuantity());

            }
            updateCheckOutAmount(
                    BigDecimal.valueOf(amount),
                    true);
        }

        item_counter = findViewById(R.id.item_counter);
        item_counter.setOnClickListener(this);

        checkout = findViewById(R.id.checkout);
        checkout.setOnClickListener(this);

        homeFragment = new HomeFragment();

        Utils.switchFragmentWithAnimation(R.id.frag_container,
                homeFragment, this, Utils.HOME_FRAGMENT,
                AnimationType.SLIDE_UP);

        toggleBannerVisibility();

        mNavigationView
                .setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()) {
                            case R.id.home:

                                mDrawerLayout.closeDrawers();

                                Utils.switchContent(R.id.frag_container,
                                        Utils.HOME_FRAGMENT,
                                        ECartHomeActivity.this,
                                        AnimationType.SLIDE_LEFT);

                                return true;

                            case R.id.my_cart:

                                mDrawerLayout.closeDrawers();

                                Utils.switchContent(R.id.frag_container,
                                        Utils.SHOPPING_LIST_TAG,
                                        ECartHomeActivity.this,
                                        AnimationType.SLIDE_LEFT);
                                return true;

//                            case R.id.apriori_result:
//
//                                mDrawerLayout.closeDrawers();
//
//                                startActivity(new Intent(ECartHomeActivity.this, APrioriResultActivity.class));
//
//                                return true;


                            case R.id.contact_us:

                                mDrawerLayout.closeDrawers();

//                                Utils.switchContent(R.id.frag_container,
//                                        Utils.CONTACT_US_FRAGMENT,
//                                        ECartHomeActivity.this,
//                                        AnimationType.SLIDE_LEFT);
                                return true;

                            default:
                                return true;
                        }
                    }
                });

    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateItemCount(boolean ifIncrement) {
        if (ifIncrement) {
            itemCount++;
            itemCountTextView.setText(String.valueOf(itemCount));

        } else {
            itemCountTextView.setText(String.valueOf(itemCount <= 0 ? 0
                    : --itemCount));
        }

        toggleBannerVisibility();
    }

    public void updateCheckOutAmount(BigDecimal amount, boolean increment) {

        if (increment) {
            checkoutAmount = checkoutAmount.add(amount);
        } else {
            if (checkoutAmount.signum() == 1)
                checkoutAmount = checkoutAmount.subtract(amount);
        }

        checkOutAmount.setText(Money.albaniaCurrency(checkoutAmount).toString());
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Store Shopping Cart in DB
        new TinyDB(getApplicationContext()).putListObject(
                PreferenceHelper.MY_CART_LIST_LOCAL, CenterRepository
                        .getCenterRepository().getListOfProductsInShoppingList());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Show Offline Error Message
        if (!Connectivity.isConnected(getApplicationContext())) {
            final Dialog dialog = new Dialog(ECartHomeActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.connection_dialog);
            Button dialogButton = (Button) dialog
                    .findViewById(R.id.dialogButtonOK);

            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });

            dialog.show();
        }

        // Show Whats New Features If Requires
        new WhatsNewDialog(this);
    }

    /*
     * Toggles Between Offer Banner and Checkout Amount. If Cart is Empty SHow
     * Banner else display total amount and item count
     */
    public void toggleBannerVisibility() {
        if (itemCount == 0) {

            findViewById(R.id.checkout_item_root).setVisibility(View.GONE);
            findViewById(R.id.new_offers_banner).setVisibility(View.VISIBLE);

        } else {
            findViewById(R.id.checkout_item_root).setVisibility(View.VISIBLE);
            findViewById(R.id.new_offers_banner).setVisibility(View.GONE);
        }
    }

    /*
     * get total checkout amount
     */
    public BigDecimal getCheckoutAmount() {
        return checkoutAmount;
    }



    /*
     * Get Number of items in cart
     */
    public int getItemCount() {
        return itemCount;
    }

    /*
     * Get Navigation drawer
     */
    public DrawerLayout getmDrawerLayout() {
        return mDrawerLayout;
    }


    public void showPurchaseDialog() {

        AlertDialog.Builder exitScreenDialog = new AlertDialog.Builder(ECartHomeActivity.this, R.style.PauseDialog);

        exitScreenDialog.setTitle("Payment Methods")
                .setMessage("Which payment do you prefer?");
        exitScreenDialog.setCancelable(true);

        exitScreenDialog.setPositiveButton(
                "Cash Payment",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //finish();
                        dialog.cancel();
                        choosePaymentTypeDialog(true);

                    }
                });

        exitScreenDialog.setNegativeButton(
                "Card Payment",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        choosePaymentTypeDialog(false);
                    }
                });

        AlertDialog alert11 = exitScreenDialog.create();
        alert11.show();

        ((Button)alert11.findViewById(android.R.id.button3)).setTextColor(getResources().getColor(R.color.white));
        ((Button)alert11.findViewById(android.R.id.button1)).setTextColor(getResources().getColor(R.color.white));
        ((Button)alert11.findViewById(android.R.id.button2)).setTextColor(getResources().getColor(R.color.white));

    }


    public void choosePaymentTypeDialog(boolean isCashPayment){
        ArrayList<String> productId = new ArrayList<String>();

        for (Product productFromShoppingList : CenterRepository.getCenterRepository().getListOfProductsInShoppingList()) {

            //add product ids to array
            productId.add(productFromShoppingList.getProductId());
        }

        //pass product id array to Apriori ALGO
        CenterRepository.getCenterRepository()
                .addToItemSetList(new HashSet<>(productId));

        //Do Minning
        FrequentItemsetData<String> data = generator.generate(
                CenterRepository.getCenterRepository().getItemSetList()
                , MINIMUM_SUPPORT);

        for (Set<String> itemset : data.getFrequentItemsetList()) {
            Log.e("APriori", "Item Set : " +
                    itemset + "Support : " +
                    data.getSupport(itemset));
        }
        openCheckoutPaymentFragment(isCashPayment);

    }

    public void openCheckoutPaymentFragment(boolean isCashPayment){
        Bundle bundle = new Bundle();
        bundle.putSerializable(OrderExecuteBootomFragment.CHECKOUT_DATA,
                (Serializable) CenterRepository.getCenterRepository().getListOfProductsInShoppingList());
        bundle.putSerializable("TOTAL_AMOUNT", (Serializable) checkOutAmount.getText());

         orderExecuteBootomFragment =
                OrderExecuteBootomFragment.newInstance(bundle, this, isCashPayment);
         Utils.addFragmentWithAnimation(R.id.frag_container,orderExecuteBootomFragment,
                 this, Utils.ORDER_EXECUTE_FRAGMENT,  AnimationType.SLIDE_UP);
    }


    public void clearOrderList(){
        itemCount = 0;
        itemCountTextView.setText(String.valueOf(0));
        checkoutAmount = new BigDecimal(BigInteger.ZERO);
        checkOutAmount.setText(Money.albaniaCurrency(checkoutAmount).toString());
        toggleBannerVisibility();
    }

    public FrameLayout getFrag_container() {
        return frag_container;
    }

    public RelativeLayout getRl_error_server() {
        return rl_error_server;
    }

    public View getCheckout_button() {
        return checkout_button;
    }

    public TextView getTv_error_message() {
        return tv_error_message;
    }


    public View getOrder_button() {
        return order_button;
    }

    @Override
    public void onDataReceive(int action, final List<Object> data) {
        if (action == CheckSetup.ServerActions.ECOMMERCE_MAKE_AN_ORDER) {
            if (Repository.newInstance().getStatusCode() == Constants.Success) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clearOrderList();

                        orderExecuteBootomFragment.getpDialog().setTitleText(((String) data.get(0)))
                                .setConfirmText("OK").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                orderExecuteBootomFragment.getpDialog().dismissWithAnimation();
                                orderExecuteBootomFragment.dismiss();
                            }
                        })
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                        CenterRepository.getCenterRepository().setListOfProductsInShoppingList(new ArrayList<Product>());
                    }
                });
            }else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clearOrderList();

                        orderExecuteBootomFragment.getpDialog().setTitleText(Repository.newInstance().getMessageError())
                                .setConfirmText("OK").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                orderExecuteBootomFragment.getpDialog().dismissWithAnimation();
                                orderExecuteBootomFragment.dismiss();
                            }
                        })
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                });
            }
        }else if (action == CheckSetup.ServerActions.ECOMMERCE_LIST_OF_SEARCHED_PRODUCTS){
            HomeFragment homeFragment = null;
            for (int i =0 ; i<getSupportFragmentManager().getFragments().size(); i++ ){
                try {
                    homeFragment = (HomeFragment) getSupportFragmentManager().getFragments().get(i);
                }catch (Exception e){
                    continue;
                }
            }
        }



    }

    @Override
    public void onClick(View view) {
        if (view == ll_retry){
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frag_container);
            getSupportFragmentManager().beginTransaction()
                    .detach(currentFragment)
                    .attach(currentFragment)
                    .commit();
        }else if (view == checkOutAmount){

            Utils.vibrate(getApplicationContext());

            Utils.switchContent(R.id.frag_container,
                    Utils.SHOPPING_LIST_TAG, ECartHomeActivity.this,
                    AnimationType.SLIDE_UP);

        }else if (view == item_counter){
            Utils.vibrate(getApplicationContext());
            Utils.switchContent(R.id.frag_container,
                    Utils.SHOPPING_LIST_TAG,
                    ECartHomeActivity.this, AnimationType.SLIDE_UP);
        }else if (view == checkout){

            Utils.vibrate(getApplicationContext());
            choosePaymentTypeDialog(false);
        }

        rl_error_server.setVisibility(View.GONE);

    }

    public  Bitmap getBitmapFromVectorDrawable(int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
