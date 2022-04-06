package edu.singaporetech.hr

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ServerTimestamp
import java.sql.Timestamp
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
    var imageName: String? = "",
    var imageNamee: String? = "",
    var imageRef: String? = "",
    @ServerTimestamp
    var leaveTimeStamp:Date? = null ){

}

