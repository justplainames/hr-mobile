package edu.singaporetech.hr.ViewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import edu.singaporetech.hr.data.Leave
import edu.singaporetech.hr.data.LeaveType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LeaveViewModel : ViewModel() {
    private var _leaveRecords: MutableLiveData<ArrayList<Leave>> =
        MutableLiveData<ArrayList<Leave>>()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var _leaveType: MutableLiveData<ArrayList<LeaveType>> =
        MutableLiveData<ArrayList<LeaveType>>()
    var imageReff = ""

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToLeaveRecord()
        listenToLeaveBalance()
    }

    private fun listenToLeaveRecord() {
        GlobalScope.launch {
            Thread.sleep(100)
            firestore.collection("leave")
                .orderBy("leaveTimeStamp", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("firestore Error", error.message.toString())
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val leaveRecords = ArrayList<Leave>()
                        val documents = snapshot.documents
                        documents.forEach {
                            val leaveRecord = it.toObject(Leave::class.java)
                            if (leaveRecord != null) {
                                leaveRecord.leaveId = it.id
                                leaveRecords.add(leaveRecord!!)
                            }
                        }
                        _leaveRecords.value = leaveRecords
                    }
                }
        }
    }

    private fun listenToLeaveBalance() {
        GlobalScope.launch {
            Thread.sleep(100)
            firestore.collection("leaveType")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("firestore Error", error.message.toString())
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val leaveRecords = ArrayList<LeaveType>()
                        snapshot!!.documents.forEach {
                            val leaveRecord = it.toObject(LeaveType::class.java)
                            if (leaveRecord != null) {
                                leaveRecords.add(leaveRecord!!)
                            }
                        }
                        _leaveType.value = leaveRecords
                    }
                }
        }
    }

    fun save(leave: Leave, location: String, image_uri: Uri) {
        val document = firestore.collection("leave").document()
        leave.leaveId = document.id
        leave.imageNamee = "gs://csc2008-hr-app.appspot.com/images/${leave.leaveId}/$location"

        val documents =
            FirebaseStorage.getInstance().getReference("images/${leave.leaveId}/$location")
        GlobalScope.launch {
            Thread.sleep(100)
            image_uri.let { documents.putFile(it) }
                .addOnSuccessListener {
                    Log.d("firebase", "save image!")
                    documents.downloadUrl.addOnSuccessListener { Uri ->

                        imageReff = Uri.toString()
                        leave.imageRef = imageReff
                        val set = document.set(leave)
                        set.addOnSuccessListener {
                            Log.d("firebase", "document saved")
                        }
                        set.addOnFailureListener {
                            Log.d("firebase", "save failed")
                        }
                    }
                }
                ?.addOnFailureListener {
                    Log.d("firebaseeeeeeee", "save failed")
                }
        }
    }

    fun updateAnnualLeaveBalance(value: Double) {
        val document = firestore.collection("leaveType").document("imEaChWkIuehvw3yxTDu")
        val set = document.update("annualLeaveBalance", FieldValue.increment(-(value)))
        GlobalScope.launch {
            Thread.sleep(100)
            set.addOnSuccessListener {
                Log.d("firebase", "document saved!!!!!!")
            }
            set.addOnFailureListener {
                Log.d("firebase", "save failed!!!!")
            }
        }
    }

    fun updateSickLeaveBalance(value: Double) {
        val document = firestore.collection("leaveType").document("imEaChWkIuehvw3yxTDu")
        val set = document.update("sickLeaveBalance", FieldValue.increment(-(value)))
        GlobalScope.launch {
            Thread.sleep(100)
            set.addOnSuccessListener {
                Log.d("firebase", "document saved")
            }
            set.addOnFailureListener {
                Log.d("firebase", "save failed")
            }
        }
    }

    fun updateMaternityLeaveBalance(value: Double) {
        val document = firestore.collection("leaveType").document("imEaChWkIuehvw3yxTDu")
        val set = document.update("maternityLeaveBalance", FieldValue.increment(-(value)))
        GlobalScope.launch {
            Thread.sleep(100)
            set.addOnSuccessListener {
                Log.d("firebase", "document saved")
            }
            set.addOnFailureListener {
                Log.d("firebase", "save failed")
            }
        }
    }

    internal var leave: MutableLiveData<ArrayList<Leave>>
        get() {
            return _leaveRecords
        }
        set(value) {
            _leaveRecords = value
        }

    internal var leaveType: MutableLiveData<ArrayList<LeaveType>>
        get() {
            return _leaveType
        }
        set(value) {
            _leaveType = value
        }

}






