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
                        Toast.makeText(requireContext(), "?????????~", Toast.LENGTH_SHORT).show()
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
     * ?????????view
     */
    private fun initView(view: View) {
        /**
         * ??????????????????
         */
        mainBvp = view.findViewById(R.id.main_bvp)
        val bannerViewAdapter = PagerBannerAdapter()
        mainBvp.apply {
            //?????????????????? ???Activity?????????????????????????????????
            setLifecycleRegistry(lifecycle)
            //????????????
            setAutoPlay(true)
            //????????????
            setCanLoop(true)
            //????????????????????????
            setInterval(2)
            //???????????????
            setCanShowIndicator(true)
            //???????????????
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
                "????????????",
                "?????????????????????????????????"

            ),
            MainSkill(
                Color.parseColor("#FDCF0D"),
                R.drawable.main_ic_main_sunny,
                "?????????",
                "????????????????????????????????????"
            ),
            MainSkill(
                Color.parseColor("#099BF4"),
                R.drawable.main_ic_main_phone,
                "??????",
                "?????????????????????????????????"
            ),
            MainSkill(
                Color.parseColor("#9FDDEC"),
                R.drawable.main_ic_main_eheromometer,
                "????????????",
                "???????????????????????????"
            ),
            MainSkill(
                Color.parseColor("#47D710"),
                R.drawable.main_ic_main_schedule,
                "????????????",
                "???????????????????????????"
            ),
            MainSkill(
                Color.parseColor("#B767FE"),
                R.drawable.main_ic_main_target,
                "????????????",
                "??????????????????????????????!"
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