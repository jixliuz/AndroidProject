package com.chauncy.account.common.inflate

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pools
import androidx.core.view.LayoutInflaterCompat
import java.lang.NullPointerException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.Future


class AsyncLayoutInflaterPlus constructor(val context: Context) {

    private var mHandler: Handler? = null
    private var mInflater: LayoutInflater? = null
    private var mInflaterRunnable: InflaterRunnable? = null
    private var future: Future<Any>? = null

    private var mHandlerCallBack: Handler.Callback? = null

    init {
        mInflater = BasicInflater(context)
        mHandlerCallBack = Handler.Callback {
            val request = it.obj as InflateRequest
            if (request.view == null) {
               /* request.view = mInflater.inflate(
                        request.resId, request.parent, false)*/
            }
            request.callback?.onInflateFinished(
                    request.view, request.resId, request.parent)
            request.countDownLatch?.countDown()
            releaseRequest(request)
            return@Callback true
        }
        mHandler = Handler(mHandlerCallBack)
    }

    @UiThread
    fun inflate(@LayoutRes resId: Int, @Nullable parent: ViewGroup, @NonNull countDownLatch: CountDownLatch,
                @NonNull callback: OnInflateFinishedListener){

        if (callback==null)
            throw NullPointerException("callback argument may not be null!")
        val request=obtainRequest()
    }

    private fun obtainRequest(): InflateRequest? {

        return null
    }

    private fun releaseRequest(request: InflateRequest) {

    }



    class OnInflateFinishedListener {
        fun onInflateFinished(view: View?, resId: Int, parent: ViewGroup?) {

        }

    }


    class BasicInflater(context: Context) : LayoutInflater(context) {
        companion object {
            private val sClassPrefixList = arrayOf("android.widget.",
                    "android.webkit.",
                    "android.app.")
        }

        init {
            if (context is AppCompatActivity) {
                val appCompatDelegate = context.delegate
                if (appCompatDelegate is LayoutInflater.Factory2) {
                    LayoutInflaterCompat.setFactory2(this, appCompatDelegate)
                }
            }
        }

        override fun cloneInContext(newContext: Context?): LayoutInflater? {
            return newContext?.let { BasicInflater(it) }
        }

        override fun onCreateView(name: String?, attrs: AttributeSet?): View {
            try {
                for (prefix in sClassPrefixList) {
                    val view = createView(name, prefix, attrs)
                    if (view != null) return view
                }
            } catch (e: ClassNotFoundException) {
            }

            return super.onCreateView(name, attrs)
        }

    }

    companion object {
        private const val Tag = "AsyncLayoutInflaterPlus"

        private val sExecutor = Executors.newFixedThreadPool(2.coerceAtLeast(Runtime.getRuntime().availableProcessors() - 2))

        private val sRequestPool = Pools.SynchronizedPool<InflateRequest>(10)
    }


    class InflateRequest constructor() {
         var inflater: AsyncLayoutInflaterPlus? = null
         var parent: ViewGroup? = null
         var resId: Int = 0
         var view: View? = null

        var callback: OnInflateFinishedListener? = null
        var countDownLatch: CountDownLatch? = null
    }
}

class InflaterRunnable {

}
