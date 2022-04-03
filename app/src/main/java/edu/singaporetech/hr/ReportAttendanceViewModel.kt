package edu.singaporetech.hr

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import java.util.ArrayList

class ReportAttendanceViewModel : ViewModel() {
    private var _attendenceArrayList: MutableLiveData<ArrayList<Attendance>> = MutableLiveData<ArrayList<Attendance>>()
    private var firestore: FirebaseFirestore
    open var attendenceArrayList = ArrayList<Attendance>()
    init{
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToAttendanceRecord()


    }

    fun listenToAttendanceRecord(){

        firestore.collection("Attendance").addSnapshotListener {
                snapshot, error ->
            if(error != null){
                Log.e("firestore Error", error.message.toString())
                return@addSnapshotListener
            }
            if(snapshot!=null){

                val documents = snapshot.documents
                documents.forEach{
                    val attendanceItem = it.toObject(Attendance::class.java)
                    if (attendanceItem != null){
                        attendanceItem.id = it.id
                        attendenceArrayList.add(attendanceItem!!)
                    }
                }
                _attendenceArrayList.value = attendenceArrayList
            }
        }
    }





    internal var attendance: MutableLiveData<ArrayList<Attendance>>
        get() {return _attendenceArrayList}

        set(value) {_attendenceArrayList = value}

}