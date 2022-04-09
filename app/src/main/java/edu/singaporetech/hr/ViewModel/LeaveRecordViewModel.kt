package edu.singaporetech.hr.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import edu.singaporetech.hr.data.Leave
import edu.singaporetech.hr.data.LeaveRecordViewAllItem

class LeaveRecordViewModel  : ViewModel(){
    private var _leaveRecords: MutableLiveData<ArrayList<LeaveRecordViewAllItem>> = MutableLiveData<ArrayList<LeaveRecordViewAllItem>>()
    private lateinit var firestore: FirebaseFirestore

    init{
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToLeaveRecord()
    }

    private fun listenToLeaveRecord() {
        firestore.collection("leave")
            .orderBy("leaveTimeStamp", Query.Direction.DESCENDING)
            .addSnapshotListener {
                snapshot, error ->
            if(error != null){
                Log.e("firestore Error", error.message.toString())
                return@addSnapshotListener
            }
            if(snapshot!=null){
                val leaveRecords = ArrayList<LeaveRecordViewAllItem>()
                val documents = snapshot.documents
                documents.forEach{
                    val leaveRecord = it.toObject(LeaveRecordViewAllItem::class.java)
                    if (leaveRecord != null){
                        leaveRecord.leaveId = it.id
                        var path = "${leaveRecord.leaveId}/${leaveRecord.imageName}.jpg"

                        val documents =FirebaseStorage.getInstance().reference.child("images/$path")

                        leaveRecords.add(leaveRecord!!)
                    }
                }
                _leaveRecords.value = leaveRecords
            }
        }
    }

    fun updateAnnualLeaveBalance(leave: Double){
        val document = firestore.collection("leaveType").document(
            "imEaChWkIuehvw3yxTDu")
        val set = document.update("annualLeaveBalance" ,FieldValue.increment(leave) )
        set.addOnSuccessListener {
            Log.d("firebase", "document saved!!!!!!")
        }
        set.addOnFailureListener {
            Log.d("firebase", "save failed!!!!")
        }
    }

    fun updateSickLeaveBalance(leave: Double){

        val document = firestore.collection("leaveType").document("imEaChWkIuehvw3yxTDu")
        val set = document.update("sickLeaveBalance" ,FieldValue.increment(leave) )
        set.addOnSuccessListener {
            Log.d("firebase", "document saved")
        }
        set.addOnFailureListener {
            Log.d("firebase", "save failed")
        }
    }

    fun updateMaternityLeaveBalance(leave:Double){
        val document = firestore.collection("leaveType").document("imEaChWkIuehvw3yxTDu")
        val set = document.update("maternityLeaveBalance" ,FieldValue.increment(leave ))
        set.addOnSuccessListener {
            Log.d("firebase", "document saved")
        }
        set.addOnFailureListener {
            Log.d("firebase", "save failed")
        }
    }

    internal fun delete(leave: LeaveRecordViewAllItem){
        val document = firestore.collection("leave").document(leave.leaveId)
        //leave.leaveId = document.id
        val task = document.delete();
        task.addOnSuccessListener {
            Log.e("firebase", "Event ${leave.leaveId} Deleted")
        }
        task.addOnFailureListener {
            Log.e("firebase", "Event ${leave.leaveId} Failed to delete.  Message: ${it.message}")
        }
    }

    internal var leave: MutableLiveData<ArrayList<LeaveRecordViewAllItem>>
        get() {return _leaveRecords}
        set(value) {_leaveRecords = value}

}




