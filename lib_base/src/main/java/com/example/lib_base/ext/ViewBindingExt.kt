package com.example.lib_base.ext

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.lib_base.app.BaseApp

/**
 *@author ZhiQiang Tu
 *@time 2021/10/25  21:34
 *@signature 我们不明前路，却已在路上
 */

inline fun <reified VB : ViewBinding> inflateViewBinding(
    layoutInflater: LayoutInflater = LayoutInflater.from(BaseApp.app),
    parent: ViewGroup? = null,
    attachToParent: Boolean = false,
): ViewBindingLazy<VB> {

    return ViewBindingLazy(VB::class.java, layoutInflater, parent, attachToParent)
}

fun <VB : ViewBinding> inflateViewBinding(
    clz: Class<*>,
    layoutInflater: LayoutInflater,
    parent: ViewGroup? = null,
    attachToParent: Boolean = false,
): VB {
    val method = clz.getDeclaredMethod("inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java)
    return method.invoke(layoutInflater, parent, attachToParent) as VB
}


class ViewBindingLazy<VB : ViewBinding>(
    private val clz: Class<VB>,
    private val layoutInflater: LayoutInflater,
    private val parent: ViewGroup?,
    private val attachToParent: Boolean,
) : Lazy<VB> {
    private val cache: VB? = null
    override fun isInitialized(): Boolean = cache != null

    override val value: VB
        get() {
            return if (cache == null) {
                val kClass = clz
                val method = kClass.getDeclaredMethod("inflate",
                    LayoutInflater::class.java,
                    ViewGroup::class.java,
                    Boolean::class.java)
                method.invoke(null, layoutInflater, parent, attachToParent) as VB
            } else {
                cache
            }
        }

}