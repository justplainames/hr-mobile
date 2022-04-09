package edu.singaporetech.hr.data

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

/*
    Leave: Leave (data class)
      Declare the data type along with the variable
      -- same as the one stored in the firebase
      -- used to display within the Leave Fragment Page
 */

data class Leave(
    var leaveId: String = "",
    var leaveStartDate: String? = null,
    var leaveEndDate: String? = null,
    var leaveDay: String? = null,
    var leaveNoOfDays: String? = null,
    var leaveReason: String? = null,
    var leaveStatus: String? = null,
    var leaveSupervisor: String? = null,
    var leaveType: String? = null,
    var imageName: String? = "",
    var imageNamee: String? = "",
    var imageRef: String? = "",
    @ServerTimestamp
    var leaveTimeStamp:Date? = null ){

}

