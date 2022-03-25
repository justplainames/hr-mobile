package edu.singaporetech.hr.leave

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class LeaveRecordViewAllItem (
    var selected: Boolean = false,
    var leaveId: String = "",
    val leaveType: String ="",
    val leaveStartDate: String="",
    val leaveEndDate: String="",
    val leaveDay: String="",
    val leaveStatus: String="",
    @ServerTimestamp
    var leaveTimeStamp: Date? = null )
