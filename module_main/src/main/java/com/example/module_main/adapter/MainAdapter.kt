package com.example.module_main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.module_main.bean.MainSkill
import com.example.module_main.databinding.MainItemFuncBinding

/**
 * @Date : 2021/11/16   19:17
 * @By ysh
 * @Usage :
 * @Request : God bless my code
 **/
class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private lateinit var skillList: List<MainSkill>

    var btnClickedListener: (Int)->Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            MainItemFuncBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.mainBtnTry.setOnClickListener{
            btnClickedListener(viewType)
        }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val skill = skillList[position]
        holder.binding.apply {
            mainSivBg.setBackgroundColor(skill.bg)
            mainIvAlarm.setImageResource(skill.icon)
            mainTvTitle.text = skill.title
            mainTvContent.text = skill.content
        }

    }

    override fun getItemViewType(position: Int): Int = position

    override fun getItemCount(): Int {
        return skillList.size
    }

    inner class ViewHolder(val binding: MainItemFuncBinding) : RecyclerView.ViewHolder(binding.root) {
        val mainIvBg = binding.mainSivBg
        val mainIvIcon = binding.mainIvAlarm
        val mainTvTitle = binding.mainTvTitle
        val mainTvContent = binding.mainTvContent
        val manBtnTrt = binding.mainBtnTry
    }

    fun setData(skillList: List<MainSkill>) {
        this.skillList = skillList
    }


}