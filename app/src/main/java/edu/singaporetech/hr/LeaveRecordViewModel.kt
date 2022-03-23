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
    private var _leaveType: MutableLiveData<ArrayList<LeaveType>> = MutableLiveData<ArrayList<LeaveType>>()

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
                        leaveRecords.add(leaveRecord!!)
                    }
                }
                _leaveRecords.value = leaveRecords
            }
        }
    }

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


    internal var leave: MutableLiveData<ArrayList<LeaveRecordViewAllItem>>
        get() {return _leaveRecords}
        set(value) {_leaveRecords = value}



}

private fun <T> MutableLiveData<T>.postValue(items: LeaveRecordViewAllItem) {

}



