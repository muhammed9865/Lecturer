package com.example.databasework.data.dao.data_classes.subject_related

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey


data class Week(
    var weekNumber: Int = -1,
    var weekData: List<WeekData> = ArrayList()
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.createTypedArrayList(WeekData.CREATOR)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(weekNumber)
        parcel.writeTypedList(weekData)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Week> {
        override fun createFromParcel(parcel: Parcel): Week {
            return Week(parcel)
        }

        override fun newArray(size: Int): Array<Week?> {
            return arrayOfNulls(size)
        }
    }
}
