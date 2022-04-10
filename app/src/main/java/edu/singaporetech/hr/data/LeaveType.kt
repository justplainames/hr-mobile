package edu.singaporetech.hr.data

data class LeaveType(
    var annualLeaveTotal: Double = 0.0,
    var annualLeaveBalance: Double = 0.0,

    var maternityLeaveTotal: Double = 0.0,
    var maternityLeaveBalance: Double = 0.0,

    var sickLeaveTotal: Double = 0.0,
    var sickLeaveBalance: Double = 0.0) {
}