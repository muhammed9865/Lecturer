package com.example.lecturer.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class LecturesViewModelFactory(private val year: String, private val subject_name: String):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LecturesViewModel(year, subject_name) as T
    }
}