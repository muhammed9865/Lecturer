package com.example.lecturer.view.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.RecyclerView
import com.example.databasework.R
import com.example.databasework.data.dao.data_classes.subject_related.WeekData
import com.example.lecturer.view.interfaces.WeekInterface
import com.example.lecturer.view.ui.VideoFragmentArgs
import com.example.lecturer.view.viewmodel.LecturesViewModel
import kotlinx.android.synthetic.main.item_week_element.view.*

class LecturesWeeksAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: LecturesViewModel
) :
    RecyclerView.Adapter<LecturesWeeksAdapter.ViewHolder>() {
    private var onClickListener: WeekInterface? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LecturesWeeksAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_week_element, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: LecturesWeeksAdapter.ViewHolder, position: Int) {
        var list: List<WeekData> = ArrayList()
            viewModel.getWeekDetails(position).observe(lifecycleOwner){
            list = it
        }
        val item = list[position]
        holder.itemView.lecture_name.text = item.videoName
        holder.itemView.lecture_startBtn.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onWeekClick(
                    item.videoName,
                    item.videoUrl
                )
            }
        }
    }

    override fun getItemCount(): Int {
       /* var size = 0*/
       /* viewModel.getWeekDetails().observe(lifecycleOwner){
            size = it.size
        }*/
        return 0
    }

    fun setOnClickListener(onClickListener: WeekInterface){
        this.onClickListener = onClickListener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)


}