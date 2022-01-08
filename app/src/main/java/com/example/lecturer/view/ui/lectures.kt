package com.example.lecturer.view.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databasework.R
import com.example.databasework.data.dao.data_classes.subject_related.Week
import com.example.databasework.data.dao.data_classes.subject_related.WeekData
import com.example.databasework.databinding.FragmentLecturesBinding
import com.example.lecturer.view.adapter.WeeksAdapter
import com.example.lecturer.view.interfaces.LectureInterface
import com.example.lecturer.view.viewmodel.LecturesViewModel
import com.example.lecturer.view.viewmodel.LecturesViewModelFactory
import com.google.android.exoplayer2.*


class Lectures : Fragment(R.layout.fragment_lectures), Player.Listener {
    private lateinit var binding: FragmentLecturesBinding
    private lateinit var viewModelFactory: LecturesViewModelFactory
    private lateinit var viewModel: LecturesViewModel
    private var weeks: List<Week> = ArrayList()
    private lateinit var adapter: WeeksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lectures, container, false)!!
        adapter = WeeksAdapter(requireContext())

        val pref =
            activity?.getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE)
        val year = pref!!.getString(getString(R.string.year), "")
        val subject_name = pref.getString(getString(R.string.subject_name), "")


        setUpViewModel(year!!, subject_name!!)




        return binding.root
    }


    private fun setUpViewModel(year: String, subject_name: String) {
        viewModelFactory = LecturesViewModelFactory(year, subject_name)
        viewModel = ViewModelProvider(this, viewModelFactory)[LecturesViewModel::class.java]
        viewModel.getWeeks().observe(this) {
            if (it != null && it != weeks){
                weeks = it
            }
            setUpRecyclerViews()
            adapter.submitList(weeks)
            Log.d(TAG, "setUpViewModel: $weeks")
        }


    }

    private fun setUpRecyclerViews() {
        binding.rvWeeks.layoutManager = LinearLayoutManager(this.context)
        binding.rvWeeks.adapter = adapter
        adapter.setOnClickListener(object : LectureInterface {
            override fun onClickListener(weekData: WeekData) {
                findNavController().navigate(
                    LecturesDirections.actionLecturesToVideoFragment(
                        weekData.videoName,
                        weekData.videoUrl
                    )
                )
            }
        })


    }

    companion object {
        private const val TAG = "lectures"
    }

}