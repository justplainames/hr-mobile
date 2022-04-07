package edu.singaporetech.hr.data


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