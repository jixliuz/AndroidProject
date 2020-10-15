package com.chauncy.account.common.inflate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.util.Pools;
import androidx.core.view.LayoutInflaterCompat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsyncLayoutInflaterPlus2 {
    private static final String TAG = "AsyncLayoutInflaterPlus2";
    private Handler mHandler;
    private LayoutInflater mInflater;
    private InflateRunnable mInflateRunnable;
    // 真正执行加载任务的线程池
    private static ExecutorService sExecutor = Executors.newFixedThreadPool(Math.max(2,
            Runtime.getRuntime().availableProcessors() - 2));
    // InflateRequest pool
    private static Pools.SynchronizedPool<AsyncLayoutInflaterPlus2.InflateRequest> sRequestPool = new Pools.SynchronizedPool<>(10);
    private Future<?> future;

    public AsyncLayoutInflaterPlus2(@NonNull Context context) {
        mInflater = new AsyncLayoutInflaterPlus2.BasicInflater(context);
        mHandler = new Handler(mHandlerCallback);
    }

    @UiThread
    public void inflate(@LayoutRes int resId, @Nullable ViewGroup parent, @NonNull CountDownLatch countDownLatch,
                        @NonNull AsyncLayoutInflaterPlus2.OnInflateFinishedListener callback) {
        if (callback == null) {
            throw new NullPointerException("callback argument may not be null!");
        }
        AsyncLayoutInflaterPlus2.InflateRequest request = obtainRequest();
        request.inflater = this;
        request.resId = resId;
        request.parent = parent;
        request.callback = callback;
        request.countDownLatch = countDownLatch;
        mInflateRunnable = new InflateRunnable(request);
        future = sExecutor.submit(mInflateRunnable);
    }

    public void cancel() {
        future.cancel(true);
    }

    /**
     * 判断这个任务是否已经开始执行
     *
     * @return
     */
    public boolean isRunning() {
        return mInflateRunnable.isRunning();
    }

    private Handler.Callback mHandlerCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            AsyncLayoutInflaterPlus2.InflateRequest request = (AsyncLayoutInflaterPlus2.InflateRequest) msg.obj;
            if (request.view == null) {
                request.view = mInflater.inflate(
                        request.resId, request.parent, false);
            }
            request.callback.onInflateFinished(
                    request.view, request.resId, request.parent);
            request.countDownLatch.countDown();
            releaseRequest(request);
            return true;
        }
    };

    public interface OnInflateFinishedListener {
        void onInflateFinished(View view, int resid, ViewGroup parent);
    }

    private class InflateRunnable implements Runnable {
        private InflateRequest request;
        private boolean isRunning;

        public InflateRunnable(InflateRequest request) {
            this.request = request;
        }

        @SuppressLint("LongLogTag")
        @Override
        public void run() {
            isRunning = true;
            try {
                request.view = request.inflater.mInflater.inflate(
                        request.resId, request.parent, false);
            } catch (RuntimeException ex) {
                // Probably a Looper failure, retry on the UI thread
                Log.w(TAG, "Failed to inflate resource in the background! Retrying on the UI"
                        + " thread", ex);
            }
            Message.obtain(request.inflater.mHandler, 0, request)
                    .sendToTarget();
        }

        public boolean isRunning() {
            return isRunning;
        }
    }

    private static class InflateRequest {
        AsyncLayoutInflaterPlus2 inflater;
        ViewGroup parent;
        int resId;
        View view;
        AsyncLayoutInflaterPlus2.OnInflateFinishedListener callback;
        CountDownLatch countDownLatch;

        InflateRequest() {
        }
    }

    private static class BasicInflater extends LayoutInflater {
        private static final String[] sClassPrefixList = {
                "android.widget.",
                "android.webkit.",
                "android.app."
        };

        BasicInflater(Context context) {
            super(context);
            if (context instanceof AppCompatActivity) {
                // 加上这些可以保证AppCompatActivity的情况下，super.onCreate之前
                // 使用AsyncLayoutInflater加载的布局也拥有默认的效果
                AppCompatDelegate appCompatDelegate = ((AppCompatActivity) context).getDelegate();
                if (appCompatDelegate instanceof LayoutInflater.Factory2) {
                    LayoutInflaterCompat.setFactory2(this, (LayoutInflater.Factory2) appCompatDelegate);
                }
            }
        }

        @Override
        public LayoutInflater cloneInContext(Context newContext) {
            return new AsyncLayoutInflaterPlus.BasicInflater(newContext);
        }

        @Override
        protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
            for (String prefix : sClassPrefixList) {
                try {
                    View view = createView(name, prefix, attrs);
                    if (view != null) {
                        return view;
                    }
                } catch (ClassNotFoundException e) {
                    // In this case we want to let the base class take a crack
                    // at it.
                }
            }

            return super.onCreateView(name, attrs);
        }
    }

    public AsyncLayoutInflaterPlus2.InflateRequest obtainRequest() {
        AsyncLayoutInflaterPlus2.InflateRequest obj = sRequestPool.acquire();
        if (obj == null) {
            obj = new AsyncLayoutInflaterPlus2.InflateRequest();
        }
        return obj;
    }

    public void releaseRequest(AsyncLayoutInflaterPlus2.InflateRequest obj) {
        obj.callback = null;
        obj.inflater = null;
        obj.parent = null;
        obj.resId = 0;
        obj.view = null;
        sRequestPool.release(obj);
    }

}
