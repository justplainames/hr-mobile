package edu.singaporetech.hr

import java.util.*

data class Attendance(
    var id: String? = "", //document ID
    var ClockInDate: Date? = null,//String? = ""  ,
    var ClockOutDate:  Date? = null,
    var IssueReason: String? = "",
    var IssueStatus:  String? = "",
    var UserID: String? = "",
    var attendanceStatus: String? = ""
)
