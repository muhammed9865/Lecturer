package com.example.databasework.data.dao.data_classes.subject_related

import android.os.Parcel
import android.os.Parcelable

data class WeekData(
    var videoName: String = "",
    var videoUrl: String = "",
    var videoType: String = ""
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(videoName)
        parcel.writeString(videoUrl)
        parcel.writeString(videoType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WeekData> {
        override fun createFromParcel(parcel: Parcel): WeekData {
            return WeekData(parcel)
        }

        override fun newArray(size: Int): Array<WeekData?> {
            return arrayOfNulls(size)
        }
    }
}

