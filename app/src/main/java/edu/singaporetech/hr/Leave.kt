package edu.singaporetech.hr

data class Leave(
    val leaveId: String,
    val leaveType: String,
    val leaveStartDate: String,
    val leaveEndDate: String,
    val leaveDay: String,
    val leaveSupervisor: String,
    val leaveReason: String
    ) {
        internal constructor(
            leaveType: String,
            leaveStartDate: String,
            leaveEndDate: String,
            leaveDay: String,
            leaveSupervisor: String,
            leaveReason: String
        ): this(
            leaveId = "",
            leaveType=leaveType,
            leaveStartDate=leaveStartDate,
            leaveEndDate=leaveEndDate,
            leaveDay=leaveDay,
            leaveSupervisor=leaveSupervisor,
            leaveReason=leaveReason
        ){}


}