package com.chauncy.account.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class PageRecyclerView extends RecyclerView {
    private PageIndicatorView mIndicatorView = null; // 指示器布局
    private PageIndicatorHelper mPageIndicatorHelper;

    public PageRecyclerView(Context context) {
        this(context, null);
    }

    public PageRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPageIndicatorHelper = new PageIndicatorHelper();
    }


    /**
     * 设置指示器
     *
     * @param indicatorView 指示器布局
     */
    public void setIndicator(PageIndicatorView indicatorView) {
        mIndicatorView = indicatorView;
        mPageIndicatorHelper.setIndicatorView(indicatorView);
    }


    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        mPageIndicatorHelper.attachToRecyclerView(this);
        if (mIndicatorView != null)
            mIndicatorView.initIndicator(adapter.getItemCount());
    }

    @Override
    public void scrollToPosition(int position) {
        super.scrollToPosition(position);
        if(mIndicatorView!=null)
            mIndicatorView.setSelectedPage(position);
    }

    private class PageIndicatorHelper extends PagerSnapHelper {
        private RecyclerView mRecyclerView;
        private PageIndicatorView mIndicatorView;

        private void setIndicatorView(PageIndicatorView indicatorView) {
            mIndicatorView = indicatorView;
        }

        @Override
        public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {
            super.attachToRecyclerView(recyclerView);
            mRecyclerView = recyclerView;
        }


        @Override
        public boolean onFling(int velocityX, int velocityY) {
            RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
            if (layoutManager == null) {
                return false;
            }
            RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
            if (adapter == null) {
                return false;
            }
            int minFlingVelocity = mRecyclerView.getMinFlingVelocity();
            return (Math.abs(velocityY) > minFlingVelocity || Math.abs(velocityX) > minFlingVelocity)
                    && snapFromFling(layoutManager, velocityX, velocityY);
        }

        private boolean snapFromFling(@NonNull RecyclerView.LayoutManager layoutManager, int velocityX,
                                      int velocityY) {
            if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
                return false;
            }

            RecyclerView.SmoothScroller smoothScroller = createScroller(layoutManager);
            if (smoothScroller == null) {
                return false;
            }

            int targetPosition = findTargetSnapPosition(layoutManager, velocityX, velocityY);
            if (targetPosition == RecyclerView.NO_POSITION) {
                return false;
            }

            smoothScroller.setTargetPosition(targetPosition);
            layoutManager.startSmoothScroll(smoothScroller);
            if (mIndicatorView != null)
                mIndicatorView.setSelectedPage(targetPosition);
            return true;
        }
    }

}
