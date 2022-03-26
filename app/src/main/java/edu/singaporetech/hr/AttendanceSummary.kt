package edu.singaporetech.hr


data class AttendanceSummary(
    var daysWorked: Int,
    var hoursWorked: Int,
    var minutesWorked: Int,
    var daysMissed: Int,
    var totalOT: Float,
    var percentageMissed: Float
)