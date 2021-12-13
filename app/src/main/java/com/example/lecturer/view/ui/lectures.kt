package com.example.lecturer.view.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databasework.R
import com.example.databasework.data.dao.data_classes.subject_related.Week
import com.example.databasework.data.dao.data_classes.subject_related.WeekData
import com.example.databasework.databinding.FragmentLecturesBinding
import com.example.lecturer.view.adapter.LecturesAdapter
import com.example.lecturer.view.interfaces.LectureInterface
import com.example.lecturer.view.viewmodel.LecturesViewModel
import com.example.lecturer.view.viewmodel.LecturesViewModelFactory
import com.google.android.exoplayer2.*



class Lectures : Fragment(R.layout.fragment_lectures), Player.Listener {
    private lateinit var binding: FragmentLecturesBinding
    private lateinit var viewModelFactory: LecturesViewModelFactory
    private lateinit var viewModel: LecturesViewModel
    private var weeks: LiveData<List<Week>> = MutableLiveData()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lectures, container, false)!!

        val pref =
            activity?.getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE)
        val year = pref!!.getString(getString(R.string.year), "")
        val subject_name = pref.getString(getString(R.string.subject_name), "")


        setUpViewModel(year!!, subject_name!!)
        setUpRecyclerViews()



        return binding.root.rootView
    }


    private fun setUpViewModel(year: String, subject_name: String) {
        viewModelFactory = LecturesViewModelFactory(year, subject_name)
        viewModel = ViewModelProvider(this, viewModelFactory)[LecturesViewModel::class.java]
        weeks = viewModel.getWeeks()

        setUpRecyclerViews()

    }

    private fun setUpRecyclerViews() {
        val weeksAdapter = LecturesAdapter(weeks, this, viewModel)
        binding.rvWeeks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWeeks.adapter = weeksAdapter

        weeksAdapter.setOnClickListener(object : LectureInterface {
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

}