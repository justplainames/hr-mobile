package edu.singaporetech.hr.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.*
import edu.singaporetech.hr.data.Payslip
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


/*
   PayslipViewModel: Payslip ViewModel
        - Use to link the Firestore database with the fragments
        - Listener is setup to obtain the records from the database
        - Used to view the attributes from the database
 */

class PayslipViewModel : ViewModel() {
    private var _payslipArrayList: MutableLiveData<ArrayList<Payslip>> =
        MutableLiveData<ArrayList<Payslip>>()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var date: Date
    open var payslipArrayList = ArrayList<Payslip>()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToPayslipRecord()


    }

    private fun listenToPayslipRecord() {
        GlobalScope.launch {
            Thread.sleep(100)
        firestore.collection("payslip").addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("firestore Error", error.message.toString())
                return@addSnapshotListener
            }
            if (snapshot != null) {

                val documents = snapshot.documents
                documents.forEach {
                    val payslipItem = it.toObject(Payslip::class.java)
                    if (payslipItem != null) {
                        payslipItem.payslipID = it.id
                        payslipArrayList.add(payslipItem!!)
                    }
                }
                _payslipArrayList.value = payslipArrayList
            }
        }
        }
    }


    internal var payslip: MutableLiveData<ArrayList<Payslip>>
        get() {
            return _payslipArrayList
        }
        set(value) {
            _payslipArrayList = value
        }
}
