package com.example.lecturer.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.databasework.R
import com.example.databasework.data.dao.data_classes.subject_related.Week
import com.example.databasework.data.dao.data_classes.subject_related.WeekData
import com.example.lecturer.view.interfaces.LectureInterface
import com.example.lecturer.view.interfaces.WeekInterface
import com.example.lecturer.view.viewmodel.LecturesViewModel
import kotlinx.android.synthetic.main.item_week.view.*

class LecturesAdapter(
    private val liveDataToObserve: LiveData<List<Week>>,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: LecturesViewModel
) :
    RecyclerView.Adapter<LecturesAdapter.LecturesViewHolder>() {

    private var list: List<Week>? = null

    private var onClickListener: LectureInterface? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LecturesAdapter.LecturesViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_week, parent, false)

        return LecturesViewHolder(view)
    }

    override fun onBindViewHolder(holder: LecturesAdapter.LecturesViewHolder, position: Int) {
        Log.d("LectureFragment", list.toString())
        val item = list!![position]
        val num = "Week ${item.weekNumber}"
        holder.itemView.week_number_tv.text = num
        val adapter = LecturesWeeksAdapter(lifecycleOwner, viewModel)
        holder.itemView.rv_week_elements.adapter = adapter

        adapter.setOnClickListener(object : WeekInterface {
            override fun onWeekClick(weekName: String, weekUrl: String) {
                if (onClickListener != null) {
                    onClickListener!!.onClickListener(weekData = WeekData(weekName, weekUrl))
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return 20
    }

    fun setOnClickListener(onClickListener: LectureInterface) {
        this.onClickListener = onClickListener
    }

    inner class LecturesViewHolder(
        view: View,
    ) : RecyclerView.ViewHolder(view) {
        init {
            liveDataToObserve.observe(lifecycleOwner) {
                list = it
            }
        }
    }
}