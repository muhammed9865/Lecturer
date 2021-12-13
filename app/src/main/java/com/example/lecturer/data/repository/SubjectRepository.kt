package com.example.lecturer.data.repository

import android.content.Context
import com.example.lecturer.data.service.FirebaseProfileService

class SubjectRepository() {
    private val fbSerivce = FirebaseProfileService

    suspend fun getSubject(year: String, subject_name: String) = fbSerivce.getSubject(year, subject_name)

    suspend fun getSubjectWeeks(year: String, subject_name: String) = fbSerivce.getSubjectWeeks(year, subject_name)

    companion object {
        private var instance: SubjectRepository? = null
        fun getInstance(context: Context): SubjectRepository {
            return instance ?: synchronized(this) {
                instance ?: SubjectRepository().also { instance = it }
            }
        }
        private const val TAG = "SubjectRepository"
    }

}
