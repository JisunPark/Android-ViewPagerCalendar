package com.suminjin.calendar;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Display Util
 * @author LDS
 */

public class DisplayUtils {

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
