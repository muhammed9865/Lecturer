package com.example.lecturer.view.interfaces

import com.example.databasework.data.dao.data_classes.subject_related.WeekData

interface LectureInterface {
    fun onClickListener(weekData: WeekData)
}