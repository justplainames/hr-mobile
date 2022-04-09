package edu.singaporetech.hr.ViewModel

import android.service.controls.ControlsProviderService.TAG
import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.*
import edu.singaporetech.hr.Attendance
import java.lang.Exception
import java.util.*


/*
   AttendanceViewModel: Attendance ViewModel
        - Use to link the Firestore database with the fragments
        - Listener is setup to obtain the records from Attendance collection
        - Used to view the attributes from Attendance collection, update attendance record with issue reason
 */

class AttendanceViewModel : ViewModel() {
    private var _attendenceArrayList: MutableLiveData<ArrayList<Attendance>> =
        MutableLiveData<ArrayList<Attendance>>()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var myRef = FirebaseDatabase.getInstance().getReference("Attendance")
    open var attendenceArrayList = ArrayList<Attendance>()

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?> get() = _result

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToAttendanceRecord()
    }

    private fun listenToAttendanceRecord() {
        attendenceArrayList.clear()
        firestore.collection("Attendance")
            .orderBy("clockInDate", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("firestore Error", error.message.toString())
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val documents = snapshot.documents
                    documents.forEach {
                        val attendanceItem = it.toObject(Attendance::class.java)

                        if (attendanceItem != null) {
                            attendanceItem.id = it.id
                            attendenceArrayList.add(attendanceItem!!)
                        }
                    }
                    _attendenceArrayList.value = attendenceArrayList
                }
            }
    }

    internal fun updateAttendanceRecord(id: String, reason: Editable?): Boolean {
        var submitted = true
        val attendanceRecord = HashMap<String, Any>()
        val updatedReason = reason.toString()
        attendanceRecord.put("issueReason", updatedReason)

        val document = firestore.collection("Attendance").document(id)

        document
            .update("issueReason", updatedReason)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully updated!")
                submitted = true

            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating document", e)
                submitted = false
            }
        return submitted
    }


    internal var attendance: MutableLiveData<ArrayList<Attendance>>
        get() {
            return _attendenceArrayList
        }
        set(value) {
            _attendenceArrayList = value
        }
}
