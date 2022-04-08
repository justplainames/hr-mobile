package edu.singaporetech.hr.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.*
import edu.singaporetech.hr.data.Payslip
import java.util.*

/*
   PayslipConsoViewModel: Payslip Consolidated ViewModel
        - Use to link the Firestore database with the fragments
        - used to pass monthTo, yearTo, monthFrom
            & yearFrom into the database to check for matches in records in database
        - Listener is setup to obtain the record that matches from the database
 */

class PayslipConsoViewModel(application: Application, private var monthTo: String, private var  yearTo: String, private var monthFrom: String, private var yearFrom: String) : ViewModel() {
    private var _payslipArrayList: MutableLiveData<ArrayList<Payslip>> = MutableLiveData<ArrayList<Payslip>>()
    private var firestore: FirebaseFirestore

    private lateinit var date:Date
    open var payslipArrayList = ArrayList<Payslip>()
    init{
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToPayslipFilter(monthTo,yearTo,monthFrom,yearFrom)

    }

    fun listenToPayslipFilter(
        monthTo: String,
        yearTo: String,
        monthFrom: String,
        yearFrom: String
    ) {
        val fromDate:Date = Date("${monthFrom} 28 ${yearFrom}")
        val toDate:Date = Date("${monthTo} 29 ${yearTo}")
        firestore.collection("payslip").whereGreaterThanOrEqualTo("dateOfPayDay",fromDate).whereLessThanOrEqualTo("dateOfPayDay",toDate).addSnapshotListener {
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



    internal var payslip:MutableLiveData<ArrayList<Payslip>>
        get() {return _payslipArrayList}

        set(value) {_payslipArrayList = value}
}
