package edu.singaporetech.hr

import com.google.common.truth.Truth
import edu.singaporetech.hr.validator.AttendanceActivityValidator
import org.junit.Test
/*
    AttendenceActivityValidatorTest: Attendence Activity Validator Test
        - state all the test cases of the unit testing
        - state the condition based on the validator and its corresponding output
 */
class AttendenceActivityValidatorTest{
    /*
    Unit testing on AttendenceClockFragment :
    - - submit submit issue form (submitBtn)
    */
    /*
       empty reason input for reasonET
     */
    @Test
    fun emptytextViewLeaveStartDate() {
        val result= AttendanceActivityValidator.validateAttendanceInput(
            ""
        )
        return Truth.assertThat(result).isFalse()
    }
    /*
       successfully submitted with  input for reasonET
     */
    @Test
    fun submitIssueSuccess() {
        val result= AttendanceActivityValidator.validateAttendanceInput(
            "System Crashed due to peak hours, clock in is missed out"
        )
        return Truth.assertThat(result).isTrue()
    }
}