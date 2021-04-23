package com.example.inshortsmovie.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.TypedValue;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.text.DecimalFormat;

import javax.inject.Inject;

public class AppUtils {
    public static int convertDpToPixel(float dp, Context context){
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
    }

    public static boolean isNullOrEmptyString(String input){
        return input == null || input.isEmpty();
    }

    public static void enforceSingleScrollDirection(RecyclerView recyclerView,SingleScrollDirectionEnforcer enforcer){
        recyclerView.addOnItemTouchListener(enforcer);
        recyclerView.addOnScrollListener(enforcer);
    }

    public static RecyclerView getRecyclerView(ViewPager2 pager2){
        return (RecyclerView) pager2.getChildAt(0);
    }

    public static String parseDouble(double amount){
        try{
            return Double.toString(amount);
        }catch (Exception e){
            return "0.0";
        }
    }

    public static String parseRuntime(int timeInMinutes){
        int hour = timeInMinutes/60;
        int minutes = timeInMinutes%60;
        if(hour == 0){
            if(minutes == 0){
                return "";
            } else if(minutes == 1){
                return minutes + " minute";
            }else{
                return minutes + " minutes";
            }
        }else{
            if(minutes == 0){
                return hour == 1? hour + " hour" : hour + " hours";
            } else if(minutes == 1){
                return hour == 1? hour + " hour " + minutes + " minute" : hour + " hours " + minutes + " minute";
            }else{
                return hour == 1? hour + " hour " + minutes + " minutes" : hour + " hours " + minutes + " minutes";
            }
        }
    }

    public static String parseAmount(long amount){
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount);
    }
}
