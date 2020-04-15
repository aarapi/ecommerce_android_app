/*
 * Copyright (c) 2020. http://annoyingprojects.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Albi Arapi <albiarapi1@gmail.com>, 2020.
 */

package com.ecommerce.retailapp.view.activities;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.ecommerce.retailapp.R;
import com.ecommerce.retailapp.model.entities.Money;
import com.ecommerce.retailapp.view.data.StoryInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;

import static com.ecommerce.retailapp.view.adapters.StoryRecyclerViewAdapter.SELECTED_ITEM_INFO;
import static com.ecommerce.retailapp.view.adapters.StoryRecyclerViewAdapter.STORY_LIST_INFO;

public class StoryActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {

    private static  int PROGRESS_COUNT;


    private StoriesProgressView storiesProgressView;
    private ImageView image;
    private TextView tv_price;
    List<StoryInfo> storyInfoList;
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
        storiesProgressView.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.primary));
        storiesProgressView.setDrawingCacheBackgroundColor(getApplicationContext().getResources().getColor(R.color.green));
        storiesProgressView.setStoryDuration(3000L);
        storiesProgressView.setStoriesListener(this);
        storiesProgressView.startStories(counter);

        image = (ImageView) findViewById(R.id.image);
        tv_price = findViewById(R.id.tv_price);

       setImageResource(counter);

        // bind reverse view
        View reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);

        // bind skip view
        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });
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

        ArrayList<StoryInfo> list = (ArrayList) bundle.getSerializable(STORY_LIST_INFO);
        if (list != null && list.size() > 0) {
            storyInfoList = list;
            PROGRESS_COUNT = storyInfoList.size();

        }
        StoryInfo storyInfo = (StoryInfo) bundle.getSerializable(SELECTED_ITEM_INFO);

        for (int i=0; i< storyInfoList.size(); i++){
            if (storyInfo.ID.equals(storyInfoList.get(i).ID)){
                counter = i;
                break;
            }
        }
    }
    public void setImageResource(int counter){
        tv_price.setText(Money.albaniaCurrency(BigDecimal.valueOf
                (Double.parseDouble(storyInfoList.get(counter).getProductPrice()))).toString());
        Glide.with(getApplicationContext())
                .load(storyInfoList.get(counter).getLink())
                .transition(DrawableTransitionOptions.withCrossFade(400))
                .apply(new RequestOptions().override(Target.SIZE_ORIGINAL))
                .into(image);

    }
}
