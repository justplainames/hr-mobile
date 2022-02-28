package edu.singaporetech.hr.leave

data class LeaveRecordViewAllItem (
    var selected: Boolean,
    val leaveRecordViewAllType: String,
    val leaveRecordViewAllStartDate: String,
    val leaveRecordViewAllEndDate: String,
    val leaveRecordViewAllDays: String,
    val leaveRecordViewAllStatus: String)