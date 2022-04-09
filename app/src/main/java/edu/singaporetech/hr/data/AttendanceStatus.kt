package edu.singaporetech.hr.data

import java.util.*
/*
    AttendanceStatus: Attendance Status (data class)
  Declare the data type along with the variable
  -- same as the one stored in the firebase
  -- used in AttendanceClockViewModel for Clock In and Out status

 */
data class AttendanceStatus(

    var late : Double = 0.0,
    var onTime : Double = 0.0
)