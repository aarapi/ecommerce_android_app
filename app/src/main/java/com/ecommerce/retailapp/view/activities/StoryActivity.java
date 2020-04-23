/*
 * Copyright (c) 2020. http://annoyingprojects.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Albi Arapi <albiarapi1@gmail.com>, 2020.
 */

package com.ecommerce.retailapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.model.entities.Money;
import com.ecommerce.retailapp.model.entities.Product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;

import static com.ecommerce.retailapp.view.adapters.StoryRecyclerViewAdapter.SELECTED_ITEM_INFO;
import static com.ecommerce.retailapp.view.adapters.StoryRecyclerViewAdapter.STORY_LIST_INFO;

public class StoryActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener, View.OnClickListener {

    private static  int PROGRESS_COUNT;


    private StoriesProgressView storiesProgressView;
    private ImageView image;

    private TextView tv_price;
    private TextView tv_product_name;
    private TextView tv_shop_name;

    private RelativeLayout rl_order_button;

    private View reverse;
    private View skip;

    List<Product> productList;
    private int counter = 0;

    long pressTime = 0L;
    long limit = 500L;

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    return limit < now - pressTime;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_stories_layout);


        setPositionClicked();

        storiesProgressView = (StoriesProgressView) findViewById(R.id.stories);
        storiesProgressView.setStoriesCount(PROGRESS_COUNT);
        storiesProgressView.setStoryDuration(3000L);
        storiesProgressView.setStoriesListener(this);
        storiesProgressView.startStories(counter);

        image = (ImageView) findViewById(R.id.image);

        tv_price = findViewById(R.id.tv_price);
        tv_product_name = findViewById(R.id.tv_product_name);
        tv_shop_name = findViewById(R.id.shop_name);

        rl_order_button = findViewById(R.id.order_button);
        rl_order_button.setOnClickListener(this);


        setImageResource(counter);

        // bind reverse view
        reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(this);


        reverse.setOnTouchListener(onTouchListener);

        // bind skip view
        skip = findViewById(R.id.skip);
        skip.setOnClickListener(this);
        skip.setOnTouchListener(onTouchListener);
    }

    @Override
    public void onNext() {
        setImageResource(++counter);
    }

    @Override
    public void onPrev() {
        if ((counter - 1) < 0) return;
        setImageResource(--counter);
    }

    @Override
    public void onComplete() {
        finish();
    }

    @Override
    protected void onDestroy() {
        // Very important !
        storiesProgressView.destroy();
        super.onDestroy();
    }
    public void setPositionClicked(){
        Bundle bundle = getIntent().getBundleExtra("data");

        ArrayList<Product> list = (ArrayList) bundle.getSerializable(STORY_LIST_INFO);
        if (list != null && list.size() > 0) {
            productList = list;
            PROGRESS_COUNT = productList.size();

        }
        Product product = (Product) bundle.getSerializable(SELECTED_ITEM_INFO);

        for (int i = 0; i < productList.size(); i++) {
            if (product.getProductId().equals(productList.get(i).getProductId())) {
                counter = i;
                break;
            }
        }
    }

    public void setImageResource(int counter){
        tv_price.setText(Money.albaniaCurrency(BigDecimal.valueOf
                (Double.parseDouble(productList.get(counter).getSellMRP()))).toString());
        tv_product_name.setText(productList.get(counter).getItemName());
        tv_shop_name.setText(productList.get(counter).getShopName());
        Glide.with(getApplicationContext())
                .load(productList.get(counter).getImageURL())
                .transition(DrawableTransitionOptions.withCrossFade(400))
                .apply(new RequestOptions().override(Target.SIZE_ORIGINAL))
                .into(image);

    }

    @Override
    public void onClick(View view) {
        if (view == reverse) {
            storiesProgressView.reverse();
        } else if (view == skip) {
            storiesProgressView.skip();
        } else if (view == rl_order_button) {
            Intent intent = new Intent();
            intent.putExtra("PRODUCT_DATA", (Serializable) productList.get(counter));
            setResult(1, intent);
            finish();
        }
    }
}
