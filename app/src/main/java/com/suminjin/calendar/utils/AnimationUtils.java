package com.suminjin.calendar.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;

/**
 * Created by 152317 on 2015-09-09.
 */
public class AnimationUtils {

    public static void move(View view, float fromXDelta, float toXDelta, float fromYDelta, float toYDelta,
                            int duration, Interpolator interpolator, Animation.AnimationListener listener) {
        AnimationSet ret = new AnimationSet(false);
        Animation anim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, fromXDelta,
                Animation.RELATIVE_TO_SELF, toXDelta,
                Animation.RELATIVE_TO_SELF, fromYDelta,
                Animation.RELATIVE_TO_SELF, toYDelta);
        anim.setDuration(duration);
        if(interpolator != null)
            anim.setInterpolator(interpolator);

        ret.addAnimation(anim);
        ret.setAnimationListener(listener);
        view.startAnimation(ret);
    }

    public static void moveWithAlpah(View view, float fromXDelta, float toXDelta, float fromYDelta, float toYDelta,
                                     int duration, float fromAlpha, float toAlpha, Interpolator interpolator, Animation.AnimationListener listener){
        AnimationSet ret = new AnimationSet(false);

        Animation translateAnim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, fromXDelta,
                Animation.RELATIVE_TO_SELF, toXDelta,
                Animation.RELATIVE_TO_SELF, fromYDelta,
                Animation.RELATIVE_TO_SELF, toYDelta);
        translateAnim.setDuration(duration);

        Animation alphaAnim = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnim.setDuration(duration);
        alphaAnim.setFillAfter(true);

        if(interpolator != null)
            translateAnim.setInterpolator(interpolator);

        ret.addAnimation(alphaAnim);
        ret.addAnimation(translateAnim);
        ret.setAnimationListener(listener);
        view.startAnimation(ret);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void revealCenter(View view) {
        int centerX = view.getWidth() / 2;
        int centerY = view.getHeight() / 2;
        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, 0, finalRadius);
        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void revealLeftToRight(View view) {
        int centerX = 0;
        int centerY = view.getHeight() / 2;
        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, 0, finalRadius);
        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void hide(final View view) {
        int centerX = view.getWidth();
        int centerY = view.getHeight() / 2;
        int initialRadius = view.getWidth();

        Animator anim = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }

    /**
     * Display Util
     * @author LDS
     */

    public static class DisplayUtils {

        public static int getDisplayWidth(Context context){
            DisplayMetrics display = context.getResources().getDisplayMetrics();
            return display.widthPixels;
        }

        public static int getDisplayHeight(Context context){
            DisplayMetrics display = context.getResources().getDisplayMetrics();
            return display.heightPixels;
        }

        public static int dpToPx(Context context, int dp){
            return (int) (dp * context.getResources().getDisplayMetrics().density);
        }

        public static int pxToDp(Context context, int px){
            return (int) (px / context.getResources().getDisplayMetrics().density);
        }
    }
}
