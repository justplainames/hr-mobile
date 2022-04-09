package edu.singaporetech.hr.data

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

/*
    LeaveRecordViewAllItem: Leave Record View All Item (data class)
      Declare the data type along with the variable
      -- same as the one stored in the firebase
      -- used to display within the Leave Record Fragment Page
 */

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
