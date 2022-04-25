package com.example.module_main.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_main.R
import com.example.module_main.adapter.MainAdapter
import com.example.module_main.adapter.PagerBannerAdapter
import com.example.module_main.bean.MainSkill
import com.example.module_main.databinding.MainFragmentMainBinding
import com.example.module_main.ui.activity.MapActivity
import com.example.module_main.ui.activity.NameActivity
import com.example.module_main.view.BannerViewPager
import com.example.module_main.view.BaseBannerAdapter
import com.example.module_main.utils.*

class MainFragment : Fragment() {

    companion object {
        val INSTANCE = MainFragment()
    }

    private lateinit var mainBvp: BannerViewPager<Int>

    private val binding: MainFragmentMainBinding by lazy {
        MainFragmentMainBinding.inflate(layoutInflater)
    }

    private fun spContent(): String = requireActivity().get("IS_SELECTED")


    fun setContent(value: String) {
        requireActivity().put("IS_SELECTED", value)
    }

    private fun petName(): String = requireActivity().get("PET_NAME")


    private val mainAdapter: MainAdapter by lazy {
        MainAdapter().apply {
            btnClickedListener = {
                when(it){
                    5 ->{
                        if (spContent() != "true") {
                            startActivity(Intent(requireActivity(), NameActivity::class.java))
                            setContent(true.toString())
                        } else {
                            Intent(requireContext(), MapActivity::class.java).apply {
                                putExtra("PET_NAME", petName())
                            }.run { startActivity(this) }
                        }
                    }
                    else ->{
                        Toast.makeText(requireContext(), "研发中~", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)


    }

    /**
     * 初始化view
     */
    private fun initView(view: View) {
        /**
         * 初始化轮播图
         */
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
                        R.drawable.ic_banner_1,
                        R.drawable.ic_banner_2,
                        R.drawable.ic_banner_3,
                        R.drawable.ic_banner_4
                    )
                )

        }

        val skillList = listOf<MainSkill>(
            MainSkill(
                Color.parseColor("#FE6867"),
                R.drawable.main_ic_main_alarm,
                "闹钟功能",
                "定一个早上六点钟的闹钟"

            ),
            MainSkill(
                Color.parseColor("#FDCF0D"),
                R.drawable.main_ic_main_sunny,
                "早上好",
                "早上好呀为你打开今日日报"
            ),
            MainSkill(
                Color.parseColor("#099BF4"),
                R.drawable.main_ic_main_phone,
                "通话",
                "打开通讯录，我要打电话"
            ),
            MainSkill(
                Color.parseColor("#9FDDEC"),
                R.drawable.main_ic_main_eheromometer,
                "温湿查看",
                "及时调节家中温湿度"
            ),
            MainSkill(
                Color.parseColor("#47D710"),
                R.drawable.main_ic_main_schedule,
                "我的习惯",
                "制定计划，养成习惯"
            ),
            MainSkill(
                Color.parseColor("#B767FE"),
                R.drawable.main_ic_main_target,
                "硬件定位",
                "看看今日的待办事项呢!"
            )

        )

        binding.apply {
            mainRvSkill.apply {
                layoutManager = LinearLayoutManager(context)
                mainAdapter.setData(skillList)
                adapter = mainAdapter
            }
        }
    }


}