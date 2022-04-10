package edu.singaporetech.hr.data

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
    val leaveReason: String = "",
    val leaveSupervisor: String = "",
    var imageName: String? = "",
    var imageNamee: String? = "",
    var imageRef: String? = "",
    var leaveNoOfDays: String? = null,
    @ServerTimestamp
    var leaveTimeStamp: Date? = null )
