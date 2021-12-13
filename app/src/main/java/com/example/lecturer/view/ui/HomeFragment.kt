package com.example.lecturer.view.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.databasework.R
import com.example.databasework.data.dao.data_classes.Subject
import com.example.databasework.databinding.FragmentHomeBinding
import com.example.lecturer.view.viewmodel.HomeViewModel
import com.example.lecturer.view.viewmodel.HomeViewModelFactory


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController

    private lateinit var viewModelFactory: HomeViewModelFactory
    private lateinit var viewModel: HomeViewModel
    private lateinit var subject: Subject
    private lateinit var year: String
    private lateinit var subjectName: String
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        if (!requireArguments().isEmpty) {
            val args = HomeFragmentArgs.fromBundle(
                requireArguments()
            )
            setSharedPref(args.year, args.subjectName)
        }

        getSharedPref()

        viewModelFactory = HomeViewModelFactory(year, subjectName)
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]


        viewModel.getSubjectData().observe(this) {
            subject = it
            setUpHomepage()

        }


        navController = this.findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnGotoLectures.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_lectures)
        }
        binding.btnGotoQuiz.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_quizzesFragment)
        }
        binding.btnGotoQuestion.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_questionsFragment)
        }


    }

    private fun setUpHomepage() {
        binding.tvSubjectName.text = subject.subjectName
        binding.tvSubjectDescription.text = subject.description
        binding.lecturesCounterTv.text = subject.weeks.size.toString()
        binding.quizCounterTv.text = subject.quizzes.size.toString()

    }

    private fun setSharedPref(year: String, subject_name: String) {
        sharedPreferences =
            activity?.getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE)
                ?: return
        with(sharedPreferences!!.edit()) {
            putString(getString(R.string.year), year)
            putString(getString(R.string.subject_name), subject_name)
            apply()
        }
    }

    private fun getSharedPref() {
        val getSharedPref =
            activity?.getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE)
        year = getSharedPref!!.getString(getString(R.string.year), "").toString()
        subjectName = getSharedPref.getString(getString(R.string.subject_name), "").toString()

    }

    companion object {
        private const val TAG = "HomeFragment"
    }

}