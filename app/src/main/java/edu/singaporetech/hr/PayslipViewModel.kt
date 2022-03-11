package edu.singaporetech.hr

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.*
import java.util.Date


class PayslipViewModel : ViewModel() {
    private var _payslipArrayList: MutableLiveData<ArrayList<Payslip>> = MutableLiveData<ArrayList<Payslip>>()
    private var firestore: FirebaseFirestore
    private lateinit var date:Date
    open var payslipArrayList = ArrayList<Payslip>()

    init{
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToPayslipRecord()
        getLatestTimeStamp()
    }

     fun listenToPayslipRecord(){
        firestore.collection("payslip").addSnapshotListener {
                snapshot, error ->
            if(error != null){
                Log.e("firestore Error", error.message.toString())
                return@addSnapshotListener
            }
            if(snapshot!=null){

                val documents = snapshot.documents
                documents.forEach{
                    val payslipItem = it.toObject(Payslip::class.java)
                    if (payslipItem != null){
                        payslipItem.payslipID = it.id
                        payslipArrayList.add(payslipItem!!)
                    }
                }
                _payslipArrayList.value = payslipArrayList
            }
        }

    }

    fun getLatestTimeStamp(){

        firestore.collection("payslip").orderBy("dateOfPayDay, desc").limit(1).addSnapshotListener {
                snapshot, error ->
            if(error != null){
                Log.e("firestore Error", error.message.toString())
                return@addSnapshotListener
            }
            if(snapshot!=null){

                val documents = snapshot.documents
                documents.forEach{
                    val payslipItem = it.toObject(Payslip::class.java)
                    if (payslipItem != null){
                        payslipItem.payslipID = it.id

                        payslipArrayList.add(payslipItem!!)
                    }
                }
                _payslipArrayList.value = payslipArrayList
                date= payslipArrayList.get(0).dateOfPayDay!!

            }
        }
    }


    internal var payslip:MutableLiveData<ArrayList<Payslip>>
        get() {return _payslipArrayList}
        set(value) {_payslipArrayList = value}
}
