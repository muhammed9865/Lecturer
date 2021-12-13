package com.example.databasework.data.dao.data_classes.subject_related

import android.os.Parcel
import android.os.Parcelable

data class Question(
    var questionName: String = "",
    var answers: List<String> = ArrayList(),
    var correctAnswerIndex: Int = -1
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(questionName)
        parcel.writeStringList(answers)
        parcel.writeInt(correctAnswerIndex)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}
