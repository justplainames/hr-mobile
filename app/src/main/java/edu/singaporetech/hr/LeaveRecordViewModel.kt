package edu.singaporetech.hr

import android.content.ContentValues.TAG
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import edu.singaporetech.hr.leave.LeaveRecordViewAllItem
import java.io.File

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

                //fetchImage(leave )
                val documents = snapshot.documents
                documents.forEach{

                    val leaveRecord = it.toObject(LeaveRecordViewAllItem::class.java)
                    if (leaveRecord != null){
                        leaveRecord.leaveId = it.id


                        var path = "${leaveRecord.leaveId}/${leaveRecord.imageName}"
                        val documents = FirebaseStorage.getInstance().getReference("images/$path")

                        val localFile = File.createTempFile("path", "jpg")
                        documents.getFile(localFile)
                            ?.addOnSuccessListener {
                                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                                leaveRecord.imageName = path
                                Log.d("firebase", "get image!")
                        }
                            ?.addOnFailureListener {
                                Log.d("firebase", "save failed")
                            }
                        leaveRecords.add(leaveRecord!!)
                    }
                }
                _leaveRecords.value = leaveRecords
            }
        }
    }

    internal fun updateLeaveRecord(searchText:String){
        firestore.collection("leave")
            .orderBy("leaveType")
            .startAt("Annual").endAt("Annual" + "\uf8ff")
            .addSnapshotListener{
                    value: QuerySnapshot?,
                    error : FirebaseFirestoreException? ->
                if (error != null){
                    Log.e("firestore Error", error.message.toString())
                }

                for (dc: DocumentChange in value?.documentChanges!!){
                    if(dc.type == DocumentChange.Type.ADDED){
                        var items = dc.document.toObject(LeaveRecordViewAllItem::class.java)
                        _leaveRecords.postValue(items)
                    }
                }
//                    leaveAdapter.notifyDataSetChanged()
            }
    }

    fun updateAnnualLeaveBalance(){
        val document = firestore.collection("leaveType").document("imEaChWkIuehvw3yxTDu")
        val set = document.update("annualLeaveBalance" ,FieldValue.increment(1) )
        set.addOnSuccessListener {
            Log.d("firebase", "document saved!!!!!!")
        }
        set.addOnFailureListener {
            Log.d("firebase", "save failed!!!!")
        }
    }

    fun updateSickLeaveBalance(){
        val document = firestore.collection("leaveType").document("imEaChWkIuehvw3yxTDu")
        val set = document.update("sickLeaveBalance" ,FieldValue.increment(1) )
        set.addOnSuccessListener {
            Log.d("firebase", "document saved")
        }
        set.addOnFailureListener {
            Log.d("firebase", "save failed")
        }
    }

    fun updateMaternityLeaveBalance(){
        val document = firestore.collection("leaveType").document("imEaChWkIuehvw3yxTDu")
        val set = document.update("maternityLeaveBalance" ,FieldValue.increment(1) )
        set.addOnSuccessListener {
            Log.d("firebase", "document saved")
        }
        set.addOnFailureListener {
            Log.d("firebase", "save failed")
        }
    }
    fun fetchImage(leave: Leave){

        var path = "${leave.leaveId}/${leave.imageName}"
        val documents = FirebaseStorage.getInstance().getReference("images/$path")

        val localFile = File.createTempFile("path", "jpg")
        documents.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

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

private fun <T> MutableLiveData<T>.postValue(items: LeaveRecordViewAllItem) {

}



