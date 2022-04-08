package edu.singaporetech.hr.validator

import org.junit.runner.RunWith
import org.junit.runners.JUnit4
/*
    AttendenceActivityValidator: Attendence Activity Validator
        --place the code from the Fragment and check against the
        AttendenceActivityValidatorTest test cases
 */
@RunWith(JUnit4::class)
object AttendenceActivityValidator {

    /*
    -- input for attendence submit issue is empty
        - reason is empty (reasonET)
     */
    fun validateAttendenceInput(
        reason:String)
    :Boolean{
        var valid = true

        if (reason.isEmpty()  ) {
            valid = false
            return valid
        }
        return valid
    }

}