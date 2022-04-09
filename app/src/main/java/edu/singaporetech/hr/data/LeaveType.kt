package edu.singaporetech.hr.data

/*
    LeaveType: Leave Type (data class)
      Declare the data type along with the variable
      -- same as the one stored in the firebase
      -- used for total leave and leave balance
 */

data class LeaveType(
    var annualLeaveTotal: Double = 0.0,
    var annualLeaveBalance: Double = 0.0,

    var maternityLeaveTotal: Double = 0.0,
    var maternityLeaveBalance: Double = 0.0,

    var sickLeaveTotal: Double = 0.0,
    var sickLeaveBalance: Double = 0.0) {
}