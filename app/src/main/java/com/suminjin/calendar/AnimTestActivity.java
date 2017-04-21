package com.suminjin.calendar;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;

/**
 * Created by parkjisun on 2017. 4. 5..
 */

public class AnimTestActivity extends FragmentActivity {
    //    private ImageView animated;
    private FrameLayout animated;
    private View childView;
    private int statusBarHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_test);

        statusBarHeight = getStatusBarHeight();

        GridLayout layoutGrid = (GridLayout) findViewById(R.id.layoutGrid);
        animated = (FrameLayout) findViewById(R.id.layoutAnimated);
        animated.setVisibility(View.GONE);
        animated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });

        layoutGrid.setColumnCount(2);
        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView(this);
            if (i == 0 || i == 3) {
                setImage(imageView, getResources().getDrawable(R.drawable.sample1));
            } else {
                setImage(imageView, getResources().getDrawable(R.drawable.sample5));
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    childView = v;
                    show(v, null);
                }
            });
            layoutGrid.addView(imageView);
        }

    }

    private void show(View view, final Animator.AnimatorListener listener) {
        animated.setVisibility(View.VISIBLE);

        ImageView imageView = (ImageView) view;
        ((ImageView) animated.findViewById(R.id.imageView)).setImageDrawable(imageView.getDrawable());
//        animated.setBackgroundDrawable(imageView.getDrawable());
        animated.setX(view.getX());
        animated.setY(view.getY());
        animated.setScaleX(0.5f);
        animated.setScaleY(0.5f);
        animated.setPivotX(0);
        animated.setPivotY(0);

        PropertyValuesHolder toScaleX = PropertyValuesHolder.ofFloat("scaleX", 1);
        PropertyValuesHolder toScaleY = PropertyValuesHolder.ofFloat("scaleY", 1);
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x", 0);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", 0);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(animated, toScaleX, toScaleY, pvhX, pvhY);
        animator.setDuration(300);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) {
                    listener.onAnimationEnd(animation);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void hide() {
        animated.setVisibility(View.VISIBLE);

        animated.setX(0);
        animated.setY(0);
        animated.setScaleX(1);
        animated.setScaleY(1);
        animated.setPivotX(0);
        animated.setPivotY(0);

        PropertyValuesHolder toScaleX = PropertyValuesHolder.ofFloat("scaleX", 0.5f);
        PropertyValuesHolder toScaleY = PropertyValuesHolder.ofFloat("scaleY", 0.5f);
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x", childView.getX());
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", childView.getY());
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(animated, toScaleX, toScaleY, pvhX, pvhY);
        animator.setDuration(300);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animated.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void setImage(ImageView imageView, Drawable src) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        imageView.setImageDrawable(src);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        p.width = displayMetrics.widthPixels / 2;
        p.height = (displayMetrics.heightPixels - statusBarHeight) / 2;
        imageView.setLayoutParams(p);
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
