package com.example.lecturer.view.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.databasework.R
import com.example.databasework.data.dao.data_classes.subject_related.Week
import com.example.databasework.data.dao.data_classes.subject_related.WeekData
import com.example.lecturer.view.interfaces.LectureInterface
import com.example.lecturer.view.interfaces.WeekInterface
import kotlinx.android.synthetic.main.item_week.view.*

class WeeksAdapter(private val context: Context) :
    ListAdapter<Week, WeeksAdapter.ViewHolder>(DiffCallBack()) {
    private var onClickListener: LectureInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_week, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Week) = with(itemView) {
            week_number_tv.text = item.weekNumber.toString()

            val adapter = LecturesWeeksAdapter(item.weekData)
            rv_week_elements.layoutManager = LinearLayoutManager(this@WeeksAdapter.context)
            rv_week_elements.adapter = adapter

            adapter.setOnClickListener(object : WeekInterface {
                override fun onWeekClick(weekName: String, weekUrl: String) {
                    if (this@WeeksAdapter.onClickListener != null) {
                        onClickListener!!.onClickListener(WeekData(weekName, weekUrl))
                    }
                }
            })

        }
    }

    fun setOnClickListener(onClickListener: LectureInterface){
        this.onClickListener = onClickListener
    }

    class DiffCallBack : DiffUtil.ItemCallback<Week>() {
        override fun areItemsTheSame(oldItem: Week, newItem: Week): Boolean {
            return oldItem.weekNumber == newItem.weekNumber
        }

        override fun areContentsTheSame(oldItem: Week, newItem: Week): Boolean {
            return oldItem == newItem
        }


    }
}