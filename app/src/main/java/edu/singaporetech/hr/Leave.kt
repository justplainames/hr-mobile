package edu.singaporetech.hr

import java.util.*

data class Leave(
    var leaveId: String = "",
    var leaveStartDate: String? = null,
    var leaveEndDate: String? = null,
    var leaveDay: String? = null,
    var leaveReason: String? = null,
    var leaveStatus: String? = null,
    var leaveSupervisor: String? = null,
    var leaveType: String? = null,
    var annualLeave: Double? = 0.0,
    var sickLeave: Double? = null,
    var childcareLeave: Double? = null,
    var maternityLeave: Double? = null){

//    override fun toString(): String {
//        return "$leaveType"
//    }
}

//data class Leave {
//    var leaveStartDate: Date? = null
//    var leaveEndDate: Date? = null
//    var leaveDay: Double? = null
//    var leaveReason: String? = null
//    var leaveStatus: String? = null
//    var leaveSupervisor: String? = null
//    var leaveType: String? = null
//    var annualLeave: Double? = null
//    var sickLeave: Double? = null
//    var childcareLeave: Double? = null
//    var maternityLeave: Double? = null
//
//    constructor(){}
//
//    constructor(
//        leaveStartDate: Date?,
//        leaveEndDate: Date?,
//        leaveDay: Double?,
//        leaveReason: String?,
//        leaveStatus: String?,
//        leaveSupervisor: String? ,
//        leaveType: String?,
//        annualLeave: Double?,
//        sickLeave: Double?,
//        childcareLeave: Double?,
//        maternityLeave: Double?
//    ){
//        this.leaveStartDate = leaveStartDate
//        this.leaveEndDate = leaveEndDate
//        this.leaveDay = leaveDay
//        this.leaveReason = leaveReason
//        this.leaveStatus = leaveStatus
//        this.leaveSupervisor = leaveSupervisor
//        this.leaveType = leaveType
//        this.annualLeave = annualLeave
//        this.sickLeave = sickLeave
//        this.childcareLeave = childcareLeave
//        this.maternityLeave = maternityLeave
//    }
//}


//data class Leave(
//    val leaveId: String,
//    val leaveType: String,
//    val leaveStartDate: String,
//    val leaveEndDate: String,
//    val leaveDay: String,
//    val leaveSupervisor: String,
//    val leaveReason: String
//    ) {
//        internal constructor(
//            leaveType: String,
//            leaveStartDate: String,
//            leaveEndDate: String,
//            leaveDay: String,
//            leaveSupervisor: String,
//            leaveReason: String
//        ): this(
//            leaveId = "",
//            leaveType=leaveType,
//            leaveStartDate=leaveStartDate,
//            leaveEndDate=leaveEndDate,
//            leaveDay=leaveDay,
//            leaveSupervisor=leaveSupervisor,
//            leaveReason=leaveReason
//        ){}
//
//
//}