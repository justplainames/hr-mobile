package edu.singaporetech.hr

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.*
import edu.singaporetech.hr.leave.LeaveRecordViewAllItem

class LeaveRecordViewModel  : ViewModel(){
    private var _leaveRecords: MutableLiveData<ArrayList<LeaveRecordViewAllItem>> = MutableLiveData<ArrayList<LeaveRecordViewAllItem>>()
    private lateinit var firestore: FirebaseFirestore
    private var _leave = MutableLiveData<List<LeaveRecordViewAllItem>>()
    open var leaveArrayList = ArrayList<LeaveRecordViewAllItem>()
    private var _leaveTypes: MutableLiveData<ArrayList<LeaveRecordViewAllItem>> = MutableLiveData<ArrayList<LeaveRecordViewAllItem>>()


    init{
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToLeaveRecord()
    }

    private fun listenToLeaveRecord() {
        firestore.collection("leave").addSnapshotListener {
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
                        leaveRecords.add(leaveRecord!!)
                    }
                }
                _leaveRecords.value = leaveRecords
            }
        }
    }
//
//    private fun getLeaveData() {
//        firestore.collection("leave")
//            .addSnapshotListener{
//                    value: QuerySnapshot?,
//                    error : FirebaseFirestoreException? ->
//                    if (error != null){
//                        Log.e("firestore Error", error.message.toString())
//                    }
//
//                    for (dc: DocumentChange in value?.documentChanges!!){
//                        if(dc.type == DocumentChange.Type.ADDED){
//                            _leaveRecords.add(dc.document.toObject(Leave::class.java))
//                        }
//                    }
//
//                    leaveAdapter.notifyDataSetChanged()
//                }
//
//            })
//    }

//    private fun displayVisualization(liveData: MutableLiveData<ArrayList<Leave>>){
//        firestore.collection("leave").document()
//
//
//    }

//    fun save(leave: Leave){
////        val document = if(leave.leaveId != null && !leave.leaveId.isEmpty())
//        val document = firestore.collection("leave").document()
//        leave.leaveId = document.id
//        val set = document.set(leave)
//        set.addOnSuccessListener {
//            Log.d("firebase", "document saved")
//        }
//        set.addOnFailureListener {
//            Log.d("firebase", "save failed")
//        }
//    }

    internal fun fetchItems(){

        firestore.collection("leave")
//            .orderBy("", "desc")
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

//    internal fun displayVisualization(){
//
//        firestore.collection("leaveType")
////            .orderBy("", "desc")
//            .addSnapshotListener{
//                    value: QuerySnapshot?,
//                    error : FirebaseFirestoreException? ->
//                if (error != null){
//                    Log.e("firestore Error", error.message.toString())
//                }
//
//                for (dc: DocumentChange in value?.documentChanges!!){
//                    if(dc.type == DocumentChange.Type.ADDED){
//                        var items = dc.document.toObject(Leave::class.java)
//                        _leaveTypes.postValue(items)
//                    }
//                }
//
////                    leaveAdapter.notifyDataSetChanged()
//            }
//
//    }

//    fun displaytextView():Double?{
//        return(leaveArrayList.get(0).annualLeave)
//    }

    internal var leave: MutableLiveData<ArrayList<LeaveRecordViewAllItem>>
        get() {return _leaveRecords}
        set(value) {_leaveRecords = value}



}

private fun <T> MutableLiveData<T>.postValue(items: LeaveRecordViewAllItem) {

}



//        var eventCollection = firestore.collection("leave")
//            .document()
//        eventCollection.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
//            var innerEvents = querySnapshot?.toObject(Leave::class.java)
//            _leave.postValue(arrayListOf(innerEvents!!))





