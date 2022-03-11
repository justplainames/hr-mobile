package edu.singaporetech.hr.leave

data class LeaveRecordViewAllItem (
    var selected: Boolean = false,
    var leaveId: String = "",
    val leaveType: String ="",
    val leaveStartDate: String="",
    val leaveEndDate: String="",
    val leaveDay: String="",
    val leaveStatus: String="")