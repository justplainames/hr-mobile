package edu.singaporetech.hr

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.*
import com.google.firebase.firestore.FieldValue.serverTimestamp
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class AttendanceClockViewModel : ViewModel(){
    private var _clockRecords: MutableLiveData<ArrayList<AttendanceItem>> = MutableLiveData<ArrayList<AttendanceItem>>()
    private lateinit var firestore: FirebaseFirestore

    init{
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToClockStatus()
    }

    private fun listenToClockStatus() {
        firestore.collection("clockStatus")
            .orderBy("UserID", Query.Direction.DESCENDING)
            .addSnapshotListener {
                    snapshot, error ->
                if(error != null){
                    Log.e("firestore Error", error.message.toString())
                    return@addSnapshotListener
                }
                if(snapshot!=null){
                    val clockRecords = ArrayList<AttendanceItem>()
                    val documents = snapshot.documents
                    documents.forEach{
                        val clockRecord = it.toObject(AttendanceItem::class.java)
                        if (clockRecord != null){
                            clockRecord.id = it.id

                            clockRecords.add(clockRecord!!)
                        }
                    }
                    _clockRecords.value = clockRecords
                }
            }
    }

    fun save(clockstatus: AttendanceItem){
        val document = firestore.collection("Attendance").document()
        clockstatus.id = document.id
        val set = document.set(clockstatus)
        set.addOnSuccessListener {
            Log.d("firebase", "attendance saved")
        }
        set.addOnFailureListener {
            Log.d("firebase", "save failed")
        }
    }
}