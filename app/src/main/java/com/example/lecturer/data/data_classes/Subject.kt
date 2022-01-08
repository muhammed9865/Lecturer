package com.example.databasework.data.dao.data_classes

import android.os.Parcel
import android.os.Parcelable
import com.example.databasework.data.dao.data_classes.subject_related.Quiz
import com.example.databasework.data.dao.data_classes.subject_related.Week


data class Subject(

    var description: String = "",

    var subjectName: String = "",

    var weeks: List<Week> = ArrayList(),

    val quizzes: List<Quiz> = ArrayList()
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(Week)!!,
        parcel.createTypedArrayList(Quiz)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeString(subjectName)
        parcel.writeTypedList(weeks)
        parcel.writeTypedList(quizzes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Subject> {
        override fun createFromParcel(parcel: Parcel): Subject {
            return Subject(parcel)
        }

        override fun newArray(size: Int): Array<Subject?> {
            return arrayOfNulls(size)
        }
    }
}
