package com.example.lecturer.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.databasework.data.dao.data_classes.subject_related.Week
import com.example.databasework.data.dao.data_classes.subject_related.WeekData
import com.example.lecturer.data.service.FirebaseProfileService
import kotlinx.coroutines.launch

class LecturesViewModel(private val year: String,private val subject_name: String) : ViewModel() {

    private val _weeks = MutableLiveData<List<Week>>()
    private val _weekData = MutableLiveData<List<WeekData>>()

    fun getWeeks():LiveData<List<Week>> {
        val db = FirebaseProfileService

        viewModelScope.launch {
            _weeks.value = db.getSubjectWeeks(year, subject_name)
        }
        return _weeks
    }

    fun getWeekDetails(position: Int):LiveData<List<WeekData>>{
        val db = FirebaseProfileService

        viewModelScope.launch {
            _weekData.value = db.getSubjectWeeks(year, subject_name)!![position].weekData
        }
        return _weekData
    }
}