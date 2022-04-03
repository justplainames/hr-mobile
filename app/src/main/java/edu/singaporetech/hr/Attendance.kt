package edu.singaporetech.hr

import com.google.firebase.database.Exclude
import java.util.*

data class Attendance(
    var id: String? = "", //document ID
    var ClockInDate: Date? = null,//String? = ""  ,
    var ClockOutDate:  Date? = null,
    var IssueReason: String? = "",
    var IssueStatus:  String? = "",
    var UserID: String? = "",
    var attendanceStatus: String? = ""
){
    @Exclude
    fun getMap(): Map<String,Any?>{
        return  mapOf(
            "id" to id
        )
    }
}

data class UpdateAttendance(
    var id: String? = "",
    var IssueReason: String? = ""
){
    @Exclude
    fun getMap(): Map<String,Any?>{
        return  mapOf(
            "id" to id
        )
    }
}

