package com.chauncy.account.common.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import java.lang.ref.WeakReference

/**
 * Created by aucean.liang on 2019/7/26.
 *
 * 生命周期自感知组件，通过实现该接口获得自感知生命周期能力，从而减少依赖输出
 */
interface FragmentLifecycleAware {
    val lifecycleHandler: FragmentLifecycleHandler
}

abstract class FragmentLifecycleHandler {
    private var lifecycleOwner: WeakReference<Fragment>? = null
    private val lifecycleObserver = object : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        fun onLifecycle(owner: LifecycleOwner, event: Lifecycle.Event) {
            when (event) {
                Lifecycle.Event.ON_CREATE -> onCreate(owner)
                Lifecycle.Event.ON_DESTROY -> onDestroy()
                Lifecycle.Event.ON_RESUME -> onSupportVisible()
                Lifecycle.Event.ON_PAUSE -> onSupportInvisible()
                Lifecycle.Event.ON_START -> onViewCreated(owner)
            }
        }
    }

    protected open fun onSupportVisible() {}
    protected open fun onSupportInvisible() {}
    protected open fun onCreate(owner: LifecycleOwner) {}
    protected open fun onDestroy() {}
    protected open fun onViewCreated(owner: LifecycleOwner) {}

    fun subscribeLifecycle(fragment: Fragment) {
        unsubscribeLifecycle()

        lifecycleOwner = WeakReference(fragment)

        fragment.lifecycle.addObserver(lifecycleObserver)
    }

    fun unsubscribeLifecycle() {
        val lastOwner = lifecycleOwner?.get()
        lastOwner?.lifecycle?.removeObserver(lifecycleObserver)
        lifecycleOwner?.clear()
        lifecycleOwner = null
    }
}
