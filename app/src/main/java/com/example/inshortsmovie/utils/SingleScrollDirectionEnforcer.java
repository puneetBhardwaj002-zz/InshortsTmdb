package com.example.inshortsmovie.utils;

import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static java.lang.Math.abs;

public class SingleScrollDirectionEnforcer extends RecyclerView.OnScrollListener implements RecyclerView.OnItemTouchListener {

    private int scrollState = RecyclerView.SCROLL_STATE_IDLE;
    private int scrollPointerId = -1;
    private int initialTouchX = 0;
    private int initialTouchY = 0;
    private int dx = 0;
    private int dy = 0;

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
       switch (e.getActionMasked()){
           case MotionEvent.ACTION_DOWN:
               scrollPointerId = e.getPointerId(0);
               initialTouchX = (int)(e.getX() + 0.5f);
               initialTouchY = (int)(e.getY() + 0.5f);
               break;
           case MotionEvent.ACTION_POINTER_DOWN:
               int actionIndex = e.getActionIndex();
               scrollPointerId = e.getPointerId(actionIndex);
               initialTouchX = (int) (e.getX(actionIndex) + 0.5f);
               initialTouchY = (int) (e.getY(actionIndex) + 0.5f);
               break;
           case MotionEvent.ACTION_MOVE:
               int index = e.findPointerIndex(scrollPointerId);
               if (index >= 0 && scrollState != RecyclerView.SCROLL_STATE_DRAGGING) {
                   int x = (int) (e.getX(index) + 0.5f);
                   int y = (int) (e.getY(index) + 0.5f);
                   dx = x - initialTouchX;
                   dy = y - initialTouchY;
               }
               break;
       }
       return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        int oldState = scrollState;
        scrollState = newState;
        if (oldState == RecyclerView.SCROLL_STATE_IDLE && newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            if(recyclerView.getLayoutManager()!=null){
                boolean canScrollHorizontally = recyclerView.getLayoutManager().canScrollHorizontally();
                boolean canScrollVertically = recyclerView.getLayoutManager().canScrollVertically();
                if (canScrollHorizontally != canScrollVertically) {
                    if ((canScrollHorizontally && abs(dy) > abs(dx))
                            || (canScrollVertically && abs(dx) > abs(dy))) {
                        recyclerView.stopScroll();
                    }
                }
            }
        }
    }
}