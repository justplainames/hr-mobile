package edu.singaporetech.hr


import android.os.Parcelable
import android.util.Log

import com.google.firebase.firestore.DocumentSnapshot
import java.util.*

data class Payslip(

    var payslipID: String? = "",//Document ID is actually the payslipID
    val totalDeduction: Double?=0.0,

    val cpf: Double?=0.0,

    val asstFund: Double?=0.0,

    val totalEarning: Double?=0.0,

    val basicWage: Double?=0.0,

    val bonus: Double?=0.0,

    val opeOthers: Double?=0.0,

    var dateOfPayDay: Date? = null,

    val netPay: Double? =0.0,

    val ot: Double?=0.0


)


