package edu.singaporetech.hr

import android.service.controls.ControlsProviderService.TAG
import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import edu.singaporetech.hr.leave.LeaveRecordViewAllItem
import java.lang.Exception
import java.util.*


class AttendanceModel : ViewModel() {
    private var _attendenceArrayList: MutableLiveData<ArrayList<Attendance>> = MutableLiveData<ArrayList<Attendance>>()
    private var firestore: FirebaseFirestore
    var myRef = FirebaseDatabase.getInstance().getReference("Attendance")
    open var attendenceArrayList = ArrayList<Attendance>()

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?> get() = _result

    init{
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToAttendanceRecord()


    }

    fun listenToAttendanceRecord(){
        attendenceArrayList.clear()
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

    internal fun fetchItems(){
        firestore.collection("Attendance")
//            .orderBy("", "desc")
            .addSnapshotListener{
                    value: QuerySnapshot?,
                    error : FirebaseFirestoreException? ->
                if (error != null){
                    Log.e("firestore Error", error.message.toString())
                }
                for (dc: DocumentChange in value?.documentChanges!!){
                    if(dc.type == DocumentChange.Type.ADDED){
                        var attendanceItem = dc.document.toObject(Attendance::class.java)
                        if (attendanceItem != null){
                            attendanceItem.id = dc.document.id
                            attendenceArrayList.add(attendanceItem!!)
                        }
                        _attendenceArrayList.postValue(attendenceArrayList)
                    }
                }

//                    leaveAdapter.notifyDataSetChanged()
            }
    }

    fun updateReason(id: String, reason: String) {

    }

    internal fun updateAttendanceRecord(id: String, reason: Editable?): Boolean {
        var submitted: Boolean = true
        val attendanceRecord = HashMap<String, Any>()
        //attendanceRecord.put("id",id)
        val updatedReason = reason.toString()
        attendanceRecord.put("issueReason", updatedReason)

        val document = firestore.collection("Attendance").document(id)

       // val updateAttendanceReason = document.update(attendanceRecord)
        document
            .update("issueReason", updatedReason.toString())
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
//
//        Log.d("reportBtn", "value: " + updateAttendanceReason.toString())
//        submitted = if(updateAttendanceReason.isSuccessful){
//            Log.d("reportBtn", "document saved!!!!!!")
//             true
//        }else{
//            Log.d("reportBtn", "save failed!!!!")
//             false
//        }
      //  val result = updateAttendanceReason.isSuccessful
     //   Log.d("reportBtn", result.toString())

//        updateAttendanceReason.addOnSuccessListener {
//            Log.d("reportBtn", "document saved!!!!!!")
//            submitted = true
//
//        }
//        updateAttendanceReason.addOnFailureListener {
//            Log.d("reportBtn", "save failed!!!!")
//            submitted = false
//        }
//        return submitted
//        myRef.child(id!!).setValue(reason).addOnCompleteListener{
//            if(it.isSuccessful){
//                _result.value = null
//            }else{
//                _result.value = it.exception
//            }
//        }

//        myRef.child(id!!).setValue(reason).addOnCompleteListener{
//            if(it.isSuccessful){
//                _result.value = null
//            }else{
//                _result.value = it.exception
//            }
//        }
//        database.child("users").child(userId).setValue(user)
//            .addOnSuccessListener {
//                // Write was successful!
//                // ...
//            }
//            .addOnFailureListener {
//                // Write failed
//                // ...
//            }






    internal var attendance:MutableLiveData<ArrayList<Attendance>>
        get() {return _attendenceArrayList}

        set(value) {_attendenceArrayList = value}
}
