package com.chauncy.account.common.recyclerview;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewClickListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private OnItemClickListener itemClickListener;

    public RecyclerViewClickListener(Context context, final RecyclerView recyclerView, final OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View viewChild = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (viewChild != null && itemClickListener != null) {
                    itemClickListener.onItemClick(viewChild, recyclerView.getChildLayoutPosition(viewChild));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        return gestureDetector.onTouchEvent(e);
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
