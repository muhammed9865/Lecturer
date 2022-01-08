package com.example.lecturer.data.service

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.databasework.data.dao.data_classes.Subject
import com.example.databasework.data.dao.data_classes.subject_related.Quiz
import com.example.databasework.data.dao.data_classes.subject_related.Week

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception


object FirebaseProfileService {
    private const val TAG = "FirebaseProfileService"


    suspend fun getSubject(year: String, subject_name: String): Subject? {

        val db = Firebase.firestore

        return try {


            val document = db.collection(year)
                .document(subject_name)

            val obj = document.get()
            obj.addOnSuccessListener {
                Log.d(TAG, it.toString())
            }

           obj.await().toObject(Subject::class.java)

        } catch (e: Exception) {
            Log.e(TAG, "Error getting subject", e)
            null
        }
    }





    suspend fun getSubjectWeeks(year: String, subject_name: String): List<Week>?{
        val db = Firebase.firestore

        return try {

            val document = db.collection(year)
                .document(subject_name)

            val obj = document.get()
            obj.addOnSuccessListener {
                Log.d(TAG, it.toString())
            }

            obj.await().toObject(Subject::class.java)!!.weeks

        } catch (e: Exception) {
            Log.e(TAG, "Error getting subject", e)
            ArrayList()
        }
    }


    // Used for testing only and quick adding //
    private fun setSubject(year: String, subject_name: String) {
        val db = Firebase.firestore
        val subject = Subject(
            "This is a description test",
            subject_name,
            listOf(Week(1), Week(2), Week()),
            listOf(Quiz("This is a quiz"), Quiz("Another Quiz"))
        )

        db.collection(year)
            .document(subject_name)
            .set(subject)
            .addOnSuccessListener {
                Log.d(TAG, "SUCCEED YO")
            }
            .addOnFailureListener {
                Log.d(TAG, "FAILED SHEESH")
            }

    }
}