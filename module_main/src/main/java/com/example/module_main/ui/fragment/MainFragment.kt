package com.example.module_main.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.module_main.R
import com.example.module_main.adapter.PagerBannerAdapter
import com.example.module_main.databinding.MainActivityMainBinding
import com.example.module_main.databinding.MainFragmentMainBinding
import com.example.module_main.view.BannerViewPager
import com.example.module_main.view.BaseBannerAdapter


class MainFragment : Fragment() {

    private lateinit var mainBvp: BannerViewPager<Int>

    private val binding: MainFragmentMainBinding by lazy {
        MainFragmentMainBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainBvp = view.findViewById(R.id.main_bvp)
        val bannerViewAdapter = PagerBannerAdapter()

        mainBvp.apply {
            //设置生命周期 当Activity可视的时候开启自动轮播
            setLifecycleRegistry(lifecycle)
            //自动轮询
            setAutoPlay(true)
            //循环滚动
            setCanLoop(true)
            //设置轮询时间间隔
            setInterval(2)
            //显示指示器
            setCanShowIndicator(true)
            //设置适配器
            setAdapter(bannerViewAdapter)
            //
            setOnPageClickListener(object : BaseBannerAdapter.OnPageClickListener {
                override fun onPageClick(position: Int, v: View) {
                    println("click ...")
                }
            })
                .create(
                    listOf(
                        R.drawable.mine_ic_center_goods,
                        R.drawable.mine_ic_center_goods,
                        R.drawable.mine_ic_center_goods
                    )
                )

        }
    }

    companion object{
        val INSTANCE = MainFragment()
    }


}