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

                //fetchImage(leave )
                val documents = snapshot.documents
                documents.forEach{

                    val leaveRecord = it.toObject(LeaveRecordViewAllItem::class.java)
                    if (leaveRecord != null){
                        leaveRecord.leaveId = it.id


                        var path = "${leaveRecord.leaveId}/${leaveRecord.imageName}.jpg"
                        //val documents = FirebaseStorage.getInstance().getReference("images/$path")
//                        val documents = FirebaseStorage.getInstance().getReference("images/$path")
//
//                        val localFile = File.createTempFile("path", "jpg")
//                        documents.getFile(localFile).addOnSuccessListener {
                        val documents =FirebaseStorage.getInstance().reference.child("images/$path")
//                        val task = documents.putFile()
//                        val file = documents.downloadUrl
//                        leaveRecord.imageName=file.toString()
                        Log.e("firestoreee Error", leaveRecord.imageName.toString())
//                            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
//                            return@addOnSuccessListener bitmap
//                        }

//                        documents.getFile(localFile)
//                            ?.addOnSuccessListener {
//                                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
//                                leaveRecord.imageName = bitmap
//                                Log.d("firebase", "get image!")
//                        }
//                            ?.addOnFailureListener {
//                                Log.d("firebase", "save failed")
//                            }
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
//        var noOfDays = leave.leaveNoOfDays
        val document = firestore.collection("leaveType").document("imEaChWkIuehvw3yxTDu")
        val set = document.update("maternityLeaveBalance" ,FieldValue.increment(leave ))
        set.addOnSuccessListener {
            Log.d("firebase", "document saved")
        }
        set.addOnFailureListener {
            Log.d("firebase", "save failed")
        }
    }
    fun fetchImage(leave: Leave){

//        var path = "${leave.leaveId}/${leave.imageName}.jpg"
//        val documents = FirebaseStorage.getInstance().getReference("images/$path")
//
//        val localFile = File.createTempFile("path", "jpg")
//        documents.getFile(localFile).addOnSuccessListener {
//
//            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
////            return@addOnSuccessListener bitmap
//        }
//        val documents =FirebaseStorage.getInstance().getReference("images/${leave.leaveId}/$location")
//        val file = documents.downloadUrl
//        documents.putFile() }
//            ?.addOnSuccessListener {
//                Log.d("firebase", "save image!")
////                document.getDown
//                leave.imageName = documents.downloadUrl.toString()
//                Log.d("firebaseeeeee", leave.imageName.toString())
//            }
//            ?.addOnFailureListener {
//                Log.d("firebaseeeeeeee", "save failed")
//            }
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



