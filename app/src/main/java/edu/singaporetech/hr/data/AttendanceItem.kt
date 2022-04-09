package edu.singaporetech.hr.data

import android.os.Parcelable
import com.google.type.DateTime
import java.util.*

/*
    AttendenceItem: Attendance Item (data class)
  Declare the data type along with the variable
  -- same as the one stored in the firebase

 */

data class AttendanceItem(
    var id: String = "", //document ID
    var clockInDate: Date? = null,
    var clockInAddress: String? = null,
    var clockOutDate: Date? = null,
    var clockOutAddress: String? = null,
    var IssueReason: String ?= null,
    var IssueStatus: String ?= null,
    var UserID: String ?= null,
    var attendanceStatus: String ?= null
)


