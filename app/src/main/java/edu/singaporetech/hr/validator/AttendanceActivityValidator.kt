package edu.singaporetech.hr.validator

import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/*
    AttendanceActivityValidator: Attendance Activity Validator
        --place the code from the Fragment and check against the
        AttendanceActivityValidatorTest test cases
 */
@RunWith(JUnit4::class)
object AttendanceActivityValidator {

    /*
    -- input for attendence submit issue is empty
        - reason is empty (reasonET)
     */
    fun validateAttendanceInput(
        reason: String
    )
            : Boolean {
        var valid = true

        if (reason.isEmpty()) {
            valid = false
            return valid
        }
        return valid
    }

}