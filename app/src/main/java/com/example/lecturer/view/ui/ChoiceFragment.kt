package com.example.lecturer.view.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.databasework.R
import com.example.databasework.databinding.FragmentChoiceBinding



class ChoiceFragment : Fragment(R.layout.fragment_choice), AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentChoiceBinding
    private var subjectsArray: Int = -1
    private lateinit var year: String
    private lateinit var subjectName: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choice, container, false)


        setYearsSpinner()
        // Inflate the layout for this fragment
        return binding.root.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            findNavController().navigate(
                ChoiceFragmentDirections.actionChoiceFragmentToHomeFragment(
                    year,
                    subjectName
                )
            )

        }
    }

    private fun setYearsSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.years,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerYear.adapter = adapter
            binding.spinnerYear.onItemSelectedListener = this
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        if (parent == binding.spinnerYear) {

            when (parent.selectedItemPosition) {
                0 -> {
                    subjectsArray = R.array.first_year_subjects
                    year = "year_first"
                }
                1 -> {
                    subjectsArray = R.array.second_year_subjects
                    year = "year_second"
                }

                2 -> {
                    subjectsArray = R.array.third_year_cs_subjects
                    year = "year_third"
                }
                3 -> {
                    subjectsArray = R.array.third_year_cs_subjects
                    year = "year_fourth"
                }
            }

            setSubjectsSpinner()
        } else if (parent == binding.spinnerSubject) {
            subjectName = parent.selectedItem.toString()

            binding.button.isEnabled = true
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


    private fun setSubjectsSpinner() {
        Log.d("array", subjectsArray.toString())
        binding.spinnerSubject.isFocusable = true
        binding.spinnerSubject.isClickable = true
        ArrayAdapter.createFromResource(
            requireContext(),
            subjectsArray,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerSubject.adapter = it
            binding.spinnerSubject.onItemSelectedListener = this
        }
    }


}