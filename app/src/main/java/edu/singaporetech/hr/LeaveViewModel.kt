package edu.singaporetech.hr

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.*

class LeaveViewModel : ViewModel(){
    private var _leaveRecords: MutableLiveData<ArrayList<Leave>> = MutableLiveData<ArrayList<Leave>>()
    private lateinit var firestore: FirebaseFirestore
    private var _leave = MutableLiveData<List<Leave>>()
    open var leaveArrayList = ArrayList<Leave>()
//    private var _leaveTypes: MutableLiveData<ArrayList<Leave>> = MutableLiveData<ArrayList<Leave>>()

    private var _leaveType: MutableLiveData<ArrayList<LeaveType>> = MutableLiveData<ArrayList<LeaveType>>()

    private var _leaveTypes = MutableLiveData<List<LeaveType>>()


    init{
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToLeaveRecord()
        listenToLeaveBalance()
    }


    private fun listenToLeaveRecord() {
        firestore.collection("leave").addSnapshotListener {
                snapshot, error ->
            if(error != null){
                Log.e("firestore Error", error.message.toString())
                return@addSnapshotListener
            }
            if(snapshot!=null){
                val leaveRecords = ArrayList<Leave>()
                val documents = snapshot.documents
                documents.forEach{
                    val leaveRecord = it.toObject(Leave::class.java)
                    if (leaveRecord != null){
                        leaveRecord.leaveId = it.id
                        leaveRecords.add(leaveRecord!!)
                    }
                }
                _leaveRecords.value = leaveRecords
            }
        }
    }

    private fun listenToLeaveBalance() {
        firestore.collection("leaveType")
            .addSnapshotListener {
                snapshot, error ->
            if(error != null){
                Log.e("firestore Error", error.message.toString())
                return@addSnapshotListener
            }
            if(snapshot!=null){
                val leaveRecords = ArrayList<LeaveType>()
//                val documents = snapshot.documents
                snapshot!!.documents.forEach{
                    val leaveRecord = it.toObject(LeaveType::class.java)
                    if (leaveRecord != null){
//                        leaveRecord.leaveId = it.id
                        leaveRecords.add(leaveRecord!!)
                    }
                }
                _leaveType.value = leaveRecords
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

    private fun displayVisualization(liveData: MutableLiveData<ArrayList<Leave>>){
        firestore.collection("leave").document("leaveTotal").collection("annualLeave")



    }

    fun save(leave: Leave){
//        val document = if(leave.leaveId != null && !leave.leaveId.isEmpty())
        val document = firestore.collection("leave").document()
        leave.leaveId = document.id
        val set = document.set(leave)
            set.addOnSuccessListener {
                Log.d("firebase", "document saved")
            }
            set.addOnFailureListener {
                Log.d("firebase", "save failed")
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
                            var items = dc.document.toObject(Leave::class.java)
                            _leaveRecords.postValue(items)
                        }
                    }
//                    leaveAdapter.notifyDataSetChanged()
                }
            }

    internal fun fetchLeaveTypes(){
        firestore.collection("leaveType").document("leaveTotal")
//            .orderBy("", "desc")
            .addSnapshotListener{
                    querySnapshot,
                    firebaseFirestoreException ->
                var item = querySnapshot?.toObject(LeaveType::class.java)
                _leaveType.postValue(item!!)
//                if (error != null){
//                    Log.e("firestore Error", error.message.toString())
//                }

//                for (dc: DocumentChange in value?.documentChanges!!){
//                    if(dc.type == DocumentChange.Type.ADDED){
//                        var items = dc.document.toObject(Leave::class.java)
//                        _leaveRecords.postValue(items)
//                    }
//                }
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

    fun displaytextView():Double?{
        return(leaveArrayList.get(0).annualLeave)
    }

    internal var leave: MutableLiveData<ArrayList<Leave>>
        get() {return _leaveRecords}
        set(value) {_leaveRecords = value}

    internal var leaveType: MutableLiveData<ArrayList<LeaveType>>
        get() {return _leaveType}
        set(value) {_leaveType = value}




}

private fun <T> MutableLiveData<T>.postValue(item: LeaveType) {

}

private fun <T> MutableLiveData<T>.postValue(items: Leave) {

}


//        var eventCollection = firestore.collection("leave")
//            .document()
//        eventCollection.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
//            var innerEvents = querySnapshot?.toObject(Leave::class.java)
//            _leave.postValue(arrayListOf(innerEvents!!))




