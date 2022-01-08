package com.example.lecturer.view.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.example.databasework.data.dao.data_classes.Subject

import com.example.lecturer.data.service.FirebaseProfileService
import kotlinx.coroutines.launch

class HomeViewModel(private val year: String, private val subject_name: String): ViewModel() {

    private val fireStore = FirebaseProfileService
    private var _subject = MutableLiveData<Subject>()

    fun getSubjectData():LiveData<Subject> {
        viewModelScope.launch {
            //_subject.value = FirebaseProfileService.testGettingSubject(year, subject_name, context)
            _subject.value = fireStore.getSubject(year, subject_name)

        }

        return _subject
    }


}