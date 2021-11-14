package com.example.lib_base.ui.rv

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 *@author ZhiQiang Tu
 *@time 2021/8/11  11:20
 *@signature 我们不明前路，却已在路上
 */
class RvHolder(private val binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    //Binder一定会在binder使用之前进行初始化
    lateinit var binder: RvBinder<ViewBinding>
    fun bind(binder: RvBinder<*>) {
        this.binder = binder as RvBinder<ViewBinding>
        //讲View绑定入Data
        this.binder.binding = binding
        //onBind内容
        binder.onBind(this, this.adapterPosition)
    }
}