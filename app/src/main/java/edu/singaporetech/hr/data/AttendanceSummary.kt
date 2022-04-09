package edu.singaporetech.hr.data

/*
    AttendanceSummary: Attendance Summary (data class)
      Declare the data type along with the variable
      -- same as the one stored in the firebase
      -- used to display within the Home Fragment Page
 */
data class AttendanceSummary(
    var daysWorked: Int,
    var hoursWorked: Int,
    var minutesWorked: Int,
    var daysMissed: Int,
    var totalOT: Float,
    var percentageMissed: Float,
    var daysLate:Int,
    var daysOnTime: Int
)