package com.example.lib_base.ui.rv

import androidx.viewbinding.ViewBinding

/**
 *@author ZhiQiang Tu
 *@time 2021/8/23  9:49
 *@signature 我们不明前路，却已在路上
 */
abstract class RvBinder<VB : ViewBinding> {
    var itemId: String = ""
    lateinit var binding: VB
    open fun onBind(holder: RvHolder, position: Int) {}
    abstract fun layoutId(): Int
    abstract fun areContentsTheSame(oldItem: RvBinder<*>): Boolean
}