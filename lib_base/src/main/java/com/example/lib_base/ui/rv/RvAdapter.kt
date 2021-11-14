package com.example.lib_base.ui.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.example.lib_base.app.BaseApp

/**
 *@author ZhiQiang Tu
 *@time 2021/8/11  11:19
 *@signature 我们不明前路，却已在路上
 */

abstract class RvAdapter() : ListAdapter<RvBinder<*>, RvHolder>(diff) {

    companion object {
        private val diff = object : DiffUtil.ItemCallback<RvBinder<*>>() {
            override fun areItemsTheSame(oldItem: RvBinder<*>, newItem: RvBinder<*>): Boolean =
                oldItem.itemId == newItem.itemId

            override fun areContentsTheSame(oldItem: RvBinder<*>, newItem: RvBinder<*>): Boolean =
                oldItem.areContentsTheSame(newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvHolder {
        val holder = RvHolder(
            produceViewBinding(parent,viewType)
        )
//        holder.setIsRecyclable(false)
        return holder
    }

    abstract fun produceViewBinding(parent: ViewGroup, viewType: Int): ViewBinding

    override fun onBindViewHolder(holder: RvHolder, position: Int) {
        val binder = currentList[position] as RvBinder
        holder.bind(binder)
    }

    override fun submitList(list: MutableList<RvBinder<*>>?) {
        super.submitList(if (list != null) ArrayList(list) else null)
    }

    override fun getItemViewType(position: Int): Int = currentList[position].layoutId()
}
