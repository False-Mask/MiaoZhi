package com.example.module_main.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.module_main.R
import com.example.module_main.view.BaseBannerAdapter

class PagerBannerAdapter : BaseBannerAdapter<Int, PagerBannerAdapter.ViewHolder>() {

    override fun getLayoutId(viewType: Int) = R.layout.main_item_bvp_small_pager

    override fun onBind(holder: ViewHolder, data: Int, position: Int, pageSize: Int) {
        holder.imageView.setImageResource(data)
    }

    override fun createViewHolder(parent: ViewGroup, itemView: View, viewType: Int): ViewHolder {
        return ViewHolder(itemView)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.iv_banner_pager)
    }

}